import "./components/style.css";
import Sidebar from "./components/Sidebar";
import MainContent from "./components/MainContent";
import { useState } from "react";

function App() {
  const [filters, setFilters] = useState({
    category: [] as string[],
    status: [] as string[],
    source: [] as string[],
    price: [] as string[],
    deadLine: "",
  });

  const handleFilterChange = (updateFilters: typeof filters) => {
    setFilters(updateFilters);
  };

  return (
    <div className="layout">
      <Sidebar filters={filters} onFilterChange={setFilters}/>
      <MainContent filters={filters}/>
    </div>
  );
}

export default App;
