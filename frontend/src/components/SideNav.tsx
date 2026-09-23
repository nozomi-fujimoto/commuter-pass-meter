import { CalendarDays, History, Settings, TrainFront } from 'lucide-react';
import { View } from '../types';

type SideNavProps = {
  view: View;
  onChangeView: (view: View) => void;
};

export function SideNav({ view, onChangeView }: SideNavProps) {
  return (
    <nav className="sideNav" aria-label="Primary">
      <div className="brand">
        <TrainFront aria-hidden="true" size={24} />
        <span>Pass Meter</span>
      </div>
      <button
        aria-current={view === 'dashboard' ? 'page' : undefined}
        className={`navItem ${view === 'dashboard' ? 'active' : ''}`}
        type="button"
        onClick={() => onChangeView('dashboard')}
      >
        <CalendarDays aria-hidden="true" size={18} />
        Dashboard
      </button>
      <button
        aria-current={view === 'history' ? 'page' : undefined}
        className={`navItem ${view === 'history' ? 'active' : ''}`}
        type="button"
        onClick={() => onChangeView('history')}
      >
        <History aria-hidden="true" size={18} />
        History
      </button>
      <button
        aria-current={view === 'settings' ? 'page' : undefined}
        className={`navItem ${view === 'settings' ? 'active' : ''}`}
        type="button"
        onClick={() => onChangeView('settings')}
      >
        <Settings aria-hidden="true" size={18} />
        Settings
      </button>
    </nav>
  );
}
