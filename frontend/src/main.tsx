import React, { FormEvent, useEffect, useMemo, useState } from 'react';
import ReactDOM from 'react-dom/client';
import { CalendarDays, History, Save, Settings, TrainFront } from 'lucide-react';
import './styles.css';

type View = 'dashboard' | 'history' | 'settings';

type CommuterPass = {
  id: number;
  userId: number;
  fromStation: string;
  toStation: string;
  oneWayFare: number;
  passPrice: number;
  startDate: string;
  endDate: string;
  memo: string | null;
  active: boolean;
};

type FormState = {
  fromStation: string;
  toStation: string;
  oneWayFare: string;
  passPrice: string;
  startDate: string;
  endDate: string;
  memo: string;
};

const emptyForm: FormState = {
  fromStation: '',
  toStation: '',
  oneWayFare: '',
  passPrice: '',
  startDate: '',
  endDate: '',
  memo: '',
};

function App() {
  const [view, setView] = useState<View>(getInitialView);
  const [commuterPass, setCommuterPass] = useState<CommuterPass | null>(null);
  const [form, setForm] = useState<FormState>(emptyForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    void loadCommuterPass();
  }, []);

  useEffect(() => {
    window.history.replaceState(null, '', `#${view}`);
  }, [view]);

  const roundTripFare = useMemo(() => {
    if (!commuterPass) {
      return 0;
    }
    return commuterPass.oneWayFare * 2;
  }, [commuterPass]);

  async function loadCommuterPass() {
    setLoading(true);
    setError('');
    try {
      const response = await fetch('/api/commuter-pass');
      if (response.status === 404) {
        setCommuterPass(null);
        setForm(emptyForm);
        return;
      }
      if (!response.ok) {
        throw new Error('定期情報を読み込めませんでした。');
      }
      const data = await response.json() as CommuterPass;
      setCommuterPass(data);
      setForm(toFormState(data));
    } catch (caught) {
      setError(caught instanceof Error ? caught.message : '定期情報を読み込めませんでした。');
    } finally {
      setLoading(false);
    }
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSaving(true);
    setNotice('');
    setError('');

    try {
      const payload = {
        fromStation: form.fromStation.trim(),
        toStation: form.toStation.trim(),
        oneWayFare: Number(form.oneWayFare),
        passPrice: Number(form.passPrice),
        startDate: form.startDate,
        endDate: form.endDate,
        memo: form.memo.trim() || null,
      };
      const response = await fetch(commuterPass ? `/api/commuter-pass/${commuterPass.id}` : '/api/commuter-pass', {
        method: commuterPass ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        const data = await response.json().catch(() => null) as { message?: string; errors?: string[] } | null;
        throw new Error(formatApiError(data));
      }
      const saved = await response.json() as CommuterPass;
      setCommuterPass(saved);
      setForm(toFormState(saved));
      setNotice('定期情報を保存しました。');
      setView('dashboard');
    } catch (caught) {
      setError(caught instanceof Error ? caught.message : '保存できませんでした。入力内容を確認してください。');
    } finally {
      setSaving(false);
    }
  }

  return (
    <main className="appShell">
      <nav className="sideNav" aria-label="Primary">
        <div className="brand">
          <TrainFront aria-hidden="true" size={24} />
          <span>Pass Meter</span>
        </div>
        <button className={`navItem ${view === 'dashboard' ? 'active' : ''}`} type="button" onClick={() => setView('dashboard')}>
          <CalendarDays aria-hidden="true" size={18} />
          Dashboard
        </button>
        <button className={`navItem ${view === 'history' ? 'active' : ''}`} type="button" onClick={() => setView('history')}>
          <History aria-hidden="true" size={18} />
          History
        </button>
        <button className={`navItem ${view === 'settings' ? 'active' : ''}`} type="button" onClick={() => setView('settings')}>
          <Settings aria-hidden="true" size={18} />
          Settings
        </button>
      </nav>

      {view === 'dashboard' && (
        <section className="content" id="dashboard">
          <header className="pageHeader">
            <div>
              <p className="eyebrow">Commuter Pass Meter</p>
              <h1>今日の出社を記録</h1>
            </div>
            <button className="primaryButton" type="button" disabled={!commuterPass}>出社</button>
          </header>

          <StatusMessage loading={loading} error={error} notice={notice} />

          {commuterPass ? (
            <>
              <section className="summaryBand" aria-label="Recovery summary">
                <div>
                  <p className="label">定期回収率</p>
                  <p className="metric">0%</p>
                </div>
                <div>
                  <p className="label">往復運賃</p>
                  <p className="metric">{formatYen(roundTripFare)}</p>
                </div>
                <div>
                  <p className="label">残り出社回数</p>
                  <p className="metric">未記録</p>
                </div>
              </section>

              <section className="detailBand" aria-label="Commuter pass details">
                <div>
                  <p className="label">区間</p>
                  <p className="detailValue">{commuterPass.fromStation} から {commuterPass.toStation}</p>
                </div>
                <div>
                  <p className="label">定期料金</p>
                  <p className="detailValue">{formatYen(commuterPass.passPrice)}</p>
                </div>
                <div>
                  <p className="label">期間</p>
                  <p className="detailValue">{commuterPass.startDate} - {commuterPass.endDate}</p>
                </div>
              </section>
            </>
          ) : (
            <section className="emptyState" aria-label="No commuter pass">
              <h2>定期情報を登録してください</h2>
              <p>自宅側駅、会社側駅、片道運賃、定期料金、期間を登録すると回収状況を表示できます。</p>
              <button className="secondaryButton" type="button" onClick={() => setView('settings')}>Settings を開く</button>
            </section>
          )}
        </section>
      )}

      {view === 'settings' && (
        <section className="content" id="settings">
          <header className="pageHeader">
            <div>
              <p className="eyebrow">Settings</p>
              <h1>定期情報</h1>
            </div>
          </header>

          <StatusMessage loading={loading} error={error} notice={notice} />

          <form className="settingsForm" onSubmit={handleSubmit}>
            <label>
              <span>自宅側駅</span>
              <input required maxLength={80} value={form.fromStation} onChange={(event) => updateForm('fromStation', event.target.value)} />
            </label>
            <label>
              <span>会社側駅</span>
              <input required maxLength={80} value={form.toStation} onChange={(event) => updateForm('toStation', event.target.value)} />
            </label>
            <label>
              <span>片道運賃</span>
              <input required min={1} inputMode="numeric" type="number" value={form.oneWayFare} onChange={(event) => updateForm('oneWayFare', event.target.value)} />
            </label>
            <label>
              <span>定期料金</span>
              <input required min={1} inputMode="numeric" type="number" value={form.passPrice} onChange={(event) => updateForm('passPrice', event.target.value)} />
            </label>
            <label>
              <span>開始日</span>
              <input required type="date" value={form.startDate} onChange={(event) => updateForm('startDate', event.target.value)} />
            </label>
            <label>
              <span>終了日</span>
              <input required type="date" value={form.endDate} onChange={(event) => updateForm('endDate', event.target.value)} />
            </label>
            <label className="fullWidth">
              <span>メモ</span>
              <input maxLength={200} value={form.memo} onChange={(event) => updateForm('memo', event.target.value)} />
            </label>
            <div className="formActions">
              <button className="primaryButton" type="submit" disabled={saving}>
                <Save aria-hidden="true" size={18} />
                {saving ? '保存中' : '保存'}
              </button>
            </div>
          </form>
        </section>
      )}

      {view === 'history' && (
        <section className="content" id="history">
          <header className="pageHeader">
            <div>
              <p className="eyebrow">History</p>
              <h1>出社履歴</h1>
            </div>
          </header>
          <section className="emptyState">
            <h2>履歴は次のUnitで追加します</h2>
            <p>U-04で出社日の一覧と削除操作を実装します。</p>
          </section>
        </section>
      )}
    </main>
  );

  function updateForm(key: keyof FormState, value: string) {
    setForm((current) => ({ ...current, [key]: value }));
  }
}

