import './style.css';

type Props = {
    label: string;
    values: string[];
    isOpen: boolean;
    onToggle: () => void;
    selectedValues: string[];
    onCheckboxChange: (value: string) => void;
}

const DropdownFilter = ({ label, values, isOpen, onToggle, selectedValues, onCheckboxChange }: Props) => {
    return (
    <div className="filter-group">
      <div className="dropdown-header" onClick={onToggle}>
        {label}
      </div>
      {isOpen && (
        <div className="dropdown-content">
          {values.map((value) => (
            <label key={value}>
              <input 
                type="checkbox"
                checked={selectedValues.includes(value)}
                onChange={() => onCheckboxChange(value)} 
              />
              {value}
            </label>
          ))}
        </div>
      )}
    </div>
    );
};

export default DropdownFilter;  