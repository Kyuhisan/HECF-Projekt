import { useState } from "react";
import "./style.css";
import { Bell, Mail, X, MailCheck, Factory, Cpu, KeyRound } from "lucide-react";
import Swal from "sweetalert2";

type NotifyPopupProps = {
  onClose: () => void;
  allIndustries: string[];
  allTechnologies: string[];
};

const NotifyPopup = ({
  onClose,
  allIndustries,
  allTechnologies,
}: NotifyPopupProps) => {
  const [email, setEmail] = useState("");
  const [selectedIndustries, setSelectedIndustries] = useState<string[]>([]);
  const [selectedTechnologies, setSelectedTechnologies] = useState<string[]>(
    []
  );
  const [industrySearch, setIndustrySearch] = useState("");
  const [technologySearch, setTechnologySearch] = useState("");

  const handleSubmit = async () => {
    // TODO: Validacija + API klic
    const messegingData = {
      email,
      industries: selectedIndustries,
      technologies: selectedTechnologies,
    };
    try {
      const response = await fetch("/api/subscriptions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(messegingData),
      });

      if (response.ok) {
        Swal.fire({
          title: "Successfully subscribed!",
          text: "You will now receive weekly updates.",
          icon: "success",
          confirmButtonColor: "#3b82f6",
        }).then(() => {
          onClose();
        });
      } else {
        Swal.fire({
          title: "Error",
          text: "Something went wrong. Please try again.",
          icon: "error",
          confirmButtonColor: "#ef4444",
        });
      }
    } catch (error) {
      Swal.fire({
        title: "Network error",
        text: "Unable to connect to server.",
        icon: "error",
        confirmButtonColor: "#ef4444",
      });
    }
  };
  const toggleSelection = (
    item: string,
    selected: string[],
    setSelected: (val: string[]) => void
  ) => {
    if (selected.includes(item)) {
      setSelected(selected.filter((s) => s !== item));
    } else {
      setSelected([...selected, item]);
    }
  };

  return (
    <div className="popup-overlay" onClick={onClose}>
      <div className="popup-content" onClick={(e) => e.stopPropagation()}>
        <button className="close-button" onClick={onClose}>
          {" "}
          <X size={20}></X>
        </button>
        <div className="popup-header">
          <span className="popup-icon">
            <Bell size={40} />
          </span>
          <h2>Get Notified Weekly</h2>
          <p className="popup-subtitle">
            Receive personalized updates about open funding opportunities that
            match your interests
          </p>
          <p
            className="popup-subtitle"
            style={{
              marginTop: "0.5rem",
              fontSize: "0.9rem",
              color: "#6b7280",
            }}
          >
            Every week, we'll send you a summary of how many funding calls are
            available, based on the industries and technologies you choose
            below.
          </p>
        </div>
        <div className="form-group-center">
          <div className="form-group">
            <label className="input-label" htmlFor="email">
              <Mail
                size={16}
                color="#2563eb"
                style={{ marginRight: "5px", marginTop: "8px" }}
              ></Mail>{" "}
              Email address
            </label>
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
          <h4>
            {" "}
            <Factory size={16} color="#3b77f7"></Factory> Industries:
          </h4>
          <input
            type="text"
            className="search-input"
            placeholder="Search industries..."
            value={industrySearch}
            onChange={(e) => setIndustrySearch(e.target.value)}
          />
          <div className="industries-tags tag-scroll-section">
            {allIndustries
              .filter((ind) =>
                ind.toLowerCase().includes(industrySearch.toLowerCase())
              )
              .map((ind, index) => (
                <span
                  key={ind}
                  className={`tag-badge industry-tag industry-${index % 12} ${
                    selectedIndustries.includes(ind) ? "selected" : ""
                  }`}
                  onClick={() =>
                    toggleSelection(
                      ind,
                      selectedIndustries,
                      setSelectedIndustries
                    )
                  }
                >
                  <KeyRound size={14} /> {ind}
                </span>
              ))}
          </div>
        </div>

        <div className="section">
          <h4>
            {" "}
            <Cpu size={16} color="#3b77f7"></Cpu> Technologies:
          </h4>
          <input
            type="text"
            className="search-input"
            placeholder="Search technologies..."
            value={technologySearch}
            onChange={(e) => setTechnologySearch(e.target.value)}
          />
          <div className="technologies-tags tag-scroll-section">
            {allTechnologies
              .filter((tech) =>
                tech.toLowerCase().includes(technologySearch.toLowerCase())
              )
              .map((tech, index) => (
                <span
                  key={tech}
                  className={`tag-badge tech-tag tech-${index % 12} ${
                    selectedTechnologies.includes(tech) ? "selected" : ""
                  }`}
                  onClick={() =>
                    toggleSelection(
                      tech,
                      selectedTechnologies,
                      setSelectedTechnologies
                    )
                  }
                >
                  <KeyRound size={14} /> {tech}
                </span>
              ))}
          </div>
        </div>

        <div className="popup-actions">
          <div className="popup-actions-center">
            <button onClick={handleSubmit}>
              Notify Me{" "}
              <MailCheck size={16} style={{ marginLeft: "5px" }}></MailCheck>{" "}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotifyPopup;
