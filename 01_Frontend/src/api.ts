import axios from "axios";

const backendUrl =
  (window as any).env?.API_URL || import.meta.env.API_URL ||"localhost:8080";

export const api = axios.create({
  baseURL: "http://" + backendUrl + "/api",
});
