import { useState } from 'react';
import { useSearch } from '../../context/SearchContext.jsx';
import './Search_Style.css';

const LANGUAGE_MAP = {
    'Spanish': 'spanish',
    'English': 'english',
    'French': 'french',
    'German': 'german',
};

export default function SearchCard() {
    const { performSearch, loading, error } = useSearch();

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [language, setLanguage] = useState('Spanish');
    const [year, setYear] = useState('');
    const [localError, setLocalError] = useState(null);

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

    const displayError = localError || error;

    return (
        <article className="search-card">
            <div className="search-card_header">
                <h2>Búsqueda Avanzada</h2>
                <p>Filtra libros por título, autor, idioma y año de publicación.</p>
            </div>

            <form className="search-card_form" onSubmit={handleSubmit}>
                <label className="search-card_label" htmlFor="book-title">
                    Título del Libro
                    <input
                        id="book-title"
                        className="search-card_input"
                        type="text"
                        placeholder="Ej: El Quijote"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        disabled={loading}
                    />
                </label>

                <label className="search-card_label" htmlFor="book-author">
                    Autor
                    <input
                        id="book-author"
                        className="search-card_input"
                        type="text"
                        placeholder="Ej: Cervantes"
                        value={author}
                        onChange={(e) => setAuthor(e.target.value)}
                        disabled={loading}
                    />
                </label>

                <label className="search-card_label" htmlFor="book-language">
                    Idioma
                    <select
                        id="book-language"
                        className="search-card_select"
                        value={language}
                        onChange={(e) => setLanguage(e.target.value)}
                        disabled={loading}
                    >
                        <option value="Spanish">Spanish</option>
                        <option value="English">English</option>
                        <option value="French">French</option>
                        <option value="German">German</option>
                    </select>
                </label>

                <label className="search-card_label" htmlFor="book-year">
                    Publicado después de
                    <input
                        id="book-year"
                        className="search-card_input"
                        type="number"
                        min="0"
                        max={new Date().getFullYear()}
                        placeholder="Año"
                        value={year}
                        onChange={(e) => setYear(e.target.value)}
                        disabled={loading}
                    />
                </label>

                {displayError && (
                    <div className="search-error">{displayError}</div>
                )}

                <button type="submit" className="search-card_button" disabled={loading}>
                    {loading ? 'Buscando...' : 'Search Books'}
                </button>
            </form>
        </article>
    );
}