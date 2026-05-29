import { useState, useEffect } from 'react';
import { addFavorite, removeFavorite, getFavorites } from '../../services/favoriteService.js';
import './BookCard.css';

function HeartIcon() {
    return (
        <svg viewBox="0 0 24 24">
            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
        </svg>
    );
}

function BookCard({
    workKey,
    id,
    title,
    author,
    publishYear,
    year,
    editions,
    coverUrl,
    image,
}) {
    const bookOpenLibraryKey = workKey;
    const bookId = id;

    const publicationYear = publishYear || year || '—';
    const bookCoverUrl = coverUrl || image;

    const [isFav, setIsFav] = useState(false);
    const [favoriteId, setFavoriteId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [showMessage, setShowMessage] = useState(null);
    const [favoriteCheckDone, setFavoriteCheckDone] = useState(false);

    useEffect(() => {
        let cancelled = false;

        const checkFavorite = async () => {
            if (!bookOpenLibraryKey) {
                setFavoriteCheckDone(true);
                return;
            }

            try {
                const favorites = await getFavorites();
                if (cancelled) return;

                const existing = favorites.find(fav => fav.bookKey === bookOpenLibraryKey);
                if (existing) {
                    setIsFav(true);
                    setFavoriteId(existing.id);
                }
            } catch (err) {
                console.error('Error checking favorite status:', err);
            } finally {
                if (!cancelled) {
                    setFavoriteCheckDone(true);
                }
            }
        };

        checkFavorite();

        return () => {
            cancelled = true;
        };
    }, [bookOpenLibraryKey]);

    const showTemporaryMessage = (message) => {
        setShowMessage(message);
        setTimeout(() => setShowMessage(null), 2000);
    };

    const handleToggleFavorite = async () => {
        if (!bookOpenLibraryKey) {
            showTemporaryMessage('No se puede guardar este libro');
            return;
        }

        setLoading(true);
        try {
            if (isFav && favoriteId) {
                await removeFavorite(favoriteId);
                setIsFav(false);
                setFavoriteId(null);
                showTemporaryMessage('Eliminado de favoritos');
            } else {
                const newFavorite = await addFavorite(bookOpenLibraryKey);
                setIsFav(true);
                setFavoriteId(newFavorite.id);
                showTemporaryMessage('Agregado a favoritos');
            }
        } catch (err) {
            console.error('Error toggling favorite:', err);
            showTemporaryMessage('Error al actualizar favorito');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="book-card">
            <div className="book-cover">
                {bookCoverUrl ? (
                    <img src={bookCoverUrl} alt={title} loading="lazy" />
                ) : (
                    <div className="book-cover-fallback">
                        <span className="fallback-title">{title}</span>
                        <span className="fallback-author">{author || 'Autor desconocido'}</span>
                    </div>
                )}

                <button
                    className={`fav-btn ${isFav ? 'active' : ''}`}
                    onClick={handleToggleFavorite}
                    disabled={loading || !favoriteCheckDone}
                    title={isFav ? 'Eliminar de favoritos' : 'Agregar a favoritos'}
                >
                    <HeartIcon />
                </button>

                {showMessage && (
                    <div className="fav-message">{showMessage}</div>
                )}
            </div>

            <div className="book-info">
                <div className="book-title" title={title}>{title}</div>
                <div className="book-author">{author || 'Autor desconocido'}</div>
                <div className="book-meta">
                    <span className="book-year">Publicado: {publicationYear}</span>
                    {editions > 0 && (
                        <span className="book-editions">{editions} Ediciones</span>
                    )}
                </div>
            </div>
        </div>
    );
}

export default BookCard;