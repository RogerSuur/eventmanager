import React, { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";

function ParticipantsView() {
  const { eventId } = useParams();
  const navigate = useNavigate();
  const [isCustomer, setIsCustomer] = useState(true);
  const [event, setEvent] = useState(null);
  const [participants, setParticipants] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const eventResponse = await fetch(
          `http://localhost:8080/api/events/${eventId}`
        );
        const eventData = await eventResponse.json();
        setEvent(eventData);

        const participantsResponse = await fetch(
          `http://localhost:8080/api/eventParticipant/${eventId}`
        );
        const participantsData = await participantsResponse.json();
        console.log(participantsData);
        setParticipants(participantsData);
      } catch (error) {
        console.error("Failed to fetch data:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [eventId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const endpoint = isCustomer ? "/api/customers" : "/api/companies";
    const participantData = {
      // form fields
    };

    const response = await fetch(endpoint, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(participantData),
    });

    if (response.ok) {
      // Add to event_participants
      const { id } = await response.json();
      await fetch("/api/event_participants", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          eventId,
          participantId: id,
          participantType: isCustomer ? "customer" : "company",
        }),
      });
    }
  };

  if (loading) return <p>Loading...</p>;
  if (!event) return <p>Event not found</p>;

  return (
    <div>
      <h2>
        {event.title} - {event.location}
      </h2>
      <p>{new Date(event.startTime).toLocaleString()}</p>
      <p>{event.info}</p>

      {participants.length > 0 ? (
        <>
          <h1>Participants</h1>{" "}
          {participants.map((participant) => (
            <div key={participant.id}>
              <p>
                {participant.name} - {participant.code}
              </p>
              <Link to={`/${participant.participant_type}/${participant.id}`}>
                View Details
              </Link>
              <button
                onClick={() =>
                  deleteParticipant(
                    participant.id,
                    participant.participant_type
                  )
                }
              >
                Delete
              </button>
            </div>
          ))}
        </>
      ) : (
        <p>No participants found</p>
      )}

      <form onSubmit={handleSubmit}>
        <label>
          <input
            type="checkbox"
            checked={isCustomer}
            onChange={() => setIsCustomer(!isCustomer)}
          />
          {isCustomer ? "Customer" : "Company"}
        </label>
        {/* Form inputs here */}
        <button type="submit">Add Participant</button>
      </form>

      <button onClick={() => navigate(-1)}>Back to Events</button>
    </div>
  );
}

export default ParticipantsView;
