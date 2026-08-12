import React from 'react';
import ReactDOM from 'react-dom/client';
import { CalendarDays, History, Settings, TrainFront } from 'lucide-react';
import './styles.css';

function App() {
  return (
    <main className="appShell">
      <nav className="sideNav" aria-label="Primary">
        <div className="brand">
          <TrainFront aria-hidden="true" size={24} />
          <span>Pass Meter</span>
        </div>
        <a className="navItem active" href="#dashboard">
          <CalendarDays aria-hidden="true" size={18} />
          Dashboard
        </a>
        <a className="navItem" href="#history">
          <History aria-hidden="true" size={18} />
          History
        </a>
        <a className="navItem" href="#settings">
          <Settings aria-hidden="true" size={18} />
          Settings
        </a>
      </nav>

      <section className="content" id="dashboard">
        <header className="pageHeader">
          <div>
            <p className="eyebrow">Commuter Pass Meter</p>
            <h1>今日の出社を記録</h1>
          </div>
          <button className="primaryButton" type="button">出社</button>
        </header>

        <section className="summaryBand" aria-label="Recovery summary">
          <div>
            <p className="label">定期回収率</p>
            <p className="metric">0%</p>
          </div>
          <div>
            <p className="label">通常運賃換算</p>
            <p className="metric">0 円</p>
          </div>
          <div>
            <p className="label">残り出社回数</p>
            <p className="metric">未設定</p>
          </div>
        </section>

        <section className="emptyState" aria-label="No commuter pass">
          <h2>定期情報を登録してください</h2>
          <p>自宅側駅、会社側駅、片道運賃、定期料金、期間を登録すると回収状況を表示できます。</p>
          <a className="secondaryButton" href="#settings">Settings を開く</a>
        </section>
      </section>
    </main>
  );
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
