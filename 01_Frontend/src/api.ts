import axios from "axios";

const backendUrl = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080/api";

export const api = axios.create({
  baseURL: backendUrl
});
