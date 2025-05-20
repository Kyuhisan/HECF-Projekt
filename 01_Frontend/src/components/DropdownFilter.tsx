import './style.css';

type Props = {
    label: string;
    values: string[];
    isOpen: boolean;
    onToggle: () => void;
}

const DropdownFilter = ({ label, values, isOpen, onToggle }: Props) => {
    return (
            <div className="filter-group">
      <div className="dropdown-header" onClick={onToggle}>
        {label}
      </div>
      {isOpen && (
        <div className="dropdown-content">
          {values.map((value) => (
            <label key={value}>
              <input type="checkbox" />
              {value}
            </label>
          ))}
        </div>
      )}
    </div>
    );
};

export default DropdownFilter;  