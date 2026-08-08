# API設計 v0.1

## Base Path

```text
/api
```

## 1. Commuter Pass

### GET /api/commuter-pass

現在有効な定期情報を取得する。

#### Response

```json
{
  "id": 1,
  "userId": 1,
  "fromStation": "横浜",
  "toStation": "品川",
  "oneWayFare": 310,
  "passPrice": 14200,
  "startDate": "2026-08-01",
  "endDate": "2026-08-31",
  "active": true
}
```

### POST /api/commuter-pass

定期情報を作成する。

#### Request

```json
{
  "fromStation": "横浜",
  "toStation": "品川",
  "oneWayFare": 310,
  "passPrice": 14200,
  "startDate": "2026-08-01",
  "endDate": "2026-08-31",
  "memo": "JR定期"
}
```

### PUT /api/commuter-pass/{id}

定期情報を更新する。

## 2. Attendance Records

### GET /api/attendance-records

出社履歴を取得する。

#### Query

- `from`: 任意。開始日
- `to`: 任意。終了日

#### Response

```json
[
  {
    "id": 10,
    "commuterPassId": 1,
    "attendanceDate": "2026-08-06",
    "roundTripFare": 620,
    "createdAt": "2026-08-06T08:52:00"
  }
]
```

### POST /api/attendance-records

今日の出社を記録する。

#### Request

```json
{
  "attendanceDate": "2026-08-06"
}
```

#### Notes

- MVPでは日付指定を許可する
- フロントの出社ボタンは今日の日付を送る
- 同一ユーザー、同一定期、同一日付の重複登録は禁止

### DELETE /api/attendance-records/{id}

出社記録を削除する。

## 3. Dashboard

### GET /api/dashboard

ダッシュボード表示用の集計済み情報を返す。

#### Response

```json
{
  "commuterPass": {
    "id": 1,
    "fromStation": "横浜",
    "toStation": "品川",
    "oneWayFare": 310,
    "passPrice": 14200,
    "startDate": "2026-08-01",
    "endDate": "2026-08-31"
  },
  "attendanceCount": 12,
  "monthlyAttendanceCount": 8,
  "normalFareTotal": 7440,
  "difference": -6760,
  "recoveryRate": 52.4,
  "breakEvenAttendanceCount": 23,
  "remainingAttendanceCount": 11,
  "hasAttendedToday": false,
  "recentAttendanceRecords": [
    {
      "id": 10,
      "attendanceDate": "2026-08-06",
      "roundTripFare": 620
    }
  ]
}
```

## 4. Error Policy

### 400 Bad Request

- 入力値が不正
- 定期期間外の日付を記録しようとした

### 404 Not Found

- 指定IDのデータが存在しない

### 409 Conflict

- 同じ日にすでに出社記録がある

## 5. Calculation

```text
roundTripFare = oneWayFare * 2
normalFareTotal = attendanceCount * roundTripFare
difference = normalFareTotal - passPrice
recoveryRate = normalFareTotal / passPrice * 100
breakEvenAttendanceCount = ceil(passPrice / roundTripFare)
remainingAttendanceCount = max(breakEvenAttendanceCount - attendanceCount, 0)
```
