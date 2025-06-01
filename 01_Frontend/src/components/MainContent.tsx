import { useEffect, useState } from "react";
import "./style.css";
import SmartSearch from "./SmartSearch";
import type { Listing } from "../App";
import { set } from "lodash";
import { CalendarClock, HandCoins, CodeXml,  KeyRound, Lock, LayoutGrid, Rows2,SearchX,Hourglass } from "lucide-react";

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
  const [viewMode, setViewMode] = useState<"grid" | "list">("list");
  const [filteredListings, setFilteredListings] = useState<Listing[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [searchKeywords, setSearchKeywords] = useState<string[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [allKeywords, setAllKeywords] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const itemsPerPage = 15;
  const resetPage = () => setCurrentPage(1);

  useEffect(() => {
      if (listings.length === 0) {
    setIsLoading(true);
    setFilteredListings([]);
    return;
  }
  setIsLoading(false);
    const results = listings.filter((listing) => {
      const matchesIndustries = filters.industries.length === 0 || (listing.industries !== null && filters.industries?.every((ind) => listing.industries?.includes(ind)));
      const matchesStatus = !filters.status || filters.status.length === 0 || filters.status.includes(listing.status);
      const matchesSource = filters.source.length === 0 || filters.source.includes(listing.source);
      const listingBudget = Number(listing.budget);
      const matchesBudget = isNaN(listingBudget) || listingBudget <= filters.budget;
      const matchesDeadline = !filters.deadLine || listing.deadlineDate === filters.deadLine;

      const lowerDescription = listing.description?.toLowerCase() ?? "";
      const lowerTechnologies = (listing.technologies ?? []).map((t) => t.toLowerCase());
      const lowerIndustries = (listing.industries ?? []).map((i) => i.toLowerCase());
      const activeKeywords = searchKeywords.length > 0 ? searchKeywords : allKeywords;

      const keywordMatch = activeKeywords.length === 0 || activeKeywords.some((k) => {
  const kw = k.toLowerCase();
  return (
    lowerDescription.includes(" " + kw + " ") ||
    lowerTechnologies.includes(" " + kw + " ") ||
    lowerIndustries.includes(" " + kw + " ") ||
    lowerTechnologies.includes(kw) ||
    lowerIndustries.includes(kw)
  );
});

      return (
        matchesIndustries &&
        matchesStatus &&
        matchesBudget &&
        matchesSource &&
        matchesDeadline &&
        keywordMatch
      );
    });
    setFilteredListings(results);
  }, [listings, filters, searchKeywords, allKeywords]);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }, [currentPage]);

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentListings = filteredListings.slice(
    indexOfFirstItem,
    indexOfLastItem
  );

  return (
    <div className="main-content right-shift">
      <div className="content-wrapper">
        {/* Gumb za preklop med prikazom grid ali row */}
          <div className="view-toggle-buttons">
            <button 
              className={`toggle-button ${viewMode === "grid" ? "active" : ""}`} 
              onClick={() => setViewMode("grid")} 
              title="Grid view">
              <LayoutGrid />
            </button>
            <button 
              className={`toggle-button ${viewMode === "list" ? "active" : ""}`} 
              onClick={() => setViewMode("list")} 
              title="List view">
              <Rows2 />
            </button>
          </div>

        {/* Dodal SmartSearch komponento za api */}
        <SmartSearch
          value={searchTerm}
          onChange={setSearchTerm}
          onKeywordsChange={setSearchKeywords}
          onAllKeywordsChange={setAllKeywords}
           onInteraction={resetPage}
        ></SmartSearch>
        {/* Dodal SmartSearch komponento za api */}

        <div className={`results ${viewMode === "grid" ? "grid-view" : "list-view"}`}>
          
          {currentListings.map((listing) => (
            <div className="result-item" key={listing.id}>
              
              {/* Header z naslovom in statusom/datumom */}
              <div className="card-header">
                <h4 className="card-title">{listing.title}</h4>
                <div className="card-status-info">
                  <span
                    className={`status-badge ${listing.status.toLowerCase()}`}
                  >
                    {listing.status}
                  </span>
                  <span className="deadline-date">
                    <CalendarClock size={15} /> {listing.deadlineDate}
                  </span>
                </div>
              </div>

              {/* Glavna vsebina */}
              <div className="card-content">
                <p className="card-summary">
                  <span className="summary-clamp">
                    {listing.summary
                      ? listing.summary.length > 270
                        ? listing.summary.substring(0, 270) + "..."
                        : listing.summary
                      : "No summary available."}
                  </span>
                </p>

                {/* Industries in Technologies */}
                <div className="card-tags">
                  {listing.industries && listing.industries.length > 0 && (
                    <div className="tag-section">
                      <span className="tag-section-title">Industries:</span>
                      <div className="industries-tags">
                        {listing.industries.map((industry, index) => (
                          <span
                            key={index}
                            className={`tag-badge industry-tag industry-${
                              index % 3
                            }`}
                          >
                            <KeyRound size={14} /> {industry} 
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                  {listing.technologies && listing.technologies.length > 0 && (
                    <div className="tag-section">
                      <span className="tag-section-title">Technologies:</span>
                      <div className="technologies-tags">
                        {listing.technologies.map((tech, index) => (
                          <span
                            key={index}
                            className={`tag-badge tech-tag tech-${index % 3}`}
                          >
                          <KeyRound size={14} /> {tech.length > 120 ? tech.substring(0, 120) + "..." : tech} 
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                </div>

                {/* Budget */}
                <div className="card-budget">
                  <strong>
                    <HandCoins /> {formatEuro(listing.budget)}
                  </strong>
                </div>
              </div>

              {/* Footer z source in gumbom */}
              <div className="card-footer">
                <span className="card-source">
                  <CodeXml size={16} /> {listing.source}
                </span>
                <button className="view-listings-button">
                  <a
                    href={listing.url}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    View Listing
                  </a>
                </button>
              </div>
            </div>
          ))}

          
        </div>
       
        {isLoading ? (
          
  <div className="no-results">
    <div className="loading-dots">
    <span></span>
    <span></span>
    <span></span>
  </div>
  <p>Loading listings...</p>
</div>
) : currentListings.length === 0 ? (
  <div className="no-results">
    <SearchX size={24} />
    <p>No listings found matching your criteria.</p>
  </div>
) : null}

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
