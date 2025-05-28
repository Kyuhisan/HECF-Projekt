import './style.css';
import { useState } from 'react';

type Props = {
  filters: {
    industries: string[];
    status: string[];
    source: string[];
    budget: number;
    deadLine: string;
  };
  onFilterChange: (filters: Props['filters']) => void;
  industries: string[];
}

const filterOption = {
  status: ['Forthcoming', 'Open', 'Closed'],
  source: ['ec.europa.eu', 'getonepass.eu', 'cascadefunding.eu'],
};

const Sidebar = ({ filters, onFilterChange, industries }: Props) => {
    const [openDropdowns, setOpenDropdowns] = useState<Record<string, boolean>>({});
    
    const toggle = (key: string) => {
      setOpenDropdowns(prev => ({
        ...prev,
        [key]: !prev[key],
      }));
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
      {/* Industries dinamicni filter */}
      <div className="filter-group" key='industries'>
        <div className="dropdown-header" onClick={() => toggle('industries')}>
          Industries
        </div>
        {openDropdowns['industries'] && (
          <div className="dropdown-content">
            {industries.map((industry) => (
              <label key={industry}>
                <input
                  type="checkbox"
                  checked={filters.industries.includes(industry)}
                  onChange={() => handleCheckboxChange('industries', industry)}
                />
                {industry}
              </label>
            ))}
          </div>
        )}
      </div>

      {Object.entries(filterOption).map(([key, values]) => (
        <div className="filter-group" key={key}>
          <div className="dropdown-header" onClick={() => toggle(key)}>
            {key.charAt(0).toUpperCase() + key.slice(1)}
          </div>
          {openDropdowns[key] && (
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
  <div className="slider-container">
    <div className="slider-header">
      <div className="slider-title">
       <b>Budget</b> 
      </div>

    </div>
    
    <div className="slider-wrapper">
      <div className="slider-track">
        <div 
          className="slider-fill" 
          style={{width: `${(filters.budget / 100000000) * 100}%`}}
        ></div>
        <div 
          className="slider-thumb" 
          style={{left: `${(filters.budget / 100000000) * 100}%`}}
        ></div>
      </div>
      
      <input
        type="range"
        min={0}
        max={100000000}
        step={1000000}
        value={filters.budget || 0}
        onChange={(e) => onFilterChange({...filters, budget: Number(e.target.value)})}
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
      <div className='filter-group'>
        <div className="slider-container">
        <label className="slider-title"> <b>Deadline</b></label>
        <br />
        <input
          type="date"
          className="slider-title"
          value={filters.deadLine || ""}
          onChange={handleDateChange}
        />
      </div>
      </div>

      {/* Reset filters gumb */}
      <button
        className="reset-button"
        onClick={() => onFilterChange({
          industries: [],
          status: [],
          source: [],
          budget: 100000000,
          deadLine: "",
        })}
      >
        Reset Filters
      </button>
    </aside>
    );
};

export default Sidebar;