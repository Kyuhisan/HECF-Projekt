import { useEffect, useState } from 'react';
import './style.css';
import { api } from '../api';

type Listing = {
  id: string;
  status: string;
  source: string;
  url: string;
  title: string;
  summary: string | null;
  deadlineDate: string;
};

const MainContent = () => {
  const [listings, setListings] = useState<Listing[]>([]);

  useEffect(() => {
    api.get<Listing[]>('/listings/show/all')
      .then(res => setListings(res.data))
      .catch(err => console.error("Error fetching listings", err));
  }, []);

  return (
    <div className="main-content right-shift">
      <div className="title">HECF</div>

      <div className="content-wrapper">
        <div className="search-bar">
          <input type="text" placeholder="Search by summary..." />
        </div>

        <div className="results">
          {listings.map((listing) => (
            <div className="result-item" key={listing.id}>
              <h4>{listing.title}</h4>
              <p><strong>Status:</strong> {listing.status}</p>
              <p><strong>Deadline:</strong> {listing.deadlineDate}</p>
              <p><strong>Summary:</strong> {listing.summary}</p>
              <p><strong>Source:</strong> {listing.source}</p>
              <a href={listing.url} target="_blank" rel="noopener noreferrer">View Listing</a>
            </div>
          ))}

          {listings.length === 0 && (
            <p>No forthcoming listings found.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default MainContent;
