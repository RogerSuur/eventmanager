import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import MainPage from "./components/MainPage.jsx";
import AddEvent from "./components/AddEvent.jsx";
import ParticipantsView from "./components/ParticipantsView.jsx";
import AddParticipant from "./components/AddParticipant.jsx";

function App() {
  return (
    <div className="bg-light">
      <div className="container">
        <Router>
          <Navbar />
          <Routes>
            <Route path="/" element={<MainPage />} />
            <Route path="/add-event" element={<AddEvent />} />
            <Route
              path="/participants/:eventId"
              element={<ParticipantsView />}
            />
            {/* <Route path="/customer/:customerId" element={<CustomerView />} />
            <Route path="/company/:companyId" element={<CompanyView />} /> */}
            <Route path="/add-participant" element={<AddParticipant />} />
          </Routes>
          <Footer />
        </Router>
      </div>
    </div>
  );
}

export default App;
