import './style.css';
import { useState } from 'react';

type Props = {
  filters: {
    category: string[];
    status: string[];
    source: string[];
    budget: string[];
    deadLine: string;
  };
  onFilterChange: (filters: Props['filters']) => void;
}

const filterOption = {
    category: ['Technology', 'Science', 'Art', 'History'],
    status: ['Forthcoming', 'Open', 'Closed'],
    source: ['ec.europa.eu', 'getonepass.eu', 'cascadefunding.eu'],
    budget: ['Free', '$1 - $50', '$50 - $100', '$100+']
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