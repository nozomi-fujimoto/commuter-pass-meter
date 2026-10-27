# AIDLC Progress

## Current Focus

- Unit: U-03 Dashboard Calculation
- Status: developing
- Active Bolt: （次: B-03-06）
- Last Updated: 2026-10-28

## Unit Progress

| Unit | Status | Current Bolt | Verification | Notes |
| --- | --- | --- | --- | --- |
| U-00 Project Foundation | done |  | `mvn -Dmaven.repo.local=/private/tmp/commuter-pass-meter-m2 test`, `npm run build`, `npm audit --audit-level=moderate` | Spring BootとReactの最小構成、Health API、初期Dashboardを追加 |
| U-01 Commuter Pass Settings | done |  | `mvn test -e`, `npm run build` | 定期情報API、Settings画面、Dashboard表示、API/ドメイン/JSON Test、Frontendコンポーネント分割を実装済み |
| U-02 Attendance Recording | verifying |  | `mvn test -e`, `npm run build` | 出社記録API、日付重複防止、定期期間外拒否、今日の記録済み状態、Dashboard出社ボタンを実装済み。受け入れ確認へ進む |
| U-03 Dashboard Calculation | developing | 次: B-03-06 | `npm run build`、`npm test`（表示7 tests成功）。Backend前回51 tests成功 | APIの画面接続完了。次は回収前後の文言切り替えとブラウザでの受け入れ確認 |
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
| B-03-01 | done | 回収計算Serviceと計算結果recordを追加 | `mvn test -e`（全38 tests成功） | UT-001〜003、損益一致、回収後、負数拒否、大きな累計金額の6 tests。API・画面接続は後続Bolt |
| B-03-02 | done | `GET /api/dashboard` と集計用Service・Responseを追加 | `mvn test -e`（全42 tests成功） | 定期未登録404、0件・回収前・回収後の集計、対象ユーザー・定期の絞り込み |
| B-03-03 | done | 今月の出社回数と今日の記録状態を集計 | `DashboardControllerTest` | 注入Clockのローカル日付・年月で判定 |
| B-03-04 | done | 最近の出社記録を出社日の降順で最大5件返す | `DashboardControllerTest` | 挿入順と異なるデータで検証 |
| B-03-08 | done | API-007を実装 | `DashboardControllerTest`（4 tests成功） | 出社APIからの集計更新を含む。FEの確認は後続Bolt |
| B-03-09 | done | AttendanceCount・NormalFareTotal・FareDifference・RecoveryRateと専用レスポンスDTOを追加 | `mvn test -e`（全51 tests成功） | 計算入力・結果は値オブジェクト、API境界で数値化。既存JSONを維持 |
| B-03-05 | done | Dashboard APIの画面接続と集計値・最近の履歴表示 | `npm run build`、`npm test`（7 tests成功） | 出社記録・定期保存後に再取得。失敗時は集計値を隠し再読み込み導線を表示。ブラウザ操作確認は未実施 |

## Decisions

| Date | Decision | Reason |
| --- | --- | --- |
| 2026-08-08 | Progress StateをUnit/Boltとは別モジュールとして管理する | Unit/Boltは作業分解、Progress Stateは進行台帳として責務を分けるため |
| 2026-08-09 | BackendはSpring Boot、FrontendはVite + React + TypeScriptで開始する | MVPのAPI/画面を小さく実装しやすく、既存資料の想定構成に合うため |
| 2026-08-12 | U-01の初期Repositoryはin-memoryで実装する | U-06で永続化を扱うため、まずAPI/Service境界と画面連携を固める |
| 2026-08-16 | ドメインモデルの各フィールドは原則としてプリミティブ型を直接持たず、recordで定義した値オブジェクトで表現する | ドメイン駆動の方針に合わせ、StationName/FareAmount/PassPeriod/PassMemoやAttendanceRecordの各値オブジェクトが不正状態を拒否するため |
| 2026-08-17 | `frontend/src/main.tsx` のコンポーネント分割はB-01-09で扱う | 現在のBoltはAPI TestとU-01の動作確認に集中し、画面構造の改善を別Boltとしてレビューしやすくするため |
| 2026-09-23 | U-02では出社記録日をAPIリクエストで任意指定できるが、画面操作ではサーバーの今日を使う | 通常利用の操作を簡潔に保ちつつ、重複・期間外のAPI Testを安定して実行するため |
| 2026-10-18 | B-03-01はRepositoryに依存しない純粋な計算Serviceとし、金額累計はlong、回収率はBigDecimalの小数第1位HALF_UPとする | UT-001〜003の期待値に合わせ、整数の桁あふれと浮動小数の丸め誤差を避けるため |

| 2026-10-25 | Dashboardは現在有効な定期・ユーザーの記録を集計し、最近の履歴は出社日の降順で最大5件返す | API契約を具体化し、過去の別定期の記録が混入することを避けるため。金額計算は既存のACの式に従い現在の定期運賃を使用する |

| 2026-10-25 | 計算の入力・結果を値オブジェクトで表し、DashboardのAPI専用DTOで数値・日付に変換する | 業務制約を型で保護し、ドメインモデルとAPI契約の変更を分離するため。既存JSON項目は維持する |

## Blockers

| Date | Scope | Blocker | Needed Action |
| --- | --- | --- | --- |

## Learn Log

| Date | Unit | Observation | Next Action |
| --- | --- | --- | --- |
| 2026-10-18 | U-02 / U-03 | 既存32 testsとFrontend build成功。AC-003の累計額更新はDashboard連携に依存し、画面操作も未確認のためU-02はverifyingを維持。B-03-01の計算例と境界値は成功 | B-03-02でDashboard APIを実装し、画面連携後にFE-003/004を確認する |

| 2026-10-25 | U-03 | API-007と全42 tests成功。出社APIから累計額・今日の記録状態への反映を確認 | B-03-05でDashboard画面にAPIを接続し、B-03-06で回収前後の文言を切り替える。U-02のFE-003/004もそこで確認 |

| 2026-10-25 | U-03 | B-03-05の本番ビルドと表示7 tests成功。Dashboard APIに今日の記録状態も統一し、記録成功後の再取得失敗でも記録済み表示を保持 | B-03-06で回収前後の文言を追加し、ブラウザで定期保存・出社・再読み込み・エラー復旧を確認。U-02はverifyingを維持 |

| 2026-10-28 | U-01 / U-03 | ユーザーより画面との受け入れ項目はOKとの確認。追加指摘の日付入力を年4桁・数字8桁入力後の自動整形に修正し、Frontend全11 testsと本番ビルド成功 | 新しい日付入力のブラウザ確認。B-03-06の回収前後の文言切り替えは継続 |
