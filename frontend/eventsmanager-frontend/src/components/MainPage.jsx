import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import image from "../assets/images/pilt.jpg";
import removeImage from "../assets/images/remove.svg";

function MainPage() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEvents = async () => {
      setLoading(true);
      try {
        const response = await fetch("http://localhost:8080/api/events");
        const data = await response.json();
        setEvents(data);
        console.log(data);
      } catch (error) {
        console.error("Failed to fetch events:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchEvents();
  }, []);

  const upcomingEvents = events
    .filter((event) => new Date(event.startTime) > new Date())
    .sort((a, b) => new Date(a.startTime) - new Date(b.startTime));
  const pastEvents = events
    .filter((event) => new Date(event.startTime) <= new Date())
    .sort((a, b) => new Date(b.startTime) - new Date(a.startTime));

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()}`;
  };

  return (
    <div>
      <div className="container mt-3 p-0">
        <div className="row g-0">
          <div className="col-6 bg-custom-blue d-flex align-items-center justify-content-center">
            <p className="mx-5 text-left h3 lightweight">
              Lorem ipsum dolor amet, <strong>consectetur adipisicing </strong>
              elit. Veritatis deleniti <strong>incidunt possimus modi </strong>
              sequi <strong> recusandae, totam, voluptates </strong> velit
              mollitia. Quaerat fugiat sunt praesentium ad sit.
            </p>
          </div>
          <div className="col-6">
            <img
              src={image}
              alt="Pilt"
              style={{ display: "block", width: "100%", height: "auto" }}
            />
          </div>
        </div>
        {/* </div> */}

        <div className="row mt-3 p-0">
          <div className="col-md-6 d-flex flex-column justify-content-center bg-white">
            <p className="bg-custom-blue h3 lightweight text-center p-4 w-100">
              Tulevad üritused
            </p>
            <div className="flex-grow-1">
              {loading ? (
                <p>Loading...</p>
              ) : (
                upcomingEvents.map((event, index) => (
                  <div
                    key={event.id}
                    className="text-center w-100 d-flex justify-content-between align-items-center text-lightgrey "
                    style={{ padding: "1px" }}
                  >
                    <div className="col-5 d-flex align-items-center text-start">
                      <p>
                        {index + 1}. {event.title}
                      </p>
                    </div>
                    <div className="col-4 d-flex align-items-center">
                      <p>{formatDate(event.startTime)}</p>
                    </div>
                    <Link
                      to={`/participants/${event.id}`}
                      className="col-2 d-flex align-items-center"
                      style={{
                        textDecoration: "none",
                        color: "gray",
                      }}
                    >
                      <p>OSAVÕTJAD</p>
                    </Link>
                    <div className="col-1 d-flex align-items-center justify-content-center">
                      <button
                        className="btn p-0 mb-3"
                        style={{ border: "none", background: "none" }}
                      >
                        <img
                          src={removeImage}
                          alt="Pilt"
                          className="color-black"
                          style={{ width: 15 }}
                        />
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
            <Link
              to="/add-event"
              style={{ textDecoration: "none", color: "gray" }}
            >
              <p className="text-grey mt-3 mb-3">ADD NEW EVENT</p>
            </Link>
          </div>

          <div className="col-md-6 d-flex flex-column justify-content-center bg-white">
            <p className="bg-custom-blue h3 lightweight text-center p-4 w-100">
              Toimunud üritused
            </p>
            <div className="flex-grow-1">
              {loading ? (
                <p>Loading...</p>
              ) : (
                pastEvents.map((event, index) => (
                  <div
                    key={event.id}
                    className="text-center w-100 d-flex justify-content-between align-items-center text-lightgrey"
                    style={{ padding: "1px" }}
                  >
                    <div className="col-6 text-start">
                      <p>
                        {index + 1}. {event.title}
                      </p>
                    </div>
                    <div className="col-3">
                      <p>{formatDate(event.startTime)}</p>
                    </div>
                    <Link to={`/participants/${event.id}`} className="col-3">
                      <p>OSAVÕTJAD</p>
                    </Link>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MainPage;
