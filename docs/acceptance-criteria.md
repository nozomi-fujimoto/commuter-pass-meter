# 受け入れ条件 v0.1

## 1. Scope

このドキュメントは MVP の完了条件を定義する。

対象は単一ユーザー、認証なし、定期情報1件の利用を前提とする。

## 2. Assumptions

- MVP では固定ユーザーを使う
- 有効な定期情報は同時に1件のみ扱う
- 片道運賃から往復運賃を計算する
- 出社記録は日付単位で管理する
- 日付はアプリの実行環境のローカル日付を基準にする

## 3. Acceptance Criteria

### AC-001: 定期情報を登録できる

Given ユーザーが定期情報を未登録である  
When 自宅側駅、会社側駅、片道運賃、定期料金、開始日、終了日を入力して保存する  
Then 定期情報が保存され、Dashboard で定期情報と集計値を確認できる

### AC-002: 定期情報の入力値を検証する

Given ユーザーが定期情報を入力している  
When 駅名が空、片道運賃が1未満、定期料金が1未満、または開始日が終了日より後で保存する  
Then 保存されず、入力エラーが表示される

### AC-003: 今日の出社を記録できる

Given 有効な定期情報が登録されている  
When ユーザーが Dashboard の出社ボタンを押す  
Then 今日の日付で出社記録が1件作成され、通常運賃換算の累計額が往復運賃分増える

### AC-004: 同じ日の出社記録を重複登録しない

Given 今日の出社記録がすでに存在する  
When ユーザーが同じ日付で出社記録を作成しようとする  
Then 新しい出社記録は作成されず、重複登録であることが分かる

### AC-005: 定期期間外の日付は記録しない

Given 有効な定期情報が登録されている  
When 定期開始日前または定期終了日後の日付で出社記録を作成しようとする  
Then 出社記録は作成されず、定期期間外であることが分かる

### AC-006: Dashboard で回収状況を確認できる

Given 有効な定期情報と出社記録が存在する  
When ユーザーが Dashboard を開く  
Then 出社回数、今月の出社回数、通常運賃換算の累計額、定期料金との差分、回収率、元が取れるまでの残り出社回数を確認できる

### AC-007: 元が取れていない状態を表示できる

Given 通常運賃換算の累計額が定期料金未満である  
When ユーザーが Dashboard を開く  
Then 未回収額、回収率、元が取れるまでの残り出社回数が分かる

### AC-008: 元が取れた状態を表示できる

Given 通常運賃換算の累計額が定期料金以上である  
When ユーザーが Dashboard を開く  
Then お得額が分かり、元が取れるまでの残り出社回数は0として表示される

### AC-009: 今日すでに出社済みであることを表示できる

Given 今日の出社記録がすでに存在する  
When ユーザーが Dashboard を開く  
Then 出社ボタンは押せない状態になり、今日は記録済みであることが分かる

### AC-010: 出社履歴を確認できる

Given 出社記録が存在する  
When ユーザーが History を開く  
Then 出社日、通常運賃換算額、月別出社回数を確認できる

### AC-011: 出社履歴を削除できる

Given 出社記録が存在する  
When ユーザーが History で対象の出社記録を削除する  
Then 出社記録が削除され、Dashboard の集計値が削除後の内容に更新される

### AC-012: 定期未登録時は設定への導線を表示する

Given 定期情報が登録されていない  
When ユーザーが Dashboard を開く  
Then 集計値は表示されず、定期情報を登録するための導線が表示される

## 4. Calculation Acceptance

以下の計算結果を満たすこと。

```text
roundTripFare = oneWayFare * 2
normalFareTotal = attendanceCount * roundTripFare
difference = normalFareTotal - passPrice
recoveryRate = normalFareTotal / passPrice * 100
breakEvenAttendanceCount = ceil(passPrice / roundTripFare)
remainingAttendanceCount = max(breakEvenAttendanceCount - attendanceCount, 0)
```

例:

```text
oneWayFare = 310
passPrice = 14200
attendanceCount = 12

roundTripFare = 620
normalFareTotal = 7440
difference = -6760
recoveryRate = 52.4
breakEvenAttendanceCount = 23
remainingAttendanceCount = 11
```

## 5. Done Definition

MVP は以下を満たしたとき完了とする。

- Must 要件に対応する受け入れ条件がすべて満たされている
- 計算ロジックの単体テストが通っている
- API の主要正常系と異常系が確認できている
- Dashboard、Settings、History の主要操作が手動確認できている
- 実際の出社日に記録しても入力負担が小さいことを確認できている
