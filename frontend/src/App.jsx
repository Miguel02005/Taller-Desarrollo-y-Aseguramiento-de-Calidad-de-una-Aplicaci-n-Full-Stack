import './App.css'

import BookGrid from './components/BookGrid/BookGrid'

function App() {
  return (
    <div className="layout">
      <header className="site-header"> 
        <h1>Mi aplicación</h1>
      </header>

      <div className="site-body">
        <aside className="sidebar">
          <nav>
            <ul>
              <li>Inicio</li>
              <li>Libros</li>
              <li>Usuarios</li>
              <li>Configuración</li>
            </ul>
          </nav>
        </aside>

        <main className="main">
          <section className="content">
            <BookGrid />
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
