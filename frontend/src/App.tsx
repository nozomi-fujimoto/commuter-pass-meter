import { FormEvent, useEffect, useMemo, useState } from 'react';
import { formatApiError, getInitialView, toFormState } from './commuterPass';
import { SideNav } from './components/SideNav';
import { DashboardView } from './views/DashboardView';
import { HistoryView } from './views/HistoryView';
import { SettingsView } from './views/SettingsView';
import { CommuterPass, emptyForm, FormState, View } from './types';

export function App() {
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

  function updateForm(key: keyof FormState, value: string) {
    setForm((current) => ({ ...current, [key]: value }));
  }

  return (
    <main className="appShell">
      <SideNav view={view} onChangeView={setView} />
      {view === 'dashboard' && (
        <DashboardView
          commuterPass={commuterPass}
          error={error}
          loading={loading}
          notice={notice}
          roundTripFare={roundTripFare}
          onOpenSettings={() => setView('settings')}
        />
      )}
      {view === 'settings' && (
        <SettingsView
          error={error}
          form={form}
          loading={loading}
          notice={notice}
          saving={saving}
          onChangeForm={updateForm}
          onSubmit={handleSubmit}
        />
      )}
      {view === 'history' && <HistoryView />}
    </main>
  );
}
