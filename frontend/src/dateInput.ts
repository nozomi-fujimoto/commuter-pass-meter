/** 年4桁・月2桁の入力時点で区切りを追加します。 */
export function formatDateInput(value: string, previousValue = ''): string {
  // 自動追加した末尾のスラッシュを削除した場合は、直前の数字も削除できるようにする。
  const deletingSeparator = previousValue.endsWith('/') && value === previousValue.slice(0, -1);
  const input = deletingSeparator ? value.slice(0, -1) : value;
  const digits = input.replace(/[^0-9]/g, '').slice(0, 8);
  if (digits.length < 4) {
    return digits;
  }
  const year = digits.slice(0, 4);
  if (digits.length < 6) {
    return `${year}/${digits.slice(4)}`;
  }
  return `${year}/${digits.slice(4, 6)}/${digits.slice(6)}`;
}

/** 存在する西暦0001〜9999年の日付だけをAPI用のISO形式へ変換します。 */
export function toApiDate(value: string): string {
  if (!/^[0-9]{4}\/[0-9]{2}\/[0-9]{2}$/.test(value)) {
    throw new Error('日付は年4桁・月2桁・日2桁で入力してください（例：20260410）。');
  }
  const [year, month, day] = value.split('/').map(Number);
  const leapYear = year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0);
  const daysInMonth = [31, leapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  if (year < 1 || month < 1 || month > 12 || day < 1 || day > daysInMonth[month - 1]) {
    throw new Error('存在する日付を入力してください。');
  }
  return value.replace(/\//g, '-');
}
