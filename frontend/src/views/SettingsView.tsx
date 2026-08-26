import { FormEvent } from 'react';
import { Save } from 'lucide-react';
import { FormState } from '../types';
import { StatusMessage } from '../components/StatusMessage';

type SettingsViewProps = {
  error: string;
  form: FormState;
  loading: boolean;
  notice: string;
  saving: boolean;
  onChangeForm: (key: keyof FormState, value: string) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
};

export function SettingsView({ error, form, loading, notice, saving, onChangeForm, onSubmit }: SettingsViewProps) {
  return (
    <section className="content" id="settings">
      <header className="pageHeader">
        <div>
          <p className="eyebrow">Settings</p>
          <h1>定期情報</h1>
        </div>
      </header>

      <StatusMessage loading={loading} error={error} notice={notice} />

      <form className="settingsForm" onSubmit={onSubmit}>
        <label>
          <span>自宅側駅</span>
          <input required maxLength={80} value={form.fromStation} onChange={(event) => onChangeForm('fromStation', event.target.value)} />
        </label>
        <label>
          <span>会社側駅</span>
          <input required maxLength={80} value={form.toStation} onChange={(event) => onChangeForm('toStation', event.target.value)} />
        </label>
        <label>
          <span>片道運賃</span>
          <input required min={1} inputMode="numeric" type="number" value={form.oneWayFare} onChange={(event) => onChangeForm('oneWayFare', event.target.value)} />
        </label>
        <label>
          <span>定期料金</span>
          <input required min={1} inputMode="numeric" type="number" value={form.passPrice} onChange={(event) => onChangeForm('passPrice', event.target.value)} />
        </label>
        <label>
          <span>開始日</span>
          <input required type="date" value={form.startDate} onChange={(event) => onChangeForm('startDate', event.target.value)} />
        </label>
        <label>
          <span>終了日</span>
          <input required type="date" value={form.endDate} onChange={(event) => onChangeForm('endDate', event.target.value)} />
        </label>
        <label className="fullWidth">
          <span>メモ</span>
          <input maxLength={200} value={form.memo} onChange={(event) => onChangeForm('memo', event.target.value)} />
        </label>
        <div className="formActions">
          <button className="primaryButton" type="submit" disabled={saving}>
            <Save aria-hidden="true" size={18} />
            {saving ? '保存中' : '保存'}
          </button>
        </div>
      </form>
    </section>
  );
}
