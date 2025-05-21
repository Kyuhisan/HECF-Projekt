import './style.css';
import { useState } from 'react';
import DropdownFilter from './DropdownFilter';

const filters = {
    category: ['Technology', 'Science', 'Art', 'History'],
    status: ['Forthcoming', 'Open', 'Closed'],
    price: ['Free', '$1 - $50', '$50 - $100', '$100+']
};

const Sidebar = () => {
    const [openDropdown, setOpenDropdown] = useState<string | null>(null);
    const toggle = (key: string) => {
        setOpenDropdown(openDropdown === key ? null : key);
    }
    return (
     <aside className="sidebar">
      <div className="sidebar-header">
        <h2>🔍 Filters</h2>
      </div>
      {Object.entries(filters).map(([key, values]) => (
        <DropdownFilter
          key={key}
          label={key.charAt(0).toUpperCase() + key.slice(1)}
          values={values}
          isOpen={openDropdown === key}
          onToggle={() => toggle(key)}
        />
      ))}
    </aside>
    );
};

export default Sidebar;