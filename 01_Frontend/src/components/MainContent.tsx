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
  price: string;
};

type Props = {
  filters: {
    category: string[];
    status: string[];
    source: string[];
    price: string[];
    deadLine: string;
  };
};

const MainContent = ({ filters }: Props) => {
  const [listings, setListings] = useState<Listing[]>([]);
  const [filteredListings, setFilteredListings] = useState<Listing[]>([]);

  useEffect(() => {
    api.get('/listings/show/all')
      .then((response) => setListings(response.data))
      .catch((error) => console.error('Error fetching listings:', error));
  }, []);

  useEffect(() => {
    const  results = listings.filter((listing) => {
      //const matchesCategory = filters.category.length === 0 || filters.category.includes(listing.category);
      const matchesStatus = filters.status.length === 0 || filters.status.includes(listing.status);
      const matchesSource = filters.source.length === 0 || filters.source.includes(listing.source);
      //const matchesPrice = filters.price.length === 0 || filters.price.includes(listing.price);
      const matchesDeadline = !filters.deadLine || listing.deadlineDate === filters.deadLine;

      return matchesStatus && matchesSource && matchesDeadline;
    });
    setFilteredListings(results);
  }, [listings, filters]);

  return (
    <div className="main-content right-shift">
      <div className="title">HECF-SmartSearch</div>

      <div className="content-wrapper">
        <div className="search-bar">
          <input type="text" placeholder="Search by summary..." />
        </div>

        <div className="results">
          {filteredListings.map((listing) => (
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
