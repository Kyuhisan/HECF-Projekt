import './style.css';
import { useState } from 'react';

type Props = {
  filters: {
    technologies: string[];
    status: string[];
    source: string[];
    budget: number;
    deadLine: string;
  };
  onFilterChange: (filters: Props['filters']) => void;
}

const filterOption = {
  technologies: ['Technology', 'Science', 'Art', 'History'],
  status: ['Forthcoming', 'Open', 'Closed'],
  source: ['ec.europa.eu', 'getonepass.eu', 'cascadefunding.eu'],
};

const Sidebar = ({ filters, onFilterChange }: Props) => {
    const [openDropdown, setOpenDropdown] = useState<string | null>(null);
    
    const toggle = (key: string) => {
        setOpenDropdown(openDropdown === key ? null : key);
    }

    const handleCheckboxChange = (group: keyof Props['filters'], value: string) => {
      if (group === 'deadLine') return;

      const groupValues = filters[group] as string[];
      const updateValues = groupValues.includes(value)
        ? groupValues.filter(v => v !== value)
        : [...groupValues, value];

      onFilterChange({ ...filters, [group]: updateValues });
    };
    
    const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      const isoDate = event.target.value;
      const [year, month, day] = isoDate.split("-");
      const formatted = `${day}/${month}/${year}`;
      onFilterChange({ ...filters, deadLine: formatted });
    };

    //const uniqueCategories = Array.from(new Set(listings.flatMap(l => l.technologies)));

    return (
     <aside className="sidebar">
      <div className="sidebar-header">
        <h2>🔍 Filters</h2>
      </div>
      {Object.entries(filterOption).map(([key, values]) => (
        <div className="filter-group" key={key}>
          <div className="dropdown-header" onClick={() => toggle(key)}>
            {key.charAt(0).toUpperCase() + key.slice(1)}
          </div>
          {openDropdown === key && (
            <div className="dropdown-content">
              {values.map((value) => (
                <label key={value}>
                  <input
                    type="checkbox"
                    checked={(filters as any)[key].includes(value)}
                    onChange={() => handleCheckboxChange(key as keyof Props['filters'], value)}
                  />
                  {value}
                </label>
              ))}
            </div>
          )}
        </div>
      ))}
      {/* Budget slider */}
      <div className='filter-group'>
        <label className="budget-label">Budget: </label>
        <input
          type="range"
          min={0}
          max={100000000}
          step={1000000}
          value={filters.budget || 0}
          onChange={(e) => onFilterChange( {...filters, budget: Number(e.target.value)})}
        />
        <span className="budget-value">
          {filters.budget.toLocaleString()}€
        </span>
      </div>

      {/* Datum obravnava za filter */}
      <div className='filter-group'>
        <label className="deadline-date">Deadline: </label>
        <input
          type="date"
          className="deadline-date"
          value={filters.deadLine || ""}
          onChange={handleDateChange}
        />
      </div>
    </aside>
    );
};

export default Sidebar;