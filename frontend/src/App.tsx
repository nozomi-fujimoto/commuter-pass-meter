import { toApiDate } from './dateInput';
import { FormEvent, useEffect, useState } from 'react';
import { formatApiError, getInitialView, toFormState } from './commuterPass';
import { SideNav } from './components/SideNav';
import { DashboardView } from './views/DashboardView';
import { HistoryView } from './views/HistoryView';
import { SettingsView } from './views/SettingsView';
import { CommuterPass, emptyForm, Dashboard, FormState, View } from './types';

export function App() {
  const [view, setView] = useState<View>(getInitialView);
  const [commuterPass, setCommuterPass] = useState<CommuterPass | null>(null);
  const [form, setForm] = useState<FormState>(emptyForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [recording, setRecording] = useState(false);
  const [dashboard, setDashboard] = useState<Dashboard | null>(null);
  const [recordedToday, setRecordedToday] = useState(false);
  const [attendanceStatusKnown, setAttendanceStatusKnown] = useState(false);
  const [attendanceError, setAttendanceError] = useState('');
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    void loadDashboard();
  }, []);

  useEffect(() => {
    window.history.replaceState(null, '', `#${view}`);
  }, [view]);

  async function loadDashboard() {
    setLoading(true);
    setError('');
    setDashboard(null);
    setAttendanceStatusKnown(false);
    try {
      const response = await fetch('/api/dashboard');
      if (response.status === 404) {
        setCommuterPass(null);
        setForm(emptyForm);
        setRecordedToday(false);
        return;
      }
      if (!response.ok) {
        throw new Error('回収状況を読み込めませんでした。再読み込みしてください。');
      }
      const data = await response.json() as Dashboard;
      setDashboard(data);
      setCommuterPass(data.commuterPass);
      setForm(toFormState(data.commuterPass));
      setRecordedToday(data.hasAttendedToday);
      setAttendanceStatusKnown(true);
    } catch (caught) {
      setError(caught instanceof Error ? caught.message : '回収状況を読み込めませんでした。再読み込みしてください。');
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
        startDate: toApiDate(form.startDate),
        endDate: toApiDate(form.endDate),
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
      await loadDashboard();
      setNotice('定期情報を保存しました。');
      setView('dashboard');
    } catch (caught) {
      setError(caught instanceof Error ? caught.message : '保存できませんでした。入力内容を確認してください。');
    } finally {
      setSaving(false);
    }
  }

  async function handleRecordAttendance() {
    if (!commuterPass || !attendanceStatusKnown || recordedToday || recording || loading) {
      return;
    }
    setRecording(true);
    setNotice('');
    setAttendanceError('');

    try {
      const response = await fetch('/api/attendance-records', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({}),
      });
      if (!response.ok) {
        const data = await response.json().catch(() => null) as { message?: string; errors?: string[] } | null;
        if (response.status === 409) {
          await loadDashboard();
        }
        throw new Error(formatApiError(data));
      }
      setRecordedToday(true);
      await loadDashboard();
      setNotice('今日の出社を記録しました。');
    } catch (caught) {
      setAttendanceError(caught instanceof Error ? caught.message : '出社を記録できませんでした。');
    } finally {
      setRecording(false);
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
          attendanceStatusKnown={attendanceStatusKnown}
          attendanceError={attendanceError}
          error={error}
          loading={loading}
          notice={notice}
          recording={recording}
          dashboard={dashboard}
          recordedToday={recordedToday}
          onReload={() => void loadDashboard()}
          onRecordAttendance={handleRecordAttendance}
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
