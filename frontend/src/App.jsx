import './App.css';
import { SearchProvider } from './context/SearchContext.jsx';
import { FavoritesProvider } from './context/FavoritesContext.jsx';
import SmartBook from './components/Title_Component/Title_Card.jsx';
import SearchCard from './components/Search_Component/Search_Card.jsx';
import BookGrid from './components/BookGrid/BookGrid.jsx';
import FavoritesPanel from './components/Favorites_Component/FavoritesPanel.jsx';

function App() {
    return (
        <SearchProvider>
            <FavoritesProvider>
                <div className="layout">
                    <header className="site-header">
                        <SmartBook />
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

                    <FavoritesPanel />

                    <footer className="site-footer">
                        <small>© 2026 Smart Book Finder</small>
                    </footer>
                </div>
            </FavoritesProvider>
        </SearchProvider>
    );
}

export default App;