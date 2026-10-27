import assert from 'node:assert/strict';
import { after, test } from 'node:test';
import { createElement } from 'react';
import { renderToStaticMarkup } from 'react-dom/server';
import { createServer } from 'vite';

const server = await createServer({ server: { middlewareMode: true, hmr: false, watch: null }, appType: 'custom' });
after(() => server.close());
const { DashboardView } = await server.ssrLoadModule('/src/views/DashboardView.tsx');
const { formatDateInput, toApiDate } = await server.ssrLoadModule('/src/dateInput.ts');
const { toFormState } = await server.ssrLoadModule('/src/commuterPass.ts');
const { SettingsView } = await server.ssrLoadModule('/src/views/SettingsView.tsx');
const pass = { id: 1, userId: 1, fromStation: '横浜', toStation: '品川', oneWayFare: 310,
  passPrice: 14200, startDate: '2026-10-01', endDate: '2026-10-31', memo: null, active: true };
const dashboard = { commuterPass: pass, attendanceCount: 12, monthlyAttendanceCount: 6,
  normalFareTotal: 7440, difference: -6760, recoveryRate: 52.4, breakEvenAttendanceCount: 23,
  remainingAttendanceCount: 11, hasAttendedToday: false,
  recentAttendanceRecords: [{ id: 1, attendanceDate: '2026-10-25', roundTripFare: 620 }] };
function render(overrides = {}) {
  return renderToStaticMarkup(createElement(DashboardView, {
    commuterPass: pass, dashboard, attendanceStatusKnown: true, attendanceError: '', error: '',
    loading: false, notice: '', recording: false, recordedToday: false,
    onReload() {}, onRecordAttendance() {}, onOpenSettings() {}, ...overrides,
  }));
}

test('displays API totals and recent attendance instead of placeholder values', () => {
  const html = render();
  for (const value of ['52.4%', '11 回', '12 回', '6 回', '7,440 円', '-6,760 円', '23 回', '2026-10-25', '620 円']) {
    assert.ok(html.includes(value), value);
  }
  assert.doesNotMatch(html, /未記録/);
});

test('displays recovery above 100 percent and zero remaining visits', () => {
  const html = render({ dashboard: { ...dashboard, recoveryRate: 100.4, difference: 60, remainingAttendanceCount: 0 } });
  assert.match(html, /100\.4%/);
  assert.match(html, /\+60 円/);
  assert.match(html, /0 回/);
});

test('disables the attendance button when today has been recorded', () => {
  const html = render({ recordedToday: true });
  assert.match(html, /disabled="">記録済み/);
  assert.match(html, /今日は記録済みです/);
});

test('shows settings guidance without calculations when no pass exists', () => {
  const html = render({ commuterPass: null, dashboard: null, attendanceStatusKnown: false });
  assert.match(html, /Settings を開く/);
  assert.doesNotMatch(html, /Recovery summary/);
  assert.match(html, /disabled="">出社/);
});

test('distinguishes a failed initial load from an unregistered pass', () => {
  const html = render({ commuterPass: null, dashboard: null, attendanceStatusKnown: false, error: '回収状況を読み込めませんでした。' });
  assert.match(html, /再読み込み/);
  assert.doesNotMatch(html, /Settings を開く|Recovery summary/);
  assert.match(html, /disabled="">出社/);
});

test('hides stale totals and keeps recorded state when refresh fails after attendance', () => {
  const html = render({ dashboard: null, recordedToday: true, attendanceStatusKnown: false, error: '読み込み失敗' });
  assert.match(html, /再読み込み/);
  assert.match(html, /disabled="">記録済み/);
  assert.doesNotMatch(html, /Recovery summary|7,440 円/);
});

test('shows an empty history message for zero attendance', () => {
  const html = render({ dashboard: { ...dashboard, attendanceCount: 0, monthlyAttendanceCount: 0,
    normalFareTotal: 0, difference: -14200, recoveryRate: 0, remainingAttendanceCount: 23, recentAttendanceRecords: [] } });
  assert.match(html, /出社記録はまだありません/);
  assert.match(html, /0%/);
  assert.match(html, /-14,200 円/);
});


test('formats continuous input and pasted dates with at most eight digits', () => {
  assert.equal(formatDateInput('20260410'), '2026/04/10');
  assert.equal(formatDateInput('2026/04/10'), '2026/04/10');
  assert.equal(formatDateInput('2026-04-10'), '2026/04/10');
  assert.equal(formatDateInput('2026041099'), '2026/04/10');
  assert.equal(formatDateInput(''), '');
  assert.equal(formatDateInput('2026041'), '2026/04/1');
  assert.equal(formatDateInput('2026'), '2026/');
  assert.equal(formatDateInput('20260'), '2026/0');
  assert.equal(formatDateInput('202604'), '2026/04/');
  let input = '';
  for (const digit of '20260410') {
    input = formatDateInput(input + digit);
  }
  assert.equal(input, '2026/04/10');
  assert.equal(formatDateInput(input.slice(0, -1)), '2026/04/1');
  assert.equal(formatDateInput('2026041' + '1'), '2026/04/11');
});

test('allows backspace past automatically inserted date separators', () => {
  assert.equal(formatDateInput('2026', '2026/'), '202');
  assert.equal(formatDateInput('2026/04', '2026/04/'), '2026/0');
  let input = '2026/04/10';
  const expected = ['2026/04/1', '2026/04/', '2026/0', '2026/', '202', '20', '2', ''];
  for (const value of expected) {
    input = formatDateInput(input.slice(0, -1), input);
    assert.equal(input, value);
  }
});

test('converts real four-digit-year dates to the API format and validates leap years', () => {
  for (const date of ['2026/04/10', '2024/02/29', '2000/02/29', '0001/01/01', '9999/12/31']) {
    assert.equal(toApiDate(date), date.replaceAll('/', '-'));
  }
  for (const date of ['', '20260410', '2026/04/1', '2026/02/29', '1900/02/29', '2026/04/31',
    '2026/00/10', '2026/13/10', '2026/04/00', '0000/01/01', '202604/01/01']) {
    assert.throws(() => toApiDate(date), Error, date);
  }
});

test('loads saved API dates into the new input format without changing payload dates', () => {
  const form = toFormState(pass);
  assert.equal(form.startDate, '2026/10/01');
  assert.equal(form.endDate, '2026/10/31');
  assert.equal(toApiDate(form.startDate), pass.startDate);
  assert.equal(toApiDate(form.endDate), pass.endDate);
});

test('both settings date fields use numeric text input with four-digit year validation', () => {
  const html = renderToStaticMarkup(createElement(SettingsView, {
    form: toFormState(pass), error: '', loading: false, notice: '', saving: false,
    onChangeForm() {}, onSubmit() {},
  }));
  assert.equal((html.match(/pattern="\[0-9\]\{4\}\/\[0-9\]\{2\}\/\[0-9\]\{2\}"/g) ?? []).length, 2);
  assert.equal((html.match(/maxLength="10"/g) ?? []).length, 2);
  assert.match(html, /value="2026\/10\/01"/);
  assert.match(html, /value="2026\/10\/31"/);
});
