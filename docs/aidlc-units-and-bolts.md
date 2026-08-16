# AIDLC Unit/Bolt定義 v0.1

## 1. Purpose

このドキュメントは、Commuter Pass Meter のAIDLCを回すための作業分解単位を定義する。

- `Unit`: ユーザー価値または検証可能な成果で区切る開発単位
- `Bolt`: Unitを完了させるための小さな実装・検証タスク

Unitは「何ができるようになるか」を表し、Boltは「次に何を作るか」を表す。

## 2. Unit Definition

Unitは、AIDLCのDiscover、Define、Design、Develop、Learnを1周できる大きさにする。

### Unitの条件

- 1つ以上の受け入れ条件に対応している
- 実装後にユーザー操作またはテストで確認できる
- Backend、Frontend、Data、Testの境界をまたいでもよい
- 完了時にREADME、仕様、テスト計画のどれかが更新済みである
- 次のUnitに依存されても壊れにくい状態で終わる

### UnitのDone

- 対応するAcceptance Criteriaを満たしている
- 必要なUnit TestまたはAPI Testが追加されている
- 主要な手動確認手順が明確である
- 既存の主要テストが通っている
- Learn観点の確認メモを残せる状態になっている

## 3. Bolt Definition

Boltは、1回のAI作業依頼または短い実装セッションで完了できる粒度にする。

### Boltの条件

- 1つの明確な成果物を持つ
- 変更範囲が狭い
- 完了判定がコマンド、テスト、画面確認のいずれかで可能
- 失敗時に差し戻しやすい
- 複数のBoltをまとめるとUnitが完了する

### BoltのDone

- 対象ファイルの変更が完了している
- 最小限の検証が実行済みである
- 次のBoltに必要な前提が満たされている
- 未解決事項がある場合は明記されている

## 4. Units

### U-00: Project Foundation

目的: Spring BootとReactを開発できる土台を作る。

対応範囲:

- 開発環境
- ディレクトリ構成
- 起動方法
- 基本CIまたはローカル検証コマンド

Done:

- BackendとFrontendがローカルで起動できる
- READMEに起動手順がある
- 最小のヘルスチェックまたは初期画面を確認できる

Bolts:

- B-00-01: Spring Bootプロジェクトを作成する
- B-00-02: Reactプロジェクトを作成する
- B-00-03: BackendのヘルスチェックAPIを追加する
- B-00-04: Frontendの初期画面を追加する
- B-00-05: READMEに起動手順と検証コマンドを追記する

### U-01: Commuter Pass Settings

目的: 定期情報を1件登録・更新できる。

対応AC:

- AC-001
- AC-002
- AC-012

Done:

- 定期情報の作成・取得・更新APIが動く
- 入力値バリデーションが動く
- 定期未登録時にSettingsへの導線を表示できる

Bolts:

- B-01-01: `CommuterPass` のEntity/Modelを実装する
- B-01-02: `CommuterPass` のRepositoryを実装する
- B-01-03: 作成・取得・更新Serviceを実装する
- B-01-04: `GET/POST/PUT /api/commuter-pass` を実装する
- B-01-05: 入力値バリデーションと400応答を実装する
- B-01-06: Settings画面のフォームを実装する
- B-01-07: Dashboardの定期未登録状態を実装する
- B-01-08: API TestとFrontend手動確認を追加する
- B-01-09: Frontendのタブ表示を画面コンポーネントへ分割する

### U-02: Attendance Recording

目的: 今日の出社を重複なく記録できる。

対応AC:

- AC-003
- AC-004
- AC-005
- AC-009

Done:

- 出社記録の作成APIが動く
- 同一日付の重複登録を防げる
- 定期期間外の日付を拒否できる
- 今日記録済み状態をDashboardに表示できる

Bolts:

- B-02-01: `AttendanceRecord` のEntity/Modelを実装する
- B-02-02: 日付重複を防ぐRepository制約を実装する
- B-02-03: 定期期間内外の判定Serviceを実装する
- B-02-04: `POST /api/attendance-records` を実装する
- B-02-05: 重複時の409応答を実装する
- B-02-06: 期間外登録時の400応答を実装する
- B-02-07: Dashboardの出社ボタンと記録済み状態を実装する
- B-02-08: Unit TestとAPI Testを追加する

