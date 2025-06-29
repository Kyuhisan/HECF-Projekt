import { useState } from "react";
import "./style.css";
import {
  Bell,
  Mail,
  X,
  MailCheck,
} from "lucide-react";


type NotifyPopupProps = {
  onClose: () => void;
  allIndustries: string[];
  allTechnologies: string[];
};

const NotifyPopup = ({ onClose, allIndustries, allTechnologies }: NotifyPopupProps) => {
  const [email, setEmail] = useState("");
  const [selectedIndustries, setSelectedIndustries] = useState<string[]>([]);
  const [selectedTechnologies, setSelectedTechnologies] = useState<string[]>([]);

  const handleSubmit = async () => {
    // TODO: Validacija + API klic
    const messegingData = {
      email,
      industries: selectedIndustries,
      technologies: selectedTechnologies,
    };
    await fetch("/api/subscriptions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(messegingData),
    });
    alert("Successfully subscribed!");
    onClose();
  };
   const toggleSelection = (item: string, selected: string[], setSelected: (val: string[]) => void) => {
    if (selected.includes(item)) {
      setSelected(selected.filter((s) => s !== item));
    } else {
      setSelected([...selected, item]);
    }
  };

 return (
  <div className="popup-overlay" onClick={onClose}>
    <div className="popup-content" onClick={(e) => e.stopPropagation()}>
    <button className="close-button" onClick={onClose}> <X size={20}></X></button>
      <div className="popup-header">
        <span className="popup-icon"><Bell size={40}/></span>
        <h2>Get Notified Weekly</h2>
        <p className="popup-subtitle">
           Receive personalized updates about open funding opportunities that match your interests
        </p>
        <p className="popup-subtitle" style={{ marginTop: "0.5rem", fontSize: "0.9rem", color: "#6b7280" }}>
    Every week, we'll send you a summary of how many funding calls are available, based on the industries and technologies you choose below.
  </p>
      </div>
        <div className="form-group-center">
      <div className="form-group">
        <label className="input-label" htmlFor="email"><Mail size={16}color="#2563eb" style={{ marginRight: "5px" ,marginTop: "8px"}}></Mail> Email address</label>
        <input
          id="email"
          type="email"
          placeholder="exapmle.name@gmail.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      </div>

      <div className="section">
        <h4>Industries:</h4>
        <div className="tag-grid">
          {allIndustries.map((ind) => (
            <button
              key={ind}
              className={`tag ${selectedIndustries.includes(ind) ? "selected" : ""}`}
              onClick={() => toggleSelection(ind, selectedIndustries, setSelectedIndustries)}
            >
              {ind}
            </button>
          ))}
        </div>
      </div>

      <div className="section">
        <h4>Technologies:</h4>
        <div className="tag-grid">
          {allTechnologies.map((tech) => (
            <button
              key={tech}
              className={`tag ${selectedTechnologies.includes(tech) ? "selected" : ""}`}
              onClick={() => toggleSelection(tech, selectedTechnologies, setSelectedTechnologies)}
            >
              {tech}
            </button>
          ))}
        </div>
      </div>

      <div className="popup-actions">
        <div className="popup-actions-center">
        <button onClick={handleSubmit}>Notify Me <MailCheck size={16} style={{ marginLeft: "5px" }}></MailCheck> </button>
        </div>
      </div>
    </div>
  </div>
);
};

export default NotifyPopup;