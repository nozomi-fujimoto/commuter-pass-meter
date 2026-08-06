# Commuter Pass Meter

会社に行くのが少し嬉しくなるための、定期券回収メーターアプリです。

定期区間と定期料金を登録し、出社した日に「出社」ボタンを押すことで、定期を買わずに通常運賃で通勤した場合の金額を積み上げます。定期料金との差分、回収率、元が取れるまでの残り回数を表示し、出社するたびに「得した」感覚を得られる体験を作ります。

## 想定構成

- Frontend: React
- Backend: Spring Boot REST API
- Database: MVPではH2またはPostgreSQL
- Auth: MVPではなし。将来的にユーザー認証を追加

## MVP

- 定期情報を登録する
- 今日の出社を記録する
- 出社履歴を確認する
- 定期料金に対する回収率と差額を見る

## Documents

- [AIDLC要件定義](docs/aidlc-requirements.md)
- [画面設計](docs/screen-design.md)
- [API設計](docs/api-design.md)
- [データモデル](docs/data-model.md)
- [開発ロードマップ](docs/roadmap.md)
# commuter-pass-meter
