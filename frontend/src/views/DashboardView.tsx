import { CommuterPass, Dashboard } from '../types';
import { formatYen } from '../commuterPass';
import { StatusMessage } from '../components/StatusMessage';

type DashboardViewProps = {
  attendanceStatusKnown: boolean;
  attendanceError: string;
  commuterPass: CommuterPass | null;
  error: string;
  loading: boolean;
  notice: string;
  recording: boolean;
  dashboard: Dashboard | null;
  recordedToday: boolean;
  onReload: () => void;
  onRecordAttendance: () => void;
  onOpenSettings: () => void;
};

export function DashboardView({
  attendanceStatusKnown,
  attendanceError,
  commuterPass,
  error,
  loading,
  notice,
  recording,
  dashboard,
  recordedToday,
  onReload,
  onRecordAttendance,
  onOpenSettings,
}: DashboardViewProps) {
  const attendanceButtonLabel = recordedToday ? '記録済み' : recording ? '記録中' : '出社';

  return (
    <section className="content" id="dashboard">
      <header className="pageHeader">
        <div>
          <p className="eyebrow">Commuter Pass Meter</p>
          <h1>今日の出社を記録</h1>
        </div>
        <button
          className="primaryButton"
          type="button"
          disabled={!commuterPass || loading || recording || !attendanceStatusKnown || recordedToday}
          onClick={onRecordAttendance}
        >
          {attendanceButtonLabel}
        </button>
      </header>

      <StatusMessage loading={loading} error={error} notice={notice} />
      {error && !loading && (
        <button className="secondaryButton" type="button" disabled={recording} onClick={onReload}>再読み込み</button>
      )}
      {attendanceError && <p className="statusMessage error" role="alert">{attendanceError}</p>}

      {commuterPass ? (
        <>
          {dashboard && (
            <>
              <section className="summaryBand" aria-label="Recovery summary">
                <div>
                  <p className="label">定期回収率</p>
                  <p className="metric">{dashboard.recoveryRate}%</p>
                </div>
                <div>
                  <p className="label">往復運賃</p>
                  <p className="metric">{formatYen(commuterPass.oneWayFare * 2)}</p>
                </div>
                <div>
                  <p className="label">残り出社回数</p>
                  <p className="metric">{dashboard.remainingAttendanceCount} 回</p>
                </div>
              </section>

              <section className="detailBand" aria-label="Attendance totals">
                <div><p className="label">出社回数</p><p className="detailValue">{dashboard.attendanceCount} 回</p></div>
                <div><p className="label">今月の出社回数</p><p className="detailValue">{dashboard.monthlyAttendanceCount} 回</p></div>
                <div><p className="label">通常運賃換算の累計額</p><p className="detailValue">{formatYen(dashboard.normalFareTotal)}</p></div>
                <div><p className="label">定期料金との差額</p><p className="detailValue">{dashboard.difference > 0 ? '+' : ''}{formatYen(dashboard.difference)}</p></div>
                <div><p className="label">元が取れる出社回数</p><p className="detailValue">{dashboard.breakEvenAttendanceCount} 回</p></div>
              </section>
              <section className="attendanceState" aria-label="Recent attendance records">
                <h2>最近の出社履歴</h2>
                {dashboard.recentAttendanceRecords.length === 0 ? <p>出社記録はまだありません。</p> : (
                  <ul className="recentAttendanceList">
                    {dashboard.recentAttendanceRecords.map((record) => (
                      <li key={record.id}><span>{record.attendanceDate}</span><span>{formatYen(record.roundTripFare)}</span></li>
                    ))}
                  </ul>
                )}
              </section>
            </>
          )}

          {recordedToday ? (
            <section className="attendanceState" aria-label="Today's attendance">
              <p className="label">今日の出社</p>
              <p className="detailValue">今日は記録済みです。</p>
            </section>
          ) : null}

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
      ) : !loading && !error ? (
        <section className="emptyState" aria-label="No commuter pass">
          <h2>定期情報を登録してください</h2>
          <p>自宅側駅、会社側駅、片道運賃、定期料金、期間を登録すると回収状況を表示できます。</p>
          <button className="secondaryButton" type="button" onClick={onOpenSettings}>Settings を開く</button>
        </section>
      ) : null}
    </section>
  );
}
