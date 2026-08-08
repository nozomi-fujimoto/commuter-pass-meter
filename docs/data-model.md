# データモデル v0.1

## User

MVPでは固定ユーザーで運用する。将来の複数ユーザー化に備えてモデルとして残す。

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| id | Long | Yes | 主キー |
| displayName | String | Yes | 表示名 |
| createdAt | LocalDateTime | Yes | 作成日時 |

## CommuterPass

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| id | Long | Yes | 主キー |
| userId | Long | Yes | ユーザーID |
| fromStation | String | Yes | 自宅側駅 |
| toStation | String | Yes | 会社側駅 |
| oneWayFare | Integer | Yes | 片道運賃 |
| passPrice | Integer | Yes | 定期料金 |
| startDate | LocalDate | Yes | 定期開始日 |
| endDate | LocalDate | Yes | 定期終了日 |
| memo | String | No | メモ |
| active | Boolean | Yes | 有効フラグ |
| createdAt | LocalDateTime | Yes | 作成日時 |
| updatedAt | LocalDateTime | Yes | 更新日時 |

## AttendanceRecord

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| id | Long | Yes | 主キー |
| userId | Long | Yes | ユーザーID |
| commuterPassId | Long | Yes | 定期ID |
| attendanceDate | LocalDate | Yes | 出社日 |
| createdAt | LocalDateTime | Yes | 作成日時 |

## Constraints

- `AttendanceRecord` は `userId`, `commuterPassId`, `attendanceDate` の組み合わせで一意
- `CommuterPass.oneWayFare` は1以上
- `CommuterPass.passPrice` は1以上
- `CommuterPass.startDate` は `endDate` 以前
- 出社日は定期期間内のみ登録可能

## MVP Persistence

最初の実装では以下のどちらかを選ぶ。

- H2: ローカルで素早く動かす
- PostgreSQL: 本番運用を早めに意識する

個人開発の初速を優先するならH2から開始し、APIとサービス層を固めてからPostgreSQLへ移行する。
