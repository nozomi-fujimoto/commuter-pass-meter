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

実装済み（B-03-02〜04）。固定ユーザーの現在有効な定期に紐づく出社記録だけを集計する。

- 定期未登録時は `404` とエラーメッセージを返す
- 出社記録0件でも集計値を返す（差額は定期料金の負数、回収率は0）
- `monthlyAttendanceCount` は実行環境のローカル日付の当月（年月一致）の出社回数
- `hasAttendedToday` は同じ日付基準で判定する
- `recentAttendanceRecords` は出社日の降順で最大5件。0件の場合は空配列
- 計算は現在の定期の片道運賃と定期料金を使う。履歴の `roundTripFare` は記録時点の値
- 回収率は小数第1位に四捨五入し、100%を超える値も返す

画面との接続はB-03-05以降で実装する。

B-03-09で、定期情報を `CommuterPassResponse`、履歴を
`RecentAttendanceRecordResponse` に分離した。既存のJSON項目・数値・日付形式は維持し、
ドメインモデルへのフィールド追加が自動的にAPIへ反映されないようにする。
計算モデルでは回数・累計額・差額・回収率を値オブジェクトで扱い、レスポンス生成時に
数値へ変換する。回収済みの判定は、丸めた回収率ではなく差額が0以上かで行う。

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
