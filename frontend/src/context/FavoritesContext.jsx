import { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { getFavorites, addFavorite as addFav, removeFavorite as removeFav } from '../services/favoriteService.js';

const FavoritesContext = createContext(null);

export function FavoritesProvider({ children }) {
    const [favorites, setFavorites] = useState([]);
    const [initialized, setInitialized] = useState(false);

    // Cargar favoritos al iniciar el provider
    useEffect(() => {
        const loadFavorites = async () => {
            try {
                const data = await getFavorites();
                setFavorites(data || []);
            } catch (err) {
                console.error('Error loading favorites:', err);
            } finally {
                setInitialized(true);
            }
        };

        loadFavorites();
    }, []);

    // Refrescar favoritos desde el backend
    const refreshFavorites = useCallback(async () => {
        try {
            const data = await getFavorites();
            setFavorites(data || []);
        } catch (err) {
            console.error('Error refreshing favorites:', err);
        }
    }, []);

    // Agregar favorito - actualiza estado global automáticamente
    const addFavorite = useCallback(async (bookKey) => {
        const result = await addFav(bookKey);
        // Recargar todos los favoritos para mantener sincronía con el backend
        await refreshFavorites();
        return result;
    }, [refreshFavorites]);

    // Eliminar favorito - actualiza estado global automáticamente
    const removeFavorite = useCallback(async (id) => {
        await removeFav(id);
        // Recargar todos los favoritos para mantener sincronía con el backend
        await refreshFavorites();
    }, [refreshFavorites]);

    // Verificar si un libro es favorito (búsqueda local)
    const isFavorite = useCallback((bookKey) => {
        return favorites.some(fav => fav.bookKey === bookKey);
    }, [favorites]);

    // Obtener el ID de favorito por bookKey
    const getFavoriteId = useCallback((bookKey) => {
        const fav = favorites.find(f => f.bookKey === bookKey);
        return fav ? fav.id : null;
    }, [favorites]);

    const value = {
        favorites,
        addFavorite,
        removeFavorite,
        isFavorite,
        getFavoriteId,
        refreshFavorites,
        initialized,
    };

    return (
        <FavoritesContext.Provider value={value}>
            {children}
        </FavoritesContext.Provider>
    );
}

export function useFavorites() {
    const context = useContext(FavoritesContext);
    if (!context) {
        throw new Error('useFavorites must be used within FavoritesProvider');
    }
    return context;
}

export default FavoritesContext;