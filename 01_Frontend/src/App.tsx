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
  deadlineDate: string;
  budget: string;
  technologies: string[];
};

function App() {
  const [filters, setFilters] = useState({
    status: [] as string[],
    source: [] as string[],
    technologies: [] as string[],
    budget: 100000000,
    deadLine: "",
  });

  const [listings, setListings] = useState([]);

  useEffect(() => {
    api
      .get("/listings/show/all")
      .then((response) => setListings(response.data))
      .catch((error) => console.error("Error fetching listings:", error));
  }, []);

  return (
    <div className="layout">
      <Sidebar filters={filters} onFilterChange={setFilters}/>
      <MainContent filters={filters} listings={listings}/>
    </div>
  );
}

export default App;
