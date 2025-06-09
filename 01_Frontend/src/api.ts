import axios from "axios";

export const backendUrl = (window as any).env?.API_URL;

export const api = axios.create({
  baseURL: `http://${backendUrl}/api`,
});
