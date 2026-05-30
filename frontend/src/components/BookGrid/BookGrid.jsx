import { useEffect, useState } from 'react';
import { useSearch } from '../../context/SearchContext.jsx';
import BookCard from '../BookCard/BookCard.jsx';
import './BookGrid.css';

// TODO: Remove these debug logs after fixing the issue
const DEBUG_MODE = true;

function log(message, data) {
    if (DEBUG_MODE) {
        console.log(`[BookGrid] ${message}`, data);
    }
}

function EmptyIcon() {
    return (
        <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
        </svg>
    );
}

function LoaderIcon() {
    return (
        <div className="book-grid-loader">
            <div className="loader-spinner" />
            <span>Buscando libros...</span>
        </div>
    );
}

function BookStackIcon() {
    return (
        <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
        </svg>
    );
}

function BookGrid() {
    const { books, loading, error, hasSearched } = useSearch();

    const [featuredBooks, setFeaturedBooks] = useState([]);
    const [featuredLoading, setFeaturedLoading] = useState(true);
    const [featuredError, setFeaturedError] = useState(null);

    useEffect(() => {
        if (hasSearched) return;

        const fetchFeaturedBooks = async () => {
            try {
                setFeaturedLoading(true);

                const res = await fetch(
                    'https://openlibrary.org/search.json?q=literatura+latinoamericana&limit=12&lang=spa',
                    {
                        headers: {
                            'User-Agent': 'SmartBookFinder (contacto@ejemplo.com)',
                        },
                    }
                );

                if (!res.ok) {
                    throw new Error('Error al obtener libros');
                }

                const data = await res.json();

                const mapped = data.docs.map((book) => ({
                    id: book.key,
                    workKey: book.key,
                    title: book.title,
                    author: book.author_name?.[0] || 'Autor desconocido',
                    publishYear: book.first_publish_year || null,
                    editions: book.edition_count || 1,
                    coverUrl: book.cover_i
                        ? `https://covers.openlibrary.org/b/id/${book.cover_i}-M.jpg`
                        : null,
                }));

                setFeaturedBooks(mapped);
            } catch (err) {
                setFeaturedError(err.message);
            } finally {
                setFeaturedLoading(false);
            }
        };

        fetchFeaturedBooks();
    }, [hasSearched]);

    if (loading) {
        return (
            <div className="book-grid-container">
                <LoaderIcon />
            </div>
        );
    }

    if (error) {
        return (
            <div className="book-grid-container">
                <div className="book-grid-empty">
                    <div className="empty-state empty-state--error">
                        <div className="empty-icon">
                            <EmptyIcon />
                        </div>
                        <h3>Error en la búsqueda</h3>
                        <p>{error}</p>
                    </div>
                </div>
            </div>
        );
    }

    if (!hasSearched) {
        if (featuredLoading) {
            return (
                <div className="book-grid-container">
                    <LoaderIcon />
                </div>
            );
        }

        if (featuredError) {
            return (
                <div className="book-grid-container">
                    <div className="book-grid-empty">
                        <div className="empty-state empty-state--error">
                            <div className="empty-icon">
                                <EmptyIcon />
                            </div>
                            <h3>Error al cargar</h3>
                            <p>{featuredError}</p>
                        </div>
                    </div>
                </div>
            );
        }

        return (
            <div className="book-grid-container">
                <div className="book-grid-header">
                    <div>
                        <h1 className="book-grid-title">Libros Recomendados</h1>
                        <p className="book-grid-subtitle">
                            Explora nuestra selección de literatura latinoamericana
                        </p>
                    </div>
                </div>

                <div className="books-grid">
                    {featuredBooks.map((book) => (
                        <BookCard
                            key={book.id}
                            workKey={book.workKey}
                            id={book.id}
                            title={book.title}
                            author={book.author}
                            publishYear={book.publishYear}
                            editions={book.editions}
                            coverUrl={book.coverUrl}
                        />
                    ))}
                </div>
            </div>
        );
    }

    log('Books state received:', books);
    log('Books is array:', Array.isArray(books));
    log('Books length:', books ? books.length : 'null/undefined');

    if (books.length === 0) {
        return (
            <div className="book-grid-container">
                <div className="book-grid-empty">
                    <div className="empty-state">
                        <div className="empty-icon">
                            <EmptyIcon />
                        </div>
                        <h3>Sin resultados</h3>
                        <p>No encontramos libros con esos criterios. Intenta con otros términos.</p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="book-grid-container">
            <div className="book-grid-header">
                <div className="book-grid-header-icon">
                    <BookStackIcon />
                </div>
                <div>
                    <h1 className="book-grid-title">Resultados</h1>
                    <p className="book-grid-subtitle">
                        {books.length} {books.length === 1 ? 'libro encontrado' : 'libros encontrados'}
                    </p>
                </div>
            </div>

            <div className="books-grid">
                {books.map((book) => (
                    <BookCard
                        key={book.id}
                        workKey={book.workKey}
                        id={book.id}
                        title={book.title}
                        author={book.author}
                        publishYear={book.publishYear}
                        editions={book.editions}
                        coverUrl={book.coverUrl}
                    />
                ))}
            </div>
        </div>
    );
}

export default BookGrid;