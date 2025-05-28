import { useEffect, useState } from "react";
import "./style.css";
import SmartSearch from "./SmartSearch";
import type { Listing } from "../App";
import { set } from "lodash";

type Props = {
  filters: {
    industries: string[];
    status: string[];
    source: string[];
    budget: number;
    deadLine: string;
  };
  listings: Listing[];
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

const MainContent = ({ filters, listings }: Props) => {
  const [filteredListings, setFilteredListings] = useState<Listing[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [searchKeywords, setSearchKeywords] = useState<string[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 15; 

  useEffect(() => {
    const results = listings.filter((listing) => {
      const matchesIndustries = filters.industries.length === 0 || (listing.industries !== null && filters.industries?.every((ind) => listing.industries?.includes(ind)));
      const matchesStatus = !filters.status || filters.status.length === 0 || filters.status.includes(listing.status);
      const matchesSource = filters.source.length === 0 || filters.source.includes(listing.source);
      const listingBudget = Number(listing.budget);
      const matchesBudget = isNaN(listingBudget) || listingBudget <= filters.budget;
      const matchesDeadline = !filters.deadLine || listing.deadlineDate === filters.deadLine;

      const lowerDescription = listing.description?.toLowerCase() ?? "";
      const lowerTechnologies = (listing.technologies ?? []).map(t => t.toLowerCase());
      const lowerIndustries = (listing.industries ?? []).map(i => i.toLowerCase());
      const keywordMatch = searchKeywords.length === 0 || searchKeywords.some(k => {
        const kw = k.toLowerCase();
        return lowerDescription.includes(" "+kw +" ") || lowerTechnologies.includes(" "+kw +" ") || lowerIndustries.includes(" "+kw +" ");
      });

      return matchesIndustries && matchesStatus && matchesBudget && matchesSource && matchesDeadline && keywordMatch;
    });
    setFilteredListings(results);
  }, [listings, filters,searchKeywords]);

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentListings = filteredListings.slice(indexOfFirstItem, indexOfLastItem);

  return (
    <div className="main-content right-shift">
      <div className="content-wrapper">
        
          {/* Dodal SmartSearch komponento za api */}
          <SmartSearch
            value={searchTerm}
            onChange={setSearchTerm}
            onKeywordsChange={setSearchKeywords}
          ></SmartSearch>
          {/* Dodal SmartSearch komponento za api */}
        
        <div className="results">
          {currentListings.map((listing) => (
            <div className="result-item" key={listing.id}>
              <h4>{listing.title}</h4>
              <p><strong>Status:</strong> {listing.status}</p>
              <p><strong>Deadline:</strong> {listing.deadlineDate}</p>
              <p><strong>Summary:</strong>{" "}
                <span className="summary-clamp">
                  {listing.summary ?? "No summary avaliable."}
                </span>
              </p>
              <p><strong>Budget:</strong> {formatEuro(listing.budget)}</p>
              <p><strong>Source:</strong> {listing.source}</p>

              <p><strong>Industries:</strong> {listing.industries?.join(", ") || "Not specified"}</p>
              <p><strong>Technologies:</strong> {listing.technologies?.join(", ") || "Not specified"}</p>
              <button className="view-listings-button">
                <a href={listing.url} target="_blank" rel="noopener noreferrer">View Listing</a>
              </button>
            </div>
          ))}

          {listings.length === 0 && <p className="spinner"></p>}
        </div>

        {/*Gumb za paging --> max 15*/}
        {filteredListings.length > itemsPerPage && (
          <div className="pagination">
            <button
              className="pagination-button"
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => prev - 1)}
            >
              Previous
            </button>
            <span>Page {currentPage}</span>
            <button 
              className="pagination-button"
              disabled={indexOfLastItem >= filteredListings.length}
              onClick={() => setCurrentPage((prev) => prev + 1)}
            >
              Next
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default MainContent;