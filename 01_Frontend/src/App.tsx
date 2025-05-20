import "./components/style.css";
import Sidebar from "./components/Sidebar";
import MainContent from "./components/MainContent";

function App() {

  return (
    <div className="layout">
      <Sidebar />
      <MainContent />
    </div>
  );
}

export default App;
