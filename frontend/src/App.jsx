import './App.css';
import { useState } from 'react';
import { SearchProvider } from './context/SearchContext.jsx';
import { FavoritesProvider, useFavorites } from './context/FavoritesContext.jsx';
import SmartBook from './components/Title_Component/Title_Card.jsx';
import SearchCard from './components/Search_Component/Search_Card.jsx';
import BookGrid from './components/BookGrid/BookGrid.jsx';
import FavoritesPanel from './components/Favorites_Component/FavoritesPanel.jsx';

function FavoritesButton() {
    const { favorites } = useFavorites();
    const [showPanel, setShowPanel] = useState(false);

    const count = favorites.length;

    return (
        <>
            <button
                className="header-fav-btn"
                onClick={() => setShowPanel(!showPanel)}
                title="Mis favoritos"
            >
                <svg width="20" height="20" viewBox="0 0 24 24" fill={count > 0 ? "#ef4444" : "none"} stroke={count > 0 ? "#ef4444" : "currentColor"} strokeWidth="2">
                    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
                </svg>
                {count > 0 && <span className="fav-badge">{count}</span>}
            </button>

            {showPanel && <FavoritesPanel onClose={() => setShowPanel(false)} />}
        </>
    );
}

function AppContent() {
    return (
        <div className="layout">
            <header className="site-header">
                <div className="header-content">
                    <div className="header-left">
                        <SmartBook />
                    </div>
                    <div className="header-right">
                        <FavoritesButton />
                    </div>
                </div>
            </header>

            <div className="site-body">
                <aside className="sidebar">
                    <div className="sidebar-content">
                        <SearchCard />
                    </div>
                    <div className="sidebar-bottom-space" />
                </aside>

                <main className="main">
                    <section className="content">
                        <BookGrid />
                    </section>
                </main>
            </div>

            <footer className="site-footer">
                <small>© 2026 Smart Book Finder</small>
            </footer>
        </div>
    );
}

function App() {
    return (
        <SearchProvider>
            <FavoritesProvider>
                <AppContent />
            </FavoritesProvider>
        </SearchProvider>
    );
}

export default App;