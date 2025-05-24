import { useEffect, useState } from "react";
import "./style.css";
import { api } from "../api";
import SmartSearch from "./SmartSearch";

type Listing = {
  id: string;
  status: string;
  source: string;
  url: string;
  title: string;
  summary: string | null;
  deadlineDate: string;
  budget: string;
};

type Props = {
  filters: {
    category: string[];
    status: string[];
    source: string[];
    budget: string[];
    deadLine: string;
  };
};

function formatEuro(budget: string | null): string {
  if (!budget || isNaN(Number(budget))) return "Not specified";

  const formatted = new Intl.NumberFormat("de-DE", {
    style: "currency",
    currency: "EUR",
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(Number(budget));

  return formatted;
}

const MainContent = ({ filters }: Props) => {
  const [listings, setListings] = useState<Listing[]>([]);
  const [filteredListings, setFilteredListings] = useState<Listing[]>([]);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    api
      .get("/listings/show/all")
      .then((response) => setListings(response.data))
      .catch((error) => console.error("Error fetching listings:", error));
  }, []);

  useEffect(() => {
    const results = listings.filter((listing) => {
      //const matchesCategory = filters.category.length === 0 || filters.category.includes(listing.category);
      const matchesStatus = !filters.status || filters.status.length === 0 || filters.status.includes(listing.status);
      const matchesSource = filters.source.length === 0 || filters.source.includes(listing.source);
      const matchesBudget = filters.budget.length === 0 || filters.budget.includes(listing.budget ?? 'Not specified');
      const matchesDeadline = !filters.deadLine || listing.deadlineDate === filters.deadLine;

      return matchesStatus && matchesBudget && matchesSource && matchesDeadline;
    });
    setFilteredListings(results);
  }, [listings, filters]);

  return (
    <div className="main-content right-shift">
      <div className="content-wrapper">
        
          {/* Dodal SmartSearch komponentu za api */}
          <SmartSearch
            value={searchTerm}
            onChange={setSearchTerm}
          ></SmartSearch>
          {/* Dodal SmartSearch komponentu  za api */}
        
        <div className="results">
          {filteredListings.map((listing) => (
            <div className="result-item" key={listing.id}>
              <h4>{listing.title}</h4>
              <p>
                <strong>Status:</strong> {listing.status}
              </p>
              <p>
                <strong>Deadline:</strong> {listing.deadlineDate}
              </p>
              <p>
                <strong>Summary:</strong>{" "}
                <span className="summary-clamp">
                  {listing.summary ?? "No summary avaliable."}
                </span>
              </p>
              <p>
                <p>
                  <strong>Budget:</strong> {formatEuro(listing.budget)}
                </p>
              </p>
              <p>
                <strong>Source:</strong> {listing.source}
              </p>
              <a href={listing.url} target="_blank" rel="noopener noreferrer">
                View Listing
              </a>
            </div>
          ))}

          {listings.length === 0 && <p>No forthcoming listings found.</p>}
        </div>
      </div>
    </div>
  );
};

export default MainContent;