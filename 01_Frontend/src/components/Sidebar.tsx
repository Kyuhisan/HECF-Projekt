import "./style.css";
import { useState } from "react";
import {
  CalendarClock,
  HandCoins,
  CodeXml,
  KeyRound,
  SlidersHorizontal,
  Factory,
   Eye
} from "lucide-react";
import { filter } from "lodash";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


type Props = {
  filters: {
    industries: string[];
    status: string[];
    source: string[];
    budget: number;
    deadLine: string;
  };
  onFilterChange: (filters: Props["filters"]) => void;
  industries: string[];
};

const filterOption = {
  status: ["Forthcoming", "Open", "Closed"],
  source: ["ec.europa.eu", "getonepass.eu", "cascadefunding.eu"],
};

const Sidebar = ({ filters, onFilterChange, industries }: Props) => {
  const [openDropdowns, setOpenDropdowns] = useState<Record<string, boolean>>(
    {}
  );
  const [industrySearchTerm, setIndustrySearchTerm] = useState("");

  const toggle = (key: string) => {
    setOpenDropdowns((prev) => ({
      ...prev,
      [key]: !prev[key],
    }));
  };

  const handleCheckboxChange = (
    group: keyof Props["filters"],
    value: string
  ) => {
    if (group === "deadLine") return;

    const groupValues = filters[group] as string[];
    const updateValues = groupValues.includes(value)
      ? groupValues.filter((v) => v !== value)
      : [...groupValues, value];

    onFilterChange({ ...filters, [group]: updateValues });
  };

  const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const isoDate = event.target.value;
    const [year, month, day] = isoDate.split("-");
    const formatted = `${day}/${month}/${year}`;
    onFilterChange({ ...filters, deadLine: formatted });
  };

  // Razpored industrij po abecedi
  const sortedIndustries = [...industries].sort((a, b) =>
    a.localeCompare(b, undefined, { sensitivity: "base" })
  );

  // Filtriranje industrij glede na iskalni niz
  const filteredIndustries = sortedIndustries.filter((industry) =>
    industry.toLowerCase().includes(industrySearchTerm.toLowerCase())
  );

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h2>
          <SlidersHorizontal /> Filters
        </h2>
      </div>

      {/* Industries dinamicni filter */}
      <div className="filter-group" key="industries">
        <div className="dropdown-wrapper">
          <div className="dropdown-header" onClick={() => toggle("industries")}>
            <Factory className="filter-icon" size={16} /> Industries{" "}
          </div>

          {openDropdowns["industries"] && (
            <div className="dropdown-content">
              <input
                type="text"
                placeholder="Search industries..."
                className="industry-dropdown-search"
                value={industrySearchTerm}
                onChange={(e) => setIndustrySearchTerm(e.target.value)}
              />
              {filteredIndustries.map((industry) => (
                <label key={industry}>
                  <input
                    type="checkbox"
                    checked={filters.industries.includes(industry)}
                    onChange={() => handleCheckboxChange("industries", industry)}
                  />
                  {industry}
                </label>
              ))}
            </div>
          )}
        </div>
      </div>

      {Object.entries(filterOption).map(([key, values]) => (
        <div className="filter-group" key={key}>
          <div className="dropdown-wrapper">
            <div className="dropdown-header" onClick={() => toggle(key)}>
              {key === "status" && (
                <>
                  <Eye size={16} className="filter-icon" /> Status
                </>
              )}
              {key === "source" && (
                <>
                  <CodeXml className="filter-icon" size={16} />  Source
                </>
              )}
            </div>

            {openDropdowns[key] && (
              <div className="dropdown-content">
                {values.map((value) => {
                  const getStatusClass = (status: string) => {
                    switch (status.toLowerCase()) {
                      case "closed":
                        return "status-closed";
                      case "open":
                        return "status-open";
                      case "forthcoming":
                        return "status-forthcoming";
                      default:
                        return "";
                    }
                  };

                  return (
                    <label key={value} className="checkbox-label">
                      <input
                        type="checkbox"
                        checked={(filters as any)[key].includes(value)}
                        onChange={() =>
                          handleCheckboxChange(key as keyof Props["filters"], value)
                        }
                      />
                      <span className={key === "status" ? getStatusClass(value) : ""}>
                        {value}
                      </span>
                    </label>
                  );
                })}
              </div>
            )}
          </div>
        </div>
        ))}

      {/* Budget slider */}
      <div className="filter-group">
        <div className="slider-container">
          <div className="slider-header">
            <div className="slider-title">
              <HandCoins />
              <b>Budget</b>
            </div>
          </div>

          <div className="slider-wrapper">
            <div className="slider-track">
              <div
                className="slider-fill"
                style={{ width: `${(filters.budget / 100000000) * 100}%` }}
              ></div>
              <div
                className="slider-thumb"
                style={{ left: `${(filters.budget / 100000000) * 100}%` }}
              ></div>
            </div>

            <input
              type="range"
              min={0}
              max={100000000}
              step={10000}
              value={filters.budget || 0}
              onChange={(e) =>
                onFilterChange({ ...filters, budget: Number(e.target.value) })
              }
              className="hidden-slider"
            />

            <div className="slider-ticks">
              <div className="tick"></div>
              <div className="tick"></div>
              <div className="tick"></div>
              <div className="tick"></div>
              <div className="tick"></div>
              <div className="tick"></div>
              <div className="tick"></div>
            </div>

            <div className="slider-labels">
              <span>0€</span>
              <span>50M€</span>
              <span>100M€</span>
            </div>

            <div className="budget-display">
              {filters.budget.toLocaleString()}€
            </div>
          </div>
        </div>
      </div>

      {/* Datum obravnava za filter */}
      <div className="filter-group">
        <div className="slider-container">
          <label className="slider-title">
            {" "}
            <CalendarClock size={20}/> <b>Deadline</b>
          </label>
          <br />
          <DatePicker
            selected={
              filters.deadLine
                ? new Date(filters.deadLine.split("/").reverse().join("-"))
                : null
            }
            onChange={(date: Date | null) => {
              if (date) {
                const formatted = `${date.getDate().toString().padStart(2, '0')}/${(
                  date.getMonth() + 1
                ).toString().padStart(2, '0')}/${date.getFullYear()}`;
                onFilterChange({ ...filters, deadLine: formatted });
              }
            }}
            dateFormat="dd/MM/yyyy"
            placeholderText="dd/mm/yyyy"
            className="slider-title"
          />

        </div>
      </div>

      {/* Reset filters gumb */}
      <button
        className="reset-button"
        onClick={() =>
          onFilterChange({
            industries: [],
            status: [],
            source: [],
            budget: 100000000,
            deadLine: "",
          })
        }
      >
        Reset Filters
      </button>
    </aside>
  );
};

export default Sidebar;
