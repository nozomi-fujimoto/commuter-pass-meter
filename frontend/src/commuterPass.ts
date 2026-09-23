import { CommuterPass, FormState } from './types';

export function getInitialView() {
  const hash = window.location.hash.replace('#', '');
  if (hash === 'settings' || hash === 'history') {
    return hash;
  }
  return 'dashboard';
}

export function toFormState(commuterPass: CommuterPass): FormState {
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

export function formatYen(value: number) {
  return `${new Intl.NumberFormat('ja-JP').format(value)} 円`;
}

export function formatApiError(data: { message?: string; errors?: string[] } | null) {
  if (!data) {
    return '保存できませんでした。入力内容を確認してください。';
  }
  if (data.errors?.length) {
    return data.errors.join(' / ');
  }
  return data.message ?? '保存できませんでした。入力内容を確認してください。';
}
