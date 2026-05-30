import { useState } from 'react';
import { useSearch } from '../../context/SearchContext.jsx';
import './Search_Style.css';

const LANGUAGE_MAP = {
    'Spanish': 'spanish',
    'English': 'english',
    'French': 'french',
    'German': 'german',
};

// Iconos SVG
function SearchIcon() {
    return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
        </svg>
    );
}

function LoaderIcon() {
    return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="spinner">
            <circle cx="12" cy="12" r="10" strokeOpacity="0.25" />
            <path d="M12 2a10 10 0 0 1 10 10" strokeLinecap="round" />
        </svg>
    );
}

export default function SearchCard() {
    const { performSearch, loading, error } = useSearch();

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [language, setLanguage] = useState('Spanish');
    const [year, setYear] = useState('');
    const [localError, setLocalError] = useState(null);
    const [filtersOpen, setFiltersOpen] = useState(true);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLocalError(null);

        if (!title.trim() && !author.trim()) {
            setLocalError('Ingresa al menos título o autor');
            return;
        }

        if (year && (parseInt(year) < 0 || parseInt(year) > new Date().getFullYear())) {
            setLocalError('Año inválido');
            return;
        }

        const searchParams = {
            title: title.trim() || undefined,
            author: author.trim() || undefined,
            language: LANGUAGE_MAP[language] || language.toLowerCase(),
            publishedAfter: year ? parseInt(year) : undefined,
        };

        const cleanParams = Object.fromEntries(
            Object.entries(searchParams).filter(([_, v]) => v !== undefined)
        );

        try {
            await performSearch(cleanParams);
        } catch (err) {
            setLocalError(err.message);
        }
    };

    const handleClear = () => {
        setTitle('');
        setAuthor('');
        setLanguage('Spanish');
        setYear('');
        setLocalError(null);
    };

    const displayError = localError || error;

    return (
        <div className="search-container">
            <div className="search-header" onClick={() => setFiltersOpen(!filtersOpen)}>
                <div className="search-header-title">
                    <SearchIcon />
                    <h2>Buscar Libros</h2>
                </div>
                <button className="search-toggle-btn" type="button">
                    <svg
                        width="16"
                        height="16"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                        style={{ transform: filtersOpen ? 'rotate(180deg)' : 'rotate(0deg)', transition: 'transform 0.2s' }}
                    >
                        <polyline points="6 9 12 15 18 9" />
                    </svg>
                </button>
            </div>

            {filtersOpen && (
                <form className="search-form" onSubmit={handleSubmit}>
                    <div className="search-inputs">
                        <div className="search-field">
                            <label htmlFor="book-title">Título</label>
                            <input
                                id="book-title"
                                type="text"
                                placeholder="Ej: Harry Potter"
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                                disabled={loading}
                                className="search-input"
                            />
                        </div>

                        <div className="search-field">
                            <label htmlFor="book-author">Autor</label>
                            <input
                                id="book-author"
                                type="text"
                                placeholder="Ej: J.K. Rowling"
                                value={author}
                                onChange={(e) => setAuthor(e.target.value)}
                                disabled={loading}
                                className="search-input"
                            />
                        </div>
                    </div>

                    <div className="search-filters">
                        <div className="search-field search-field--small">
                            <label htmlFor="book-language">Idioma</label>
                            <select
                                id="book-language"
                                value={language}
                                onChange={(e) => setLanguage(e.target.value)}
                                disabled={loading}
                                className="search-select"
                            >
                                <option value="Spanish">Español</option>
                                <option value="English">English</option>
                                <option value="French">Français</option>
                                <option value="German">Deutsch</option>
                            </select>
                        </div>

                        <div className="search-field search-field--small">
                            <label htmlFor="book-year">Año mínimo</label>
                            <input
                                id="book-year"
                                type="number"
                                min="0"
                                max={new Date().getFullYear()}
                                placeholder="Ej: 2000"
                                value={year}
                                onChange={(e) => setYear(e.target.value)}
                                disabled={loading}
                                className="search-input"
                            />
                        </div>
                    </div>

                    {displayError && (
                        <div className="search-error">{displayError}</div>
                    )}

                    <div className="search-actions">
                        <button
                            type="submit"
                            className="search-btn search-btn--primary"
                            disabled={loading}
                        >
                            {loading ? (
                                <>
                                    <LoaderIcon />
                                    <span>Buscando...</span>
                                </>
                            ) : (
                                <>
                                    <SearchIcon />
                                    <span>Buscar</span>
                                </>
                            )}
                        </button>

                        <button
                            type="button"
                            className="search-btn search-btn--secondary"
                            onClick={handleClear}
                            disabled={loading}
                        >
                            Limpiar
                        </button>
                    </div>
                </form>
            )}
        </div>
    );
}