export const extractKeywords = async (text: string): Promise<string[]> => {
  const backendUrl = import.meta.env.VITE_BACKEND_URL;
  
  const response = await fetch(`${backendUrl}/openai/keywords`, {
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