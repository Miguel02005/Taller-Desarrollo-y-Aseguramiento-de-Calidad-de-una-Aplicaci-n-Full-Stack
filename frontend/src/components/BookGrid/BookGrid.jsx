import { useEffect, useState } from 'react';
import { useSearch } from '../../context/SearchContext.jsx';
import BookCard from '../BookCard/BookCard.jsx';
import './BookGrid.css';

function BookGrid() {
    const { books, loading, error, hasSearched } = useSearch();

    const [featuredBooks, setFeaturedBooks] = useState([]);
    const [featuredLoading, setFeaturedLoading] = useState(true);
    const [featuredError, setFeaturedError] = useState(null);

    // Libros por defecto cuando no se ha buscado
    useEffect(() => {
        if (hasSearched) return;

        const fetchFeaturedBooks = async () => {
            try {
                setFeaturedLoading(true);

                const res = await fetch(
                    'https://openlibrary.org/search.json?q=literatura+latinoamericana&limit=12&lang=spa',
                    {
                        headers: {
                            'User-Agent':
                                'SmartBookFinder (contacto@ejemplo.com)',
                        },
                    }
                );

                if (!res.ok) {
                    throw new Error(
                        'Error al obtener libros destacados'
                    );
                }

                const data = await res.json();

                const mapped = data.docs.map((book) => ({
                    id: book.key,

                    workKey: book.key,

                    title: book.title,

                    author:
                        book.author_name?.[0] ||
                        'Autor desconocido',

                    publishYear:
                        book.first_publish_year || '—',

                    editions:
                        book.edition_count || 1,

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

    // =========================================
    // LIBROS POR DEFECTO
    // =========================================

    if (!hasSearched) {
        if (featuredLoading) {
            return (
                <p className="grid-status">
                    Cargando libros recomendados...
                </p>
            );
        }

        if (featuredError) {
            return (
                <div className="books-grid-error">
                    <p className="grid-status grid-error">
                        Error: {featuredError}
                    </p>
                </div>
            );
        }

        return (
            <div>
                <div className="grid-header">
                    <h2>Libros Recomendados</h2>

                    <p>
                        Explora algunos libros populares mientras
                        realizas una búsqueda.
                    </p>
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

    // =========================================
    // BÚSQUEDAS NORMALES
    // =========================================

    if (loading) {
        return (
            <p className="grid-status">
                Buscando libros...
            </p>
        );
    }

    if (error) {
        return (
            <div className="books-grid-error">
                <p className="grid-status grid-error">
                    Error: {error}
                </p>
            </div>
        );
    }

    if (books.length === 0) {
        return (
            <div className="books-grid-empty">
                <div className="empty-state">
                    <span className="empty-icon">🔍</span>

                    <h3>Sin resultados</h3>

                    <p>
                        No se encontraron libros con los
                        criterios de búsqueda.
                    </p>
                </div>
            </div>
        );
    }

    return (
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
    );
}

export default BookGrid;