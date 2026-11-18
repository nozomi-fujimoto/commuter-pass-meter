import { formatDateInput, toApiDate } from '../dateInput';

type DateInputProps = {
  value: string;
  onChange: (value: string) => void;
};

export function DateInput({ value, onChange }: DateInputProps) {
  return (
    <input
      required
      type="text"
      inputMode="numeric"
      maxLength={10}
      pattern="[0-9]{4}/[0-9]{2}/[0-9]{2}"
      placeholder="2026/04/10"
      title="年4桁・月2桁・日2桁で入力してください（例：20260410）"
      value={value}
      onChange={(event) => {
        const formatted = formatDateInput(event.target.value, value);
        let message = '';
        if (formatted) {
          try {
            toApiDate(formatted);
          } catch (caught) {
            message = caught instanceof Error ? caught.message : '日付を確認してください。';
          }
        }
        event.target.setCustomValidity(message);
        onChange(formatted);
      }}
    />
  );
}
