import { useEffect, useState } from 'react';
import './style.css';
import { api } from '../api';

type Listing = {
  reference: string;
  identifier: string;
  title: string;
  summary: string | null;
  url: string;
  descriptionByte: string;
}

const MainContent = () => {
    const [listings, setListings] = useState<Listing[]>([]);

    useEffect(() => {
        api.get<Listing[]>('/listings/show/forthcoming')
        .then(res => setListings(res.data))
        .catch(err => console.error("Error fetching listings", err));
    }, []);

    return (
        <div className="main-content right-shift">
            <div className="title">HECF</div>

            <div className="content-wrapper">
                <div className="search-bar">
                    <input type="text" placeholder="Search by description..." />
                </div>

                <div className="results">
                    {listings.map((listing) => (
                        <div className="result-item" key={listing.reference}>
                            <h4>{listing.title}</h4>
                            <p><strong>Identifier:</strong> {listing.identifier}</p>
                            {listing.summary && <p>{listing.summary}</p>}
                            <p><strong>Description:</strong> {listing.descriptionByte.slice(0, 300)}...</p>
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

export default MainContent