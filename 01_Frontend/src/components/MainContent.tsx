import './style.css';

const MainContent = () => {
    return (
        <div className="main-content right-shift">
      <div className="title">HECF</div>
      
      <div className="content-wrapper">
        <div className="search-bar">
          <input type="text" placeholder="Search by description..." />
        </div>

        <div className="results">
          <div className="result-item">
            <h4>Example Item 1</h4>
            <p>This is a description of the first item. It falls under Technology and is In Stock.</p>
          </div>
          <div className="result-item">
            <h4>Example Item 2</h4>
            <p>This one belongs to the Art category and is currently Out of Stock.</p>
          </div>
        </div>
      </div>
    </div>
    );
};

export default MainContent