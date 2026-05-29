import { createContext, useContext, useState, useCallback } from 'react';
import { getFavorites, addFavorite as addFav, removeFavorite as removeFav } from '../services/favoriteService.js';

const FavoritesContext = createContext(null);

export function FavoritesProvider({ children }) {
    const [favorites, setFavorites] = useState([]);
    const [refreshTrigger, setRefreshTrigger] = useState(0);

    const refreshFavorites = useCallback(async () => {
        try {
            const data = await getFavorites();
            setFavorites(data || []);
        } catch (err) {
            console.error('Error refreshing favorites:', err);
        }
    }, []);

    const addFavorite = useCallback(async (bookKey) => {
        const result = await addFav(bookKey);
        await refreshFavorites();
        return result;
    }, [refreshFavorites]);

    const removeFavorite = useCallback(async (id) => {
        await removeFav(id);
        await refreshFavorites();
    }, [refreshFavorites]);

    const isFavorite = useCallback((bookKey) => {
        return favorites.some(fav => fav.bookKey === bookKey);
    }, [favorites]);

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