# テスト計画 v0.1

## 1. Purpose

MVP の要件、API、計算ロジック、画面操作が受け入れ条件を満たすことを確認する。

## 2. Test Levels

### Unit Test

主にサービス層と計算ロジックを対象にする。

- 往復運賃の計算
- 通常運賃換算の累計額
- 定期料金との差分
- 回収率
- 損益分岐の出社回数
- 残り出社回数
- 定期期間内外の判定
- 入力値バリデーション

### API Test

Spring Boot REST API の正常系と異常系を対象にする。

- `GET /api/commuter-pass`
- `POST /api/commuter-pass`
- `PUT /api/commuter-pass/{id}`
- `GET /api/attendance-records`
- `POST /api/attendance-records`
- `DELETE /api/attendance-records/{id}`
- `GET /api/dashboard`

### Frontend Test

React 画面の表示状態と主要操作を対象にする。

- Dashboard の初期表示
- 出社ボタン押下
- 今日記録済み状態
- 定期未登録状態
- Settings の登録・編集
- History の一覧表示・削除

### Manual Learn Test

AIDLC の Learn フェーズとして、実利用時の体験を確認する。

- 出社日にすぐ記録できるか
- 数字の意味が直感的に分かるか
- 元が取れる前後の表示が嬉しいか
- 記録を続ける負担が小さいか
- 履歴を見返す意味があるか

## 3. Unit Test Cases

### UT-001: 回収前の集計を計算できる

Input:

```text
oneWayFare = 310
passPrice = 14200
attendanceCount = 12
```

Expected:

```text
roundTripFare = 620
normalFareTotal = 7440
difference = -6760
recoveryRate = 52.4
breakEvenAttendanceCount = 23
remainingAttendanceCount = 11
```

### UT-002: 回収済みの集計を計算できる

Input:

```text
oneWayFare = 310
passPrice = 14200
attendanceCount = 23
```

Expected:

```text
roundTripFare = 620
normalFareTotal = 14260
difference = 60
recoveryRate = 100.4
breakEvenAttendanceCount = 23
remainingAttendanceCount = 0
```

### UT-003: 出社回数0件の集計を計算できる

Input:

```text
oneWayFare = 310
passPrice = 14200
attendanceCount = 0
```

Expected:

```text
normalFareTotal = 0
difference = -14200
recoveryRate = 0
remainingAttendanceCount = 23
```

### UT-004: 定期開始日と終了日は記録可能日として扱う

Input:

```text
startDate = 2026-08-01
endDate = 2026-08-31
attendanceDate = 2026-08-01 or 2026-08-31
```

Expected:

```text
valid = true
```

### UT-005: 定期期間外の日付は記録不可として扱う

Input:

```text
startDate = 2026-08-01
endDate = 2026-08-31
attendanceDate = 2026-07-31 or 2026-09-01
```

Expected:

```text
valid = false
```

## 4. API Test Cases

### API-001: 定期情報を作成できる

Request:

```http
POST /api/commuter-pass
```

Expected:

```text
status = 200 or 201
created commuter pass is returned
```

### API-002: 不正な定期情報は作成できない

Input examples:

```text
empty fromStation
oneWayFare = 0
passPrice = 0
startDate > endDate
```

Expected:

```text
status = 400
```

### API-003: 出社記録を作成できる

Request:

```http
POST /api/attendance-records
```

Expected:

```text
status = 200 or 201
attendance record is returned
dashboard totals are updated
```

### API-004: 同じ日の出社記録は作成できない

Precondition:

```text
same userId, commuterPassId, attendanceDate already exists
```

Expected:

```text
status = 409
```

### API-005: 定期期間外の出社記録は作成できない

Expected:

```text
status = 400
```

### API-006: 出社履歴を削除できる

Request:

```http
DELETE /api/attendance-records/{id}
```

Expected:

```text
status = 200 or 204
record is removed
dashboard totals are recalculated
```

### API-007: Dashboard 集計を取得できる

Request:

```http
GET /api/dashboard
```

Expected:

```text
status = 200
response contains commuterPass, attendanceCount, normalFareTotal, difference, recoveryRate, remainingAttendanceCount, hasAttendedToday, recentAttendanceRecords
```

## 5. Frontend Manual Test Cases

### FE-001: 定期未登録時の Dashboard

Expected:

```text
calculation cards are not shown
settings navigation is shown
```

### FE-002: 定期登録後の Dashboard

Expected:

```text
commuter pass route, pass price, recovery rate, difference, remaining attendance count are shown
```

### FE-003: 出社ボタン押下

Expected:

```text
attendance record is created
button changes to recorded state
dashboard totals are updated
positive short message is shown
```

### FE-004: 今日記録済み状態

Expected:

```text
attendance button is disabled
recorded label is shown
```

### FE-005: History から記録を削除

Expected:

```text
record disappears from history
dashboard totals decrease
if deleted record is today, dashboard attendance button becomes available again
```

## 6. Regression Checklist

リリース前または大きな変更後に以下を確認する。

- 定期情報を登録できる
- 不正な定期情報を登録できない
- 出社記録を作成できる
- 同じ日付で重複登録できない
- 定期期間外の日付を登録できない
- 出社履歴を削除できる
- Dashboard の計算値が期待通りである
- 今日記録済み状態が正しく表示される
- 定期未登録状態が正しく表示される

## 7. Learn Log Template

実利用後に以下を記録する。

```text
date:
used_on_commute_day: yes/no
time_to_record:
felt_positive: yes/no
confusing_point:
wanted_change:
next_action:
```
