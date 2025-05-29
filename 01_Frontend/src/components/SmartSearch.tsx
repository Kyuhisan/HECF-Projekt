import { useState, useCallback } from "react";
import debounce from "lodash/debounce";
import { extractKeywords } from "../OpenAI/extractKeywords";
import "./style.css";
import { Bot,ListRestart,BrainCircuit, KeyRound ,Rocket,Boxes} from 'lucide-react';

type SmartSearchProps = {
  value: string;
  onChange: (value: string) => void;
  onKeywordsChange?: (keywords: string[]) => void;
};

const SmartSearch = ({
  value,
  onChange,
  onKeywordsChange,
}: SmartSearchProps) => {
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
  const handleClear = () => {
    setKeywords([]);
    onKeywordsChange?.([]);
    onChange?.("");
  };
  return (
    <>
      <div className="title"><Boxes size={40}/> HECF-SmartSearch</div>

      <div className="content-wrapper-search">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Describe what you're looking for..."
            value={value}
            onChange={(e) => onChange(e.target.value)}
          />
          <div className="keyword-header">
              <button className="clear-button" onClick={handleClear}><ListRestart size={21}/>

</button>
            </div>
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
          <div className="bottom">
            <div className="bottom-left"> <Bot size={21}/></div>
            <div className="bottom-right">Mistral-7b</div>
          </div>
        </div>

        {keywords.length > 0 && (
          <div className="keyword-results">

          

            <div className="container-keywords">
              {keywords.map((k,index) => (
                <div key={k} className={`keyword-item keyword-${index % 13}`}>
                  <KeyRound size={12} /> {k} 
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
