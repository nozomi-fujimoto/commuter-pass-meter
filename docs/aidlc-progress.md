# AIDLC Progress

## Current Focus

- Unit: U-01 Commuter Pass Settings
- Status: developing
- Active Bolt: B-01-09
- Last Updated: 2026-08-25

## Unit Progress

| Unit | Status | Current Bolt | Verification | Notes |
| --- | --- | --- | --- | --- |
| U-00 Project Foundation | done |  | `mvn -Dmaven.repo.local=/private/tmp/commuter-pass-meter-m2 test`, `npm run build`, `npm audit --audit-level=moderate` | Spring BootとReactの最小構成、Health API、初期Dashboardを追加 |
| U-01 Commuter Pass Settings | developing | B-01-09 | `mvn test -e`, `npm run build` | 定期情報API、Settings画面、Dashboard表示、API/ドメイン/JSON Testを実装済み。Frontendのコンポーネント分割はB-01-09で対応 |
| U-02 Attendance Recording | todo |  |  | 今日の出社記録 |
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
| B-01-09 | todo | Frontendのタブ表示を画面コンポーネントへ分割する |  | `main.tsx` はReactエントリーポイントに寄せ、`App`、`SideNav`、`DashboardView`、`SettingsView`、`HistoryView` などへ責務を分ける |

## Decisions

| Date | Decision | Reason |
| --- | --- | --- |
| 2026-08-08 | Progress StateをUnit/Boltとは別モジュールとして管理する | Unit/Boltは作業分解、Progress Stateは進行台帳として責務を分けるため |
| 2026-08-09 | BackendはSpring Boot、FrontendはVite + React + TypeScriptで開始する | MVPのAPI/画面を小さく実装しやすく、既存資料の想定構成に合うため |
| 2026-08-12 | U-01の初期Repositoryはin-memoryで実装する | U-06で永続化を扱うため、まずAPI/Service境界と画面連携を固める |
| 2026-08-16 | CommuterPassの入力制約はDTOではなく値オブジェクトで表現する | ドメイン駆動の方針に合わせ、StationName/FareAmount/PassPeriod/PassMemoが不正状態を拒否するため |
| 2026-08-17 | `frontend/src/main.tsx` のコンポーネント分割はB-01-09で扱う | 現在のBoltはAPI TestとU-01の動作確認に集中し、画面構造の改善を別Boltとしてレビューしやすくするため |

## Blockers

| Date | Scope | Blocker | Needed Action |
| --- | --- | --- | --- |

## Learn Log

| Date | Unit | Observation | Next Action |
| --- | --- | --- | --- |
