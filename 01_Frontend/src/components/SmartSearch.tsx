import { useState, useCallback } from "react";
import debounce from "lodash/debounce";
import { extractKeywords } from "../OpenAI/extractKeywords";
import './style.css';

type SmartSearchProps = {
  value: string;
  onChange: (value: string) => void;
  onKeywordsChange?: (keywords: string[]) => void;
};

const SmartSearch = ({ value, onChange, onKeywordsChange }: SmartSearchProps) => {
  const [keywords, setKeywords] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  const handleExtract = useCallback(
    debounce(
      async (val: string) => {
        setLoading(true);
        const extracted = await extractKeywords(val);
        setKeywords(extracted);
        onKeywordsChange?.(extracted);
        setLoading(false);
      },
      800,
      { leading: false, trailing: true }
    ),
    []
  );

  return (
    <>
      <div className="title">HECF-SmartSearch</div>

      <div className="content-wrapper-search">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Describe what you're looking for..."
            value={value}
            onChange={(e) => onChange(e.target.value)}
          />
          <button
            onClick={() => handleExtract(value)}
            disabled={loading || !value}
            className="search-button"
          >
            {loading ? (
              <div className="spinner"></div>
            ) : (
              <svg
                className="arrow-icon"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M5 10l7-7m0 0l7 7m-7-7v18"
                />
              </svg>
            )}
          </button>
        </div>
        

        {keywords.length > 0 && (
          <div className="keyword-results">
            <strong>Recommended results : </strong>
            
            <div className="container-keywords">
              {keywords.map((k) => (
                <div
                  key={k}
                  className="keyword-item"
                >
                  {k}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default SmartSearch;
