import { useEffect, useState } from "react";
import { api } from "./api";

type UserData = {
  id: string;
  name: string;
  username: string;
  email: string;
};

type listingData = {
  summary: string;
  identifier: string;
  language: string;
  url: string;
}

function App() {
  const [data, setData] = useState<UserData[]>([]);
  const [listingData, setListingData] = useState<listingData[]>([]);

  useEffect(() => {
    api.get("/users/all").then(res => setData(res.data));
    api.get("/listings/all").then(res => setListingData(res.data));
  }, []);

  return (
    <div style={{ padding: "2rem" }}>
      <h1>User Data Table</h1>
      <table border={1} cellPadding={10} cellSpacing={0}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>{item.username}</td>
              <td>{item.email}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <br />
            <table border={1} cellPadding={10} cellSpacing={0}>
        <thead>
          <tr>
            <th>Url</th>
            <th>Summary</th>
            <th>Identifier</th>
            <th>Language</th>
          </tr>
        </thead>
        <tbody>
          {listingData.map((item) => (
            <tr key={item.url}>
              <td>{item.url}</td>
              <td>{item.summary}</td>
              <td>{item.identifier}</td>
              <td>{item.language}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
