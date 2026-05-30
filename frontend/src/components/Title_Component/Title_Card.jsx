import './Title_Style.css';

export default function SmartBook() {
  return (
    <div className="title-brand">
      <span className="title-icon">
        <img
          src="/Logo.png"
          alt="Smart Book Finder Logo"
          className="title-logo"
        />
      </span>

      <span className="title-brand__text">
        Smart Book Finder
      </span>
    </div>
  );
}