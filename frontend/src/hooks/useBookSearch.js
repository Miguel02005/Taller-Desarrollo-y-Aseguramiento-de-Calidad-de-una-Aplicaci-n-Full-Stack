import { useState, useCallback } from 'react';
import { searchBooks } from '../services/bookService.js';

export function useBookSearch() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const performSearch = useCallback(async (searchParams) => {
        setLoading(true);
        setError(null);

        try {
            const results = await searchBooks(searchParams);
            setBooks(results || []);
        } catch (err) {
            setError(err.message);
            setBooks([]);
        } finally {
            setLoading(false);
        }
    }, []);

    const clearSearch = useCallback(() => {
        setBooks([]);
        setError(null);
    }, []);

    return {
        books,
        loading,
        error,
        performSearch,
        clearSearch,
    };
}

export default useBookSearch;