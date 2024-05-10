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

  // Split events into upcoming and past
  const upcomingEvents = events.filter(
    (event) => new Date(event.startTime) > new Date()
  );
  const pastEvents = events.filter(
    (event) => new Date(event.startTime) <= new Date()
  );

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
              srcset=""
            />
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col-md-6 d-flex flex-column  align-items-center justify-content-center bg-white">
          <p className="bg-custom-blue h3 lightweight text-center p-4 w-100">
            Tulevad üritused
          </p>
          {loading ? (
            <p>Loading...</p>
          ) : (
            upcomingEvents.map((event, index) => (
              <div
                key={event.id}
                className="text-center w-100 d-flex justify-content-between align-items-center text-lightgrey"
                style={{ padding: "10px" }}
              >
                <p>
                  {index + 1}. {event.title}
                </p>
                <p>{formatDate(event.startTime)}</p>

                <p>OSAVÕTJAD</p>
                <button
                  className="btn"
                  style={{ border: "none", background: "none" }}
                >
                  <img
                    src={removeImage}
                    alt="Pilt"
                    className="color-black"
                    style={{ width: 10 }}
                    srcset=""
                  />
                </button>
              </div>
            ))
          )}
          <Link to="/add-event" style={{ textDecoration: "none" }}>
            <p className="text-grey mt-3 mb-3">ADD NEW EVENT</p>
          </Link>
        </div>
        <div className="col-md-6 d-flex flex-column  align-items-center justify-content-center">
          <p className="bg-custom-blue h3 text-center lightweight p-4 w-100">
            Toimunud üritused
          </p>
          {loading ? (
            <p>Loading...</p>
          ) : (
            pastEvents.map((event, index) => (
              <div key={event.id}>
                <div className="row">
                  <div className="col">
                    <p>
                      {index + 1}
                      {event.title}
                    </p>
                  </div>
                  <div className="col">
                    <p>{formatDate(event.startTime)}</p>
                  </div>
                  <div className="col">
                    <p>OSAVÕTJAD</p>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default MainPage;
