import "./components/style.css";
import Sidebar from "./components/Sidebar";
import MainContent from "./components/MainContent";
import { useEffect, useState } from "react";
import { api } from "./api";

export interface Listing {
  id: string;
  status: string;
  source: string;
  url: string;
  title: string;
  summary: string | null;
   description: string | null;
  deadlineDate: string;
  budget: string;
  industries: string[] | null;
   technologies: string[] | null;
};

function App() {
  const [filters, setFilters] = useState({
    status: [] as string[],
    source: [] as string[],
    industries: [] as string[],
    budget: { min: 0, max: 100000000 },
    deadLine: "",
  });

  const [listings, setListings] = useState<Listing[]>([]);

  const allIndustries = listings
    .map(l => l.industries)
    .filter((ind): ind is string[] => Array.isArray(ind))
    .flat();

  const uniqueIndustries = Array.from(new Set(allIndustries));  

  useEffect(() => {
    api
      .get("/listings/show/all")
      .then((response) => setListings(response.data))
      .catch((error) => console.error("Error fetching listings:", error));
  }, []);

  return (
    <div className="layout">
      <Sidebar filters={filters} onFilterChange={setFilters} industries={uniqueIndustries}/>
      <MainContent filters={filters} listings={listings}/>
    </div>
  );
}

export default App;
