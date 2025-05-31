import { useState, useCallback, useRef, useEffect } from "react";
import debounce from "lodash/debounce";
import { extractKeywords } from "../OpenAI/extractKeywords";
import "./style.css";
import {
  Bot,
  ListRestart,
  BrainCircuit,
  KeyRound,
  Rocket,
  Boxes,
} from "lucide-react";

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
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const [selectedKeywords, setSelectedKeywords] = useState<string[]>([]);

  const toggleKeywordSelection = (keyword: string) => {
    setSelectedKeywords((prev) =>
      prev.includes(keyword)
        ? prev.filter((k) => k !== keyword)
        : [...prev, keyword]
    );
  };
  const adjustHeight = useCallback(() => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = "auto";
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  }, []);

  useEffect(() => {
    adjustHeight();
  }, [value, adjustHeight]);

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

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    onChange(e.target.value);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    // Submit on Enter (without Shift)
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      if (value.trim()) {
        handleExtract(value);
      }
    }
  };

  return (
    <>
      <div className="title">
        <Boxes size={40} /> HECF-SmartSearch
      </div>

      <div className="content-wrapper-search">
        <div className="search-bar">
          <textarea
            ref={textareaRef}
            placeholder="Describe what you're looking for..."
            value={value}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            rows={1}
          />
          <div className="keyword-header">
            <button className="clear-button" onClick={handleClear}>
              <ListRestart size={21} />
            </button>
          </div>
          <button
            onClick={() => {
              handleExtract(value);
            }}
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
            <div className="bottom-left">
              {" "}
              <Bot size={21} />
            </div>
            <div className="bottom-right">Mistral-7b</div>
          </div>
        </div>

        {keywords.length > 0 && (
          <div className="keyword-results">
            <div className="container-keywords">
              {keywords.map((k, index) => {
                const isSelected = selectedKeywords.includes(k);
                return (
                  <div
                    key={k}
                    onClick={() => toggleKeywordSelection(k)}
                    className={`keyword-item keyword-${index % 13} ${
                      isSelected ? "selected" : ""
                    }`}
                  >
                    <KeyRound size={12} /> {k}
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default SmartSearch;
