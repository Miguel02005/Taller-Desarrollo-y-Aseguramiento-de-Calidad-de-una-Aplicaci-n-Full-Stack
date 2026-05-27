import { useState } from 'react'

import "./BookCard.css"

function HeartIcon() {
  return (
    <svg viewBox="0 0 24 24">
      <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
    </svg>
  )
}

function BookCard({ title, author, year, editions, image, favorited = false }) {
  const [isFav, setIsFav] = useState(favorited)

  return (
    <div className="book-card">
      <div className="book-cover">
        {image ? (
          <img src={image} alt={title} />
        ) : (
          <div className="book-cover-fallback">
            <span className="fallback-title">{title}</span>
            <span className="fallback-author">{author}</span>
          </div>
        )}

        <button
          className={`fav-btn ${isFav ? 'active' : ''}`}
          onClick={() => setIsFav(prev => !prev)}
          title="Favorito"
        >
          <HeartIcon />
        </button>
      </div>
      <div className="book-info">
        <div className="book-title">{title}</div>
        <div className="book-author">{author}</div>
        <div className="book-meta">
          <span className="book-year">Publicado: {year}</span>
          <span className="book-editions">{editions} Ediciones</span>
          <button className="details-btn">Detalles</button>
        </div>
      </div>
    </div>
  )
}

export default BookCard