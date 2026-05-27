import { useState } from 'react'
import './Search_Style.css'

export default function SearchCard() {
  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')
  const [language, setLanguage] = useState('Spanish')
  const [year, setYear] = useState('')

  const handleSubmit = (event) => {
    event.preventDefault()
    const params = { title, author, language, year }
    console.log('Buscar libros con:', params)
    alert('Búsqueda simulada:\n' + JSON.stringify(params, null, 2))
  }

  return (
    <article className="search-card">
      <div className="search-card_header">
        <h2>Búsqueda Avanzada</h2>
        <p>Filtra libros por título, autor, idioma y año de publicación.</p>
      </div>

      <form className="search-card_form" onSubmit={handleSubmit}>
        <label className="search-card_label" htmlFor="book-title">
          Título del Libro
          <input
            id="book-title"
            className="search-card_input"
            type="text"
            placeholder="Ej: El Quijote"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </label>

        <label className="search-card_label" htmlFor="book-author">
          Autor
          <input
            id="book-author"
            className="search-card_input"
            type="text"
            placeholder="Ej: Cervantes"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
          />
        </label>

        <label className="search-card_label" htmlFor="book-language">
          Idioma
          <select
            id="book-language"
            className="search-card_select"
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
          >
            <option value="Spanish">Spanish</option>
            <option value="English">English</option>
            <option value="French">French</option>
            <option value="German">German</option>
          </select>
        </label>

        <label className="search-card_label" htmlFor="book-year">
          Publicado después de
          <input
            id="book-year"
            className="search-card_input"
            type="number"
            min="0"
            placeholder="Año"
            value={year}
            onChange={(e) => setYear(e.target.value)}
          />
        </label>

        <button type="submit" className="search-card_button">
          Search Books
        </button>
      </form>
    </article>
  )
}
