import { useState, useEffect } from 'react'
import BookCard from '../BookCard/BookCard'
import './BookGrid.css'

function BookGrid() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        setLoading(true)
        const res = await fetch(
          'https://openlibrary.org/search.json?q=literatura+latinoamericana&limit=8&lang=spa',
          {
            headers: {
              'User-Agent': 'SmartBookFinder (contacto@ejemplo.com)'
            }
          }
        )
        if (!res.ok) throw new Error('Error al obtener los libros')
        const data = await res.json()

        const mapped = data.docs.map(book => ({
          id: book.key,
          title: book.title,
          author: book.author_name?.[0] ?? 'Autor desconocido',
          year: book.first_publish_year ?? '—',
          editions: book.edition_count ?? 1,
          image: book.cover_i
            ? `https://covers.openlibrary.org/b/id/${book.cover_i}-M.jpg`
            : null,
        }))

        setBooks(mapped)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchBooks()
  }, [])

  if (loading) return <p className="grid-status">Cargando libros...</p>
  if (error)   return <p className="grid-status grid-error">Error: {error}</p>

  return (
    <div className="books-grid">
      {books.map(book => (
        <BookCard key={book.id} {...book} />
      ))}
    </div>
  )
}

export default BookGrid