### U-03: Dashboard Calculation

目的: 定期料金の回収状況をDashboardで確認できる。

対応AC:

- AC-006
- AC-007
- AC-008

Done:

- Dashboard APIが集計済みレスポンスを返す
- 回収前・回収後の表示が切り替わる
- 計算ロジックのUnit Testが通る

Bolts:

- B-03-01: 回収計算Serviceを実装する
- B-03-02: `GET /api/dashboard` を実装する
- B-03-03: 今月の出社回数集計を実装する
- B-03-04: 最近の出社履歴レスポンスを実装する
- B-03-05: Dashboardの回収率、差額、残り回数表示を実装する
- B-03-06: 回収前・回収後の文言切り替えを実装する
- B-03-07: UT-001からUT-003を実装する
- B-03-08: API-007を実装する

### U-04: Attendance History

目的: 出社履歴を確認・削除できる。

対応AC:

- AC-010
- AC-011

Done:

- 出社履歴一覧APIが動く
- 出社履歴削除APIが動く
- 削除後にDashboard集計が更新される

Bolts:

- B-04-01: `GET /api/attendance-records` を実装する
- B-04-02: 月別出社回数集計を実装する
- B-04-03: `DELETE /api/attendance-records/{id}` を実装する
- B-04-04: History画面の一覧表示を実装する
- B-04-05: History画面の削除操作を実装する
- B-04-06: 削除後のDashboard再取得を実装する
- B-04-07: API-006とFE-005を確認する

### U-05: MVP Experience Polish

目的: 1か月使い続けられる程度に、記録体験と表示を整える。

対応範囲:

- Success Criteria
- Manual Learn Test
- Regression Checklist

Done:

- 出社日にすぐ記録できる
- 数字の意味が直感的に分かる
- ポジティブな短い文言が表示される
- 回帰確認チェックリストが通る

Bolts:

- B-05-01: 出社記録後のポジティブ文言を実装する
- B-05-02: Dashboardの情報優先度とレイアウトを調整する
- B-05-03: Settingsの入力負担を下げる
- B-05-04: Historyの見返しやすさを調整する
- B-05-05: Regression Checklistを実行する
- B-05-06: Learn Logを記録する

### U-06: Local Persistence Upgrade

目的: MVPをローカル利用しやすい永続化構成にする。

対応範囲:

- H2またはPostgreSQL
- ローカル起動
- データ保持

Done:

- アプリ再起動後も定期情報と出社履歴が残る
- ローカル環境のDB設定がREADMEにある
- 主要API Testが永続化込みで通る

Bolts:

- B-06-01: H2永続化設定を追加する
- B-06-02: 初期スキーマまたはマイグレーションを追加する
- B-06-03: ローカルDB設定をREADMEに追記する
- B-06-04: 永続化込みのAPI Testを実行する

## 5. Recommended Execution Order

1. U-00: Project Foundation
2. U-01: Commuter Pass Settings
3. U-02: Attendance Recording
4. U-03: Dashboard Calculation
5. U-04: Attendance History
6. U-05: MVP Experience Polish
7. U-06: Local Persistence Upgrade

U-06は、H2のインメモリで先にMVPを動かす場合は後回しにできる。

## 6. AIDLC Loop per Unit

各Unitは以下のテンプレートで回す。

```text
Unit:
Hypothesis:
Target AC:
Bolts:
Implementation Notes:
Verification:
Learn:
Next:
```

## 7. Bolt Prompt Template

AIにBoltを依頼するときは、以下の形を使う。

```text
Bolt:
Goal:
Files likely involved:
Acceptance:
Verification command:
Constraints:
```

例:

```text
Bolt: B-03-01
Goal: 回収計算Serviceを実装する
Files likely involved: backend service and test package
Acceptance: UT-001からUT-003の期待値を満たす
Verification command: ./mvnw test
Constraints: ControllerやFrontendはまだ触らない
```
