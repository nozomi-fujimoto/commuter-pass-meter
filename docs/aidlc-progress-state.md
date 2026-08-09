# AIDLC Progress State v0.1

## 1. Purpose

このドキュメントは、AIDLCを回すときの進行状態を管理するためのモジュールを定義する。

Unit/Bolt定義が「何を作るか」を扱うのに対し、Progress Stateは「今どこまで進んでいるか」「次に何をするか」「何を学んだか」を扱う。

## 2. State Model

### Unit Status

| Status | Meaning |
| --- | --- |
| `todo` | 未着手 |
| `discovering` | 課題、仮説、前提を確認中 |
| `defined` | Unitの目的、AC、範囲が確定済み |
| `designing` | 画面、API、データ、テスト方針を設計中 |
| `developing` | Boltを実装中 |
| `verifying` | テスト、手動確認、受け入れ確認中 |
| `learning` | 実利用またはレビューから学びを記録中 |
| `done` | 完了 |
| `blocked` | 外部判断または未解決事項待ち |

### Bolt Status

| Status | Meaning |
| --- | --- |
| `todo` | 未着手 |
| `in_progress` | 作業中 |
| `done` | 完了 |
| `blocked` | 進行不能 |
| `skipped` | 今回のUnitでは実施しない |

## 3. Progress File

進行状態は以下のファイルで管理する。

```text
docs/aidlc-progress.md
```

このファイルは、人間とAIの両方が読み書きできる軽量な台帳として扱う。

## 4. Progress Template

```markdown
# AIDLC Progress

## Current Focus

- Unit:
- Status:
- Active Bolt:
- Last Updated:

## Unit Progress

| Unit | Status | Current Bolt | Verification | Notes |
| --- | --- | --- | --- | --- |
| U-00 | todo |  |  |  |
| U-01 | todo |  |  |  |
| U-02 | todo |  |  |  |
| U-03 | todo |  |  |  |
| U-04 | todo |  |  |  |
| U-05 | todo |  |  |  |
| U-06 | todo |  |  |  |

## Bolt Progress

| Bolt | Status | Result | Verification | Notes |
| --- | --- | --- | --- | --- |

## Decisions

| Date | Decision | Reason |
| --- | --- | --- |

## Blockers

| Date | Scope | Blocker | Needed Action |
| --- | --- | --- | --- |

## Learn Log

| Date | Unit | Observation | Next Action |
| --- | --- | --- | --- |
```

## 5. State Update Rules

- Unit開始時は `Current Focus` を更新する
- Bolt開始時は対象Boltを `in_progress` にする
- Bolt完了時は `done` にし、検証結果を記録する
- Unitの全必須Boltが完了したら `verifying` にする
- 受け入れ条件を満たしたら `learning` または `done` にする
- 実利用から得た改善点は `Learn Log` に記録する
- 判断を伴う変更は `Decisions` に残す
- 進行不能な理由は `Blockers` に残す

## 6. Operating Loop

1. `docs/aidlc-progress.md` で現在のUnitとBoltを確認する
2. 対象Unitの受け入れ条件を確認する
3. 1つのBoltだけを `in_progress` にする
4. 実装または検証を行う
5. Boltの結果と検証コマンドを記録する
6. Unitの状態を必要に応じて更新する
7. Learn LogまたはNext Actionを残す

## 7. Initial Progress

初期状態の進行台帳は、Project Foundation開始前として作成する。
