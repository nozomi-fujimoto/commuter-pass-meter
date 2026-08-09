# 開発ロードマップ v0.1

## Phase 1: Documentation

- 要件定義を作成する
- 画面設計を作成する
- API設計を作成する
- データモデルを作成する

## Phase 2: Backend MVP

- Spring Bootプロジェクトを作成する
- CommuterPass APIを実装する
- AttendanceRecord APIを実装する
- Dashboard APIを実装する
- H2で動作確認する
- サービス層の計算ロジックをテストする

## Phase 3: Frontend MVP

- Reactプロジェクトを作成する
- ダッシュボード画面を実装する
- 定期設定画面を実装する
- 履歴画面を実装する
- API連携を実装する

## Phase 4: Local Use

- 実際の出社日に使う
- 押したくなる文言か確認する
- 数字の見せ方を改善する
- 設定入力の面倒さを確認する

## Phase 5: Expansion

- ユーザー認証
- PostgreSQL対応
- 複数定期対応
- カレンダー連携
- 勤怠打刻連携
- AIによる月次振り返り

## First Implementation Order

1. Spring Boot APIの雛形を作る
2. 計算ロジックをサービス層に実装する
3. Dashboard APIを固定データで返す
4. ReactでDashboardを作る
5. DB保存に切り替える
6. 設定・履歴画面を追加する

## AIDLC Unit/Bolt

実装時の作業分解は [AIDLC Unit/Bolt定義](aidlc-units-and-bolts.md) に従う。
進行状態は [AIDLC Progress State](aidlc-progress-state.md) と [AIDLC Progress](aidlc-progress.md) で管理する。

- Unit: ユーザー価値または検証可能な成果で区切る開発単位
- Bolt: Unitを完了させるための小さな実装・検証タスク
