import './App.css'
import SmartBook from './components/Title_Component/Title_Card'
import SearchCard from './components/Search_Component/Search_Card'
import HistoryCard from './components/History_Component/History_Card'

function App() {
  return (
    <div className="layout">
      <header className="site-header"> 
        <SmartBook />
      </header>

      <div className="site-body">
        <aside className="sidebar">
          <div className="sidebar-content">
            <SearchCard />
            <HistoryCard />
          </div>
          <div className="sidebar-bottom-space" />
        </aside>

        <main className="main">
          <section className="content">
          </section>
        </main>
      </div>

      <footer className="site-footer">
        <small>© 2026 Mi aplicación</small>
      </footer>
    </div>
  )
}

export default App
