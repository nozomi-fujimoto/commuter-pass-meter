# AIDLC Progress

## Current Focus

- Unit: U-01 Commuter Pass Settings
- Status: todo
- Active Bolt:
- Last Updated: 2026-08-12

## Unit Progress

| Unit | Status | Current Bolt | Verification | Notes |
| --- | --- | --- | --- | --- |
| U-00 Project Foundation | done |  | `mvn -Dmaven.repo.local=/private/tmp/commuter-pass-meter-m2 test`, `npm run build`, `npm audit --audit-level=moderate` | Spring BootとReactの最小構成、Health API、初期Dashboardを追加 |
| U-01 Commuter Pass Settings | todo |  |  | 定期情報の登録・更新 |
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

## Decisions

| Date | Decision | Reason |
| --- | --- | --- |
| 2026-08-08 | Progress StateをUnit/Boltとは別モジュールとして管理する | Unit/Boltは作業分解、Progress Stateは進行台帳として責務を分けるため |
| 2026-08-09 | BackendはSpring Boot、FrontendはVite + React + TypeScriptで開始する | MVPのAPI/画面を小さく実装しやすく、既存資料の想定構成に合うため |

## Blockers

| Date | Scope | Blocker | Needed Action |
| --- | --- | --- | --- |

## Learn Log

| Date | Unit | Observation | Next Action |
| --- | --- | --- | --- |
