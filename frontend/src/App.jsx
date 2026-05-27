import './App.css'

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
            <h2>Contenido principal</h2>
            <p>Aquí va el contenido de la página.</p>
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
