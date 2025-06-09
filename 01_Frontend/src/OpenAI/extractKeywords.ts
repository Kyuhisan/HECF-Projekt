import { backendUrl } from "../api";

export const extractKeywords = async (text: string): Promise<string[]> => {
  const response = await fetch(`http://${backendUrl}/api/openai/keywords`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ text }),
  });

  const raw = await response.text();

  try {
    const match = raw.match(/\[.*\]/s);
    if (match) return JSON.parse(match[0]);
  } catch (e) {
    console.error("Failed to parse response:", raw);
  }

  return [];
};