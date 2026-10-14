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

export type AttendanceRecord = {
  id: number;
  userId: number;
  commuterPassId: number;
  attendanceDate: string;
  roundTripFare: number;
  createdAt: string;
};

export type TodayAttendance = {
  recorded: boolean;
  record: AttendanceRecord | null;
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
