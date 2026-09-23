import { CommuterPass } from '../types';
import { formatYen } from '../commuterPass';
import { StatusMessage } from '../components/StatusMessage';

type DashboardViewProps = {
  commuterPass: CommuterPass | null;
  error: string;
  loading: boolean;
  notice: string;
  roundTripFare: number;
  onOpenSettings: () => void;
};

export function DashboardView({ commuterPass, error, loading, notice, roundTripFare, onOpenSettings }: DashboardViewProps) {
  return (
    <section className="content" id="dashboard">
      <header className="pageHeader">
        <div>
          <p className="eyebrow">Commuter Pass Meter</p>
          <h1>今日の出社を記録</h1>
        </div>
        <button className="primaryButton" type="button" disabled={!commuterPass}>出社</button>
      </header>

      <StatusMessage loading={loading} error={error} notice={notice} />

      {commuterPass ? (
        <>
          <section className="summaryBand" aria-label="Recovery summary">
            <div>
              <p className="label">定期回収率</p>
              <p className="metric">0%</p>
            </div>
            <div>
              <p className="label">往復運賃</p>
              <p className="metric">{formatYen(roundTripFare)}</p>
            </div>
            <div>
              <p className="label">残り出社回数</p>
              <p className="metric">未記録</p>
            </div>
          </section>

          <section className="detailBand" aria-label="Commuter pass details">
            <div>
              <p className="label">区間</p>
              <p className="detailValue">{commuterPass.fromStation} から {commuterPass.toStation}</p>
            </div>
            <div>
              <p className="label">定期料金</p>
              <p className="detailValue">{formatYen(commuterPass.passPrice)}</p>
            </div>
            <div>
              <p className="label">期間</p>
              <p className="detailValue">{commuterPass.startDate} - {commuterPass.endDate}</p>
            </div>
          </section>
        </>
      ) : !loading ? (
        <section className="emptyState" aria-label="No commuter pass">
          <h2>定期情報を登録してください</h2>
          <p>自宅側駅、会社側駅、片道運賃、定期料金、期間を登録すると回収状況を表示できます。</p>
          <button className="secondaryButton" type="button" onClick={onOpenSettings}>Settings を開く</button>
        </section>
      ) : null}
    </section>
  );
}
