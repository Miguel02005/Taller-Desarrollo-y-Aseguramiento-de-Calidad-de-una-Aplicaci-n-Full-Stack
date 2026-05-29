import { useState, useCallback, useEffect } from 'react';
import { getFavorites, addFavorite, removeFavorite } from '../services/favoriteService.js';

export function useFavorites() {
    const [favorites, setFavorites] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const loadFavorites = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await getFavorites();
            setFavorites(data || []);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadFavorites();
    }, [loadFavorites]);

    const add = useCallback(async (bookKey) => {
        try {
            const newFavorite = await addFavorite(bookKey);
            setFavorites(prev => [...prev, newFavorite]);
            return true;
        } catch (err) {
            setError(err.message);
            return false;
        }
    }, []);

    const remove = useCallback(async (id) => {
        try {
            await removeFavorite(id);
            setFavorites(prev => prev.filter(fav => fav.id !== id));
            return true;
        } catch (err) {
            setError(err.message);
            return false;
        }
    }, []);

    const isFavorite = useCallback((bookKey) => {
        return favorites.some(fav => fav.bookKey === bookKey);
    }, [favorites]);

    return {
        favorites,
        loading,
        error,
        addFavorite: add,
        removeFavorite: remove,
        isFavorite,
        refreshFavorites: loadFavorites,
    };
}

export default useFavorites;