# AIDLC Progress

## Current Focus

- Unit: U-02 Attendance Recording
- Status: verifying
- Active Bolt:
- Last Updated: 2026-09-23

## Unit Progress

| Unit | Status | Current Bolt | Verification | Notes |
| --- | --- | --- | --- | --- |
| U-00 Project Foundation | done |  | `mvn -Dmaven.repo.local=/private/tmp/commuter-pass-meter-m2 test`, `npm run build`, `npm audit --audit-level=moderate` | Spring BootとReactの最小構成、Health API、初期Dashboardを追加 |
| U-01 Commuter Pass Settings | done |  | `mvn test -e`, `npm run build` | 定期情報API、Settings画面、Dashboard表示、API/ドメイン/JSON Test、Frontendコンポーネント分割を実装済み |
| U-02 Attendance Recording | verifying |  | `mvn test -e`, `npm run build` | 出社記録API、日付重複防止、定期期間外拒否、今日の記録済み状態、Dashboard出社ボタンを実装済み。受け入れ確認へ進む |
| U-03 Dashboard Calculation | todo |  |  | 回収状況の集計と表示 |
| U-04 Attendance History | todo |  |  | 履歴確認と削除 |
| U-05 MVP Experience Polish | todo |  |  | 実利用に向けた体験調整 |
| U-06 Local Persistence Upgrade | todo |  |  | ローカル永続化 |

## Bolt Progress

| Bolt | Status | Result | Verification | Notes |
| --- | --- | --- | --- | --- |
| B-00-01 | done | Spring Bootプロジェクトを `backend/` に作成 | `mvn -Dmaven.repo.local=/private/tmp/commuter-pass-meter-m2 test` | Web/Validation/Test starter |
| B-00-02 | done | Reactプロジェクトを `frontend/` に作成 | `npm run build` | Vite + React + TypeScript |
| B-00-03 | done | `GET /api/health` を追加 | `mvn -Dmaven.repo.local=/private/tmp/commuter-pass-meter-m2 test` | Foundation確認用API |
| B-00-04 | done | Frontendの初期Dashboard画面を追加 | `npm run build` | 定期未登録状態の入口 |
| B-00-05 | done | READMEに起動手順と検証コマンドを追記 | Manual | Backend/Frontend手順 |
| B-01-01 | done | `CommuterPass` modelと値オブジェクトを追加 | `mvn -Dtest=CommuterPassValueObjectTest,CommuterPassJsonTest test` | 固定ユーザー前提。StationName/FareAmount/PassPeriod/PassMemoで不変条件を保持 |
| B-01-02 | done | in-memory `CommuterPassRepository` を追加 | `mvn -q -DskipTests compile` | 有効な定期情報は同一ユーザー1件に制限 |
| B-01-03 | done | 作成・取得・更新Serviceを追加 | `mvn -q -DskipTests compile` | Requestを値オブジェクトへ変換してから保存 |
| B-01-04 | done | `GET/POST/PUT /api/commuter-pass` を追加 | `mvn -q -DskipTests compile` | PUTはAPI設計に合わせて `/api/commuter-pass/{id}` |
| B-01-05 | done | 入力値バリデーションと400/404応答を追加 | `mvn -Dtest=CommuterPassValueObjectTest,CommuterPassJsonTest test` | DTOではなく値オブジェクトでドメイン制約を検証 |
| B-01-06 | done | Settings画面のフォームを追加 | `npm run build` | 作成済みの場合は更新として保存 |
| B-01-07 | done | Dashboardの定期未登録/登録済み表示を追加 | `npm run build` | 未登録時はSettings導線、登録済みは区間・料金・期間を表示 |
| B-01-08 | done | API Testとドメイン/JSON Testを追加し、Frontend buildも確認済み | `mvn test -e`, `npm run build` | Spring Boot API Testを含む全Backend Testが完走。`frontend/src/main.tsx` の構造改善はこのBoltから切り離す |
| B-01-09 | done | Frontendのタブ表示を画面コンポーネントへ分割 | `npm run build` | `main.tsx` をReactエントリーポイントに寄せ、`App`、`SideNav`、`DashboardView`、`SettingsView`、`HistoryView` などへ責務を分けた |
| B-02-01 | done | `AttendanceRecord` modelを追加 | `mvn -Dtest=AttendanceRecordControllerTest,InMemoryAttendanceRecordRepositoryTest,CommuterPassValueObjectTest test -e` | 固定ユーザー前提。出社日、定期ID、往復運賃、作成日時を保持 |
| B-02-02 | done | 日付重複を防ぐin-memory Repository制約を追加 | `mvn -Dtest=InMemoryAttendanceRecordRepositoryTest test -e` | 同一ユーザー・同一日付の重複を拒否 |
| B-02-03 | done | 定期期間内外の判定をServiceへ追加 | `mvn -Dtest=AttendanceRecordControllerTest,CommuterPassValueObjectTest test -e` | `PassPeriod.contains` で開始日・終了日を含む範囲判定 |
| B-02-04 | done | `POST /api/attendance-records` を追加 | `mvn -Dtest=AttendanceRecordControllerTest test -e` | 日付未指定時はサーバーの今日で記録 |
| B-02-05 | done | 重複時の409応答を追加 | `mvn -Dtest=AttendanceRecordControllerTest test -e` | `DuplicateAttendanceRecordException` を409へ変換 |
| B-02-06 | done | 期間外登録時の400応答を追加 | `mvn -Dtest=AttendanceRecordControllerTest test -e` | `InvalidAttendanceRecordException` を400へ変換 |
| B-02-07 | done | Dashboardの出社ボタンと記録済み状態を追加 | `npm run build` | `GET /api/attendance-records/today` で今日の状態を読み込み、記録済み時はボタンを無効化 |
| B-02-08 | done | Unit TestとAPI Testを追加 | `mvn test -e`, `npm run build` | Backend 19 tests成功。Frontend production build成功 |

## Decisions

| Date | Decision | Reason |
| --- | --- | --- |
| 2026-08-08 | Progress StateをUnit/Boltとは別モジュールとして管理する | Unit/Boltは作業分解、Progress Stateは進行台帳として責務を分けるため |
| 2026-08-09 | BackendはSpring Boot、FrontendはVite + React + TypeScriptで開始する | MVPのAPI/画面を小さく実装しやすく、既存資料の想定構成に合うため |
| 2026-08-12 | U-01の初期Repositoryはin-memoryで実装する | U-06で永続化を扱うため、まずAPI/Service境界と画面連携を固める |
| 2026-08-16 | ドメインモデルの各フィールドは原則としてプリミティブ型を直接持たず、recordで定義した値オブジェクトで表現する | ドメイン駆動の方針に合わせ、StationName/FareAmount/PassPeriod/PassMemoやAttendanceRecordの各値オブジェクトが不正状態を拒否するため |
| 2026-08-17 | `frontend/src/main.tsx` のコンポーネント分割はB-01-09で扱う | 現在のBoltはAPI TestとU-01の動作確認に集中し、画面構造の改善を別Boltとしてレビューしやすくするため |
| 2026-09-23 | U-02では出社記録日をAPIリクエストで任意指定できるが、画面操作ではサーバーの今日を使う | 通常利用の操作を簡潔に保ちつつ、重複・期間外のAPI Testを安定して実行するため |

## Blockers

| Date | Scope | Blocker | Needed Action |
| --- | --- | --- | --- |

## Learn Log

| Date | Unit | Observation | Next Action |
| --- | --- | --- | --- |
