import { useState, useEffect } from 'react';
import { useFavorites } from '../../context/FavoritesContext.jsx';
import './FavoritesPanel.css';

// Iconos
function HeartIcon({ filled }) {
    return (
        <svg width="20" height="20" viewBox="0 0 24 24" fill={filled ? "#ef4444" : "none"} stroke={filled ? "#ef4444" : "currentColor"} strokeWidth="2">
            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
        </svg>
    );
}

function CloseIcon() {
    return (
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <line x1="18" y1="6" x2="6" y2="18" />
            <line x1="6" y1="6" x2="18" y2="18" />
        </svg>
    );
}

function EmptyHeartIcon() {
    return (
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
        </svg>
    );
}

function BookPlaceholderIcon() {
    return (
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
        </svg>
    );
}

// Panel de favoritos moderno
export default function FavoritesPanel({ onClose }) {
    // Obtener favoritos desde el contexto - se actualiza automáticamente
    const { favorites, removeFavorite, refreshFavorites } = useFavorites();
    const [removingId, setRemovingId] = useState(null);
    const [loading, setLoading] = useState(false);

    // Refrescar favoritos al abrir el panel
    useEffect(() => {
        refreshFavorites();
    }, []);

    const handleRemove = async (id) => {
        setRemovingId(id);
        try {
            await removeFavorite(id);
        } catch (err) {
            console.error('Error removing favorite:', err);
        } finally {
            setRemovingId(null);
        }
    };

    const handleClose = () => {
        if (onClose) onClose();
    };

    const favoriteCount = favorites.length;

    return (
        <div className="favorites-overlay" onClick={handleClose}>
            <div className="favorites-panel" onClick={(e) => e.stopPropagation()}>
                {/* Header */}
                <div className="favorites-header">
                    <div className="favorites-header-content">
                        <HeartIcon filled />
                        <h2>Mis Favoritos</h2>
                        <span className="favorites-count">{favoriteCount}</span>
                    </div>
                    <button className="favorites-close" onClick={handleClose} title="Cerrar">
                        <CloseIcon />
                    </button>
                </div>

                {/* Lista de favoritos */}
                <div className="favorites-body">
                    {favoriteCount === 0 ? (
                        <div className="favorites-empty">
                            <div className="favorites-empty-icon">
                                <EmptyHeartIcon />
                            </div>
                            <h3>Sin favoritos aún</h3>
                            <p>Haz clic en el corazón de un libro para agregarlo a tus favoritos.</p>
                        </div>
                    ) : (
                        <div className="favorites-list">
                            {favorites.map((fav) => (
                                <div
                                    key={fav.id}
                                    className={`favorite-item ${removingId === fav.id ? 'removing' : ''}`}
                                >
                                    <div className="favorite-cover">
                                        {fav.coverUrl ? (
                                            <img src={fav.coverUrl} alt={fav.title} loading="lazy" />
                                        ) : (
                                            <div className="favorite-cover-placeholder">
                                                <BookPlaceholderIcon />
                                            </div>
                                        )}
                                    </div>
                                    <div className="favorite-info">
                                        <span className="favorite-title">{fav.title}</span>
                                        <span className="favorite-author">{fav.author || 'Autor desconocido'}</span>
                                    </div>
                                    <button
                                        className="favorite-remove"
                                        onClick={() => handleRemove(fav.id)}
                                        disabled={removingId === fav.id}
                                        title="Eliminar de favoritos"
                                    >
                                        <CloseIcon />
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* Footer */}
                {favoriteCount > 0 && (
                    <div className="favorites-footer">
                        <p className="favorites-hint">Los favoritos se guardan automáticamente</p>
                    </div>
                )}
            </div>
        </div>
    );
}