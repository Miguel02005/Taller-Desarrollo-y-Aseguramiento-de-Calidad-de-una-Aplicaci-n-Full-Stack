import './History_Style.css'

const recentItems = [
  'Gabriel García Márquez',
  'Realismo Mágico',
  '1984 George Orwell',
]

export default function HistoryCard() {
  return (
    <aside className="history-card">
      <div className="history-card__header">
        <span className="history-card__icon">⟳</span>
        <div>
          <h3>Búsquedas Recientes</h3>
        </div>
      </div>

      <ul className="history-card__list">
        {recentItems.map((item) => (
          <li key={item} className="history-card__item">
            {item}
          </li>
        ))}
      </ul>
    </aside>
  )
}
