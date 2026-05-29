import { useState } from 'react';
import { useFavorites } from '../../context/FavoritesContext.jsx';
import './FavoritesPanel.css';

export default function FavoritesPanel() {
    const { favorites, removeFavorite, refreshFavorites } = useFavorites();
    const [showPanel, setShowPanel] = useState(false);

    const handleRemove = async (id) => {
        try {
            await removeFavorite(id);
        } catch (err) {
            console.error('Error removing favorite:', err);
        }
    };

    const favoriteCount = favorites.length;

    return (
        <>
            <button
                className="favorites-toggle-btn"
                onClick={() => {
                    setShowPanel(!showPanel);
                    if (!showPanel) refreshFavorites(); 
                }}
                title="Ver favoritos"
            >
                ❤️ <span className="favorites-count">{favoriteCount}</span>
            </button>

            {showPanel && (
                <div className="favorites-panel">
                    <div className="favorites-panel-header">
                        <h3>❤️ Mis Favoritos ({favoriteCount})</h3>
                        <button
                            className="favorites-close-btn"
                            onClick={() => setShowPanel(false)}
                        >
                            ✕
                        </button>
                    </div>

                    <div className="favorites-list">
                        {favoriteCount === 0 ? (
                            <p className="favorites-empty">
                                No tienes libros favoritos todavía.
                            </p>
                        ) : (
                            favorites.map(fav => (
                                <div key={fav.id} className="favorite-item">
                                    <div className="favorite-cover">
                                        {fav.coverUrl ? (
                                            <img src={fav.coverUrl} alt={fav.title} />
                                        ) : (
                                            <div className="favorite-cover-placeholder">📖</div>
                                        )}
                                    </div>
                                    <div className="favorite-info">
                                        <span className="favorite-title">{fav.title}</span>
                                        <span className="favorite-author">{fav.author || 'Autor desconocido'}</span>
                                    </div>
                                    <button
                                        className="favorite-remove-btn"
                                        onClick={() => handleRemove(fav.id)}
                                        title="Eliminar de favoritos"
                                    >
                                        ✕
                                    </button>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </>
    );
}