export type View = 'dashboard' | 'history' | 'settings';

export type CommuterPass = {
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

export type FormState = {
  fromStation: string;
  toStation: string;
  oneWayFare: string;
  passPrice: string;
  startDate: string;
  endDate: string;
  memo: string;
};

export const emptyForm: FormState = {
  fromStation: '',
  toStation: '',
  oneWayFare: '',
  passPrice: '',
  startDate: '',
  endDate: '',
  memo: '',
};