function StatusMessage({ loading, error, notice }: { loading: boolean; error: string; notice: string }) {
  if (loading) {
    return <p className="statusMessage">読み込み中</p>;
  }
  if (error) {
    return <p className="statusMessage error">{error}</p>;
  }
  if (notice) {
    return <p className="statusMessage success">{notice}</p>;
  }
  return null;
}

function getInitialView(): View {
  const hash = window.location.hash.replace('#', '');
  if (hash === 'settings' || hash === 'history') {
    return hash;
  }
  return 'dashboard';
}

function toFormState(commuterPass: CommuterPass): FormState {
  return {
    fromStation: commuterPass.fromStation,
    toStation: commuterPass.toStation,
    oneWayFare: String(commuterPass.oneWayFare),
    passPrice: String(commuterPass.passPrice),
    startDate: commuterPass.startDate,
    endDate: commuterPass.endDate,
    memo: commuterPass.memo ?? '',
  };
}

function formatYen(value: number) {
  return `${new Intl.NumberFormat('ja-JP').format(value)} 円`;
}

function formatApiError(data: { message?: string; errors?: string[] } | null) {
  if (!data) {
    return '保存できませんでした。入力内容を確認してください。';
  }
  if (data.errors?.length) {
    return data.errors.join(' / ');
  }
  return data.message ?? '保存できませんでした。入力内容を確認してください。';
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
