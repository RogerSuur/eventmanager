import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

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

  return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">Events Dashboard</h1>
      <div className="row">
        <div className="col-md-6">
          <h2>Upcoming Events</h2>
          {loading ? (
            <p>Loading...</p>
          ) : (
            upcomingEvents.map((event) => (
              <div key={event.id}>
                <h3>{event.title}</h3>
                <p>{new Date(event.startTime).toLocaleString()}</p>
                <p>{event.location}</p>
              </div>
            ))
          )}
        </div>
        <div className="col-md-6">
          <h2>Past Events</h2>
          {loading ? (
            <p>Loading...</p>
          ) : (
            pastEvents.map((event) => (
              <div key={event.id}>
                <h3>{event.title}</h3>
                <p>{new Date(event.startTime).toLocaleString()}</p>
                <p>{event.location}</p>
              </div>
            ))
          )}
        </div>
      </div>
      <div className="text-center">
        <Link to="/add-event">
          <button className="btn btn-primary mt-3">Add New Event</button>
        </Link>
      </div>
    </div>
  );
}

export default MainPage;
