import { useState, useCallback } from "react";
import debounce from "lodash/debounce";
import { extractKeywords } from "../OpenAI/extractKeywords";

type SmartSearchProps = {
  value: string;
  onChange: (value: string) => void;
};

const SmartSearch = ({ value, onChange }: SmartSearchProps) => {
  const [keywords, setKeywords] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  const handleExtract = useCallback(
    debounce(async (val: string) => {
      setLoading(true);
      const extracted = await extractKeywords(val);
      setKeywords(extracted);
      setLoading(false);
    }, 800, { leading: false, trailing: true }),
    []
  );

  return (
    <>
      <div className="title">HECF-SmartSearch</div>

      <div className="content-wrapper">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Search by summary..."
            value={value}
            onChange={(e) => onChange(e.target.value)}
          />
          <button onClick={() => handleExtract(value)} disabled={loading || !value}>
            {loading ? "Extracting..." : "Get Keywords"}
          </button>
        </div>

        {keywords.length > 0 && (
          <div className="keyword-results">
            <strong>Extracted Keywords:</strong>
            <ul>
              {keywords.map((k) => (
                <li key={k}>{k}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </>
  );
};

export default SmartSearch;
