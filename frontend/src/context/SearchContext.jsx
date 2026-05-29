import { createContext, useContext, useState, useCallback } from 'react';
import { searchBooks } from '../services/bookService.js';

const SearchContext = createContext(null);

export function SearchProvider({ children }) {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [hasSearched, setHasSearched] = useState(false);

    const performSearch = useCallback(async (searchParams) => {
        setLoading(true);
        setError(null);

        try {
            const results = await searchBooks(searchParams);
            setBooks(results || []);
            setHasSearched(true);
        } catch (err) {
            setError(err.message);
            setBooks([]);
            setHasSearched(true);
        } finally {
            setLoading(false);
        }
    }, []);

    const clearSearch = useCallback(() => {
        setBooks([]);
        setError(null);
        setHasSearched(false);
    }, []);

    const value = {
        books,
        loading,
        error,
        hasSearched,
        performSearch,
        clearSearch,
    };

    return (
        <SearchContext.Provider value={value}>
            {children}
        </SearchContext.Provider>
    );
}

export function useSearch() {
    const context = useContext(SearchContext);
    if (!context) {
        throw new Error('useSearch must be used within a SearchProvider');
    }
    return context;
}

export default SearchContext;