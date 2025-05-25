import Header from './Header';
import '../css/layout.css';

export default function Layout({ children }) {
  return (
    <div className="layout">
      <Header />
      <main className="layout-main">
        {children}
      </main>
    </div>
  );
}
