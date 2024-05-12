import React, { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import libledImage from "../assets/images/libled.jpg";

function ParticipantsView() {
  const [customerData, setCustomerData] = useState({
    customerFirstName: "",
    customerLastName: "",
    customerCode: "",
    customerPaymentType: "",
    customerInfo: "",
  });

  const [companyData, setCompanyData] = useState({
    companyName: "",
    companyCode: "",
    companyParticipants: 0,
    companyPaymentType: "",
    companyInfo: "",
  });

  const [errors, setErrors] = useState({});
  const { eventId } = useParams();
  const navigate = useNavigate();
  const [isCustomer, setIsCustomer] = useState(false);
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

  const handleCustomerChange = (e) => {
    setCustomerData({ ...customerData, [e.target.id]: e.target.value });

    if (errors[e.target.id]) {
      setErrors({ ...errors, [e.target.id]: null });
    }
  };

  const handleCompanyChange = (e) => {
    setCompanyData({ ...companyData, [e.target.id]: e.target.value });

    if (errors[e.target.id]) {
      setErrors({ ...errors, [e.target.id]: null });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setErrors({});

    if (validateForm()) {
      console.log("Form is valid");
    } else {
      return;
    }
    const endpoint = isCustomer ? "/api/customers" : "/api/companies";

    const newParticipant = isCustomer
      ? {
          firstName: customerData.customerFirstName,
          lastName: customerData.customerLastName,
          idCode: customerData.customerCode,
          paymentMethod: customerData.customerPaymentType,
          info: customerData.customerInfo,
        }
      : {
          companyName: companyData.companyName,
          companyCode: companyData.companyCode,
          participantCount: companyData.companyParticipants,
          paymentMethod: companyData.companyPaymentType,
          info: companyData.companyInfo,
        };

    console.log("Adding participant ", newParticipant, " to the database");

    const response = await fetch("http://localhost:8080" + endpoint, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newParticipant),
    });

    if (response.ok) {
      const { id } = await response.json();
      const registerResponse = await fetch(
        "http://localhost:8080/api/eventParticipant",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            eventId,
            participantId: id,
            participantType: isCustomer ? "customer" : "company",
          }),
        }
      );

      if (registerResponse.ok) {
        if (isCustomer) {
          setCustomerData({
            customerFirstName: "",
            customerLastName: "",
            customerCode: "",
            customerPaymentType: "",
            customerInfo: "",
          });
        } else {
          setCompanyData({
            companyName: "",
            companyCode: "",
            companyParticipants: 0,
            companyPaymentType: "",
            companyInfo: "",
          });
        }

        console.log(
          "Participant added and registered to the event successfully!"
        );
        navigate("/");
      } else {
        console.error("Failed to register participant for the event");
      }
    } else {
      console.error("Failed to add participant");
    }
  };

  const deleteParticipant = (id, type) => {
    console.log("Deleting participant", id, type);
    // Actual delete logic here
  };

  if (loading) return <p>Loading...</p>;
  if (!event) return <p>Event not found</p>;

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()}`;
  };

  const handleBack = (e) => {
    e.preventDefault();
    navigate("/");
  };

  const validateForm = () => {
    let formIsValid = false;
    let errors = {};

    if (isCustomer) {
      if (!customerData.customerFirstName.trim())
        errors.customerFirstName = "Eesnimi on kohustuslik";
      if (!customerData.customerLastName.trim())
        errors.customerLastName = "Perekonnanimi on kohustuslik";
      if (!customerData.customerCode.trim())
        errors.customerCode = "Id-kood on kohustuslik";
      if (!customerData.customerPaymentType.trim())
        errors.customerPaymentType = "Makseviis on kohustuslik";
    } else {
      if (!companyData.companyName.trim())
        errors.companyName = "Ettevõtte nimi on kohustuslik";
      if (!companyData.companyCode.trim())
        errors.companyCode = "Ettevõtte kood on kohustuslik";
      if (
        companyData.companyParticipants == 0 ||
        !companyData.companyParticipants.toString().trim()
      )
        errors.companyParticipants = "Osalejate arv on kohustuslik";
      if (!/^\d+$/.test(companyData.companyParticipants.toString()))
        errors.companyParticipants = "Osalejate arv peab olema number";

      if (!companyData.companyPaymentType.trim())
        errors.companyPaymentType = "Makseviis on kohustuslik";
    }

    if (Object.keys(errors).length > 0) {
      setErrors(errors);
      return;
    } else {
      formIsValid = true;
      return formIsValid;
    }
  };

  return (
    <div>
      <div className="row add-event-row g-0 mt-3">
        <div className="col bg-custom-blue d-flex align-items-center justify-content-center add-event-text-col">
          <p className="text-center mb-0 h4">Osavõtjad</p>
        </div>
        <div className="col">
          <img src={libledImage} alt="Libled" className="add-event-image" />
        </div>
      </div>

      <div className="row mb-5 mt-2">
        <div className="col-3"></div>
        <div className="col-md-8">
          <p className="add-event-title">Osavõtjad</p>
          <div className="row justify-content-start">
            <div className="col-md-2">
              <p>Ürituse nimi:</p>
            </div>
            <div className="col-md-10">
              <p>{event.title}</p>
            </div>
          </div>

          <div className="row justify-content-start">
            <div className="col-md-2">
              <p>Toimumisaeg:</p>
            </div>
            <div className="col-md-10">
              <p>{formatDate(event.startTime)}</p>
            </div>
          </div>

          <div className="row justify-content-start">
            <div className="col-md-2">
              <p>Koht:</p>
            </div>
            <div className="col-md-10">
              <p>{event.location}</p>
            </div>
          </div>

          {participants.length > 0 ? (
            <>
              <div className="row justify-content-start">
                <p>Osavõtjad</p>{" "}
              </div>
              {participants.map((participant, index) => (
                <div key={`${participant.type}-${participant.id}`}>
                  <div className="row">
                    <div className="col-md-2"></div>
                    <div className="col-md-4">
                      {index}. {participant.name}
                    </div>
                    <div className="col-md-3">{participant.code}</div>
                    <div className="col-md-1">
                      <Link
                        style={{
                          textDecoration: "none",
                          color: "gray",
                        }}
                        to={`/${participant.type}/${participant.id}`}
                      >
                        VAATA
                      </Link>
                    </div>
                    <div className="col-md-1 align-items-start">
                      <button
                        className="btn btn-link p-0"
                        style={{
                          textDecoration: "none",
                          color: "gray",
                        }}
                        onClick={() =>
                          deleteParticipant(participant.id, participant.type)
                        }
                      >
                        KUSTUTA
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </>
          ) : (
            <p>No participants found</p>
          )}

          <p className="add-event-title mt-3">Osavõtjate lisamine</p>

          <form onSubmit={handleSubmit}>
            <div className="row mb-2">
              <div className="col-md-2"></div>
              <div className="col-md-2">
                <div className="form-check">
                  <input
                    className="form-check-input"
                    name="participantType"
                    checked={isCustomer}
                    onChange={() => setIsCustomer(true)}
                    id="customerRadio"
                    type="radio"
                  />
                  <label className="form-check-label" htmlFor="customerRadio">
                    Eraisik
                  </label>
                </div>
              </div>
              <div className="col">
                <div className="form-check">
                  <input
                    className="form-check-input"
                    name="participantType"
                    checked={!isCustomer}
                    onChange={() => setIsCustomer(false)}
                    id="companyRadio"
                    type="radio"
                  />
                  <label className="form-check-label" htmlFor="companyRadio">
                    Ettevõte
                  </label>
                </div>
              </div>
            </div>
            {isCustomer ? (
              //CUSTOMER FROM
              <>
                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Eesnimi:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="customerFirstName"
                      value={customerData.customerFirstName}
                      onChange={handleCustomerChange}
                    />
                    {errors.customerFirstName && (
                      <div className="text-danger">
                        {errors.customerFirstName}
                      </div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Perekonnanimi:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="customerLastName"
                      value={customerData.customerLastName}
                      onChange={handleCustomerChange}
                    />
                    {errors.customerLastName && (
                      <div className="text-danger">
                        {errors.customerLastName}
                      </div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Idkood:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="customerCode"
                      value={customerData.customerCode}
                      onChange={handleCustomerChange}
                    />
                    {errors.customerCode && (
                      <div className="text-danger">{errors.customerCode}</div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Maksmisviis:</p>
                  </div>
                  <div className="col-md-4">
                    <select
                      className="form-select"
                      id="customerPaymentType"
                      value={customerData.customerPaymentType}
                      onChange={handleCustomerChange}
                    >
                      <option value="">Vali makseviis</option>
                      <option value="cash">Sula</option>
                      <option value="card">Kaart</option>
                    </select>
                    {errors.customerPaymentType && (
                      <div className="text-danger">
                        {errors.customerPaymentType}
                      </div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Lisainfo:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="customerInfo"
                      value={customerData.customerInfo}
                      onChange={handleCustomerChange}
                    />
                    {errors.customerInfo && (
                      <div className="text-danger">{errors.customerInfo}</div>
                    )}
                  </div>
                </div>
              </>
            ) : (
              //COMPANY FROM
              <>
                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Ettevõtte nimi:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="companyName"
                      value={companyData.companyName}
                      onChange={handleCompanyChange}
                    />
                    {errors.companyName && (
                      <div className="text-danger">{errors.companyName}</div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Ettevõtte kood:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="companyCode"
                      value={companyData.companyCode}
                      onChange={handleCompanyChange}
                    />
                    {errors.companyCode && (
                      <div className="text-danger">{errors.companyCode}</div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Inimeste arv:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="companyParticipants"
                      value={companyData.companyParticipants}
                      onChange={handleCompanyChange}
                    />
                    {errors.companyParticipants && (
                      <div className="text-danger">
                        {errors.companyParticipants}
                      </div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Maksmisviis:</p>
                  </div>
                  <div className="col-md-4">
                    <select
                      className="form-select"
                      id="companyPaymentType"
                      value={companyData.companyPaymentType}
                      onChange={handleCompanyChange}
                    >
                      <option value="">Vali makseviis</option>
                      <option value="cash">Sula</option>
                      <option value="card">Kaart</option>
                    </select>
                    {errors.companyPaymentType && (
                      <div className="text-danger">
                        {errors.companyPaymentType}
                      </div>
                    )}
                  </div>
                </div>

                <div className="row justify-content-start">
                  <div className="col-md-2">
                    <p>Lisainfo:</p>
                  </div>
                  <div className="col-md-4">
                    <input
                      type="text"
                      className="form-control"
                      id="companyInfo"
                      value={companyData.companyInfo}
                      onChange={handleCompanyChange}
                    />
                    {errors.companyInfo && (
                      <div className="text-danger">{errors.companyInfo}</div>
                    )}
                  </div>
                </div>
              </>
            )}

            <button
              type="submit"
              className="btn btn-secondary mt-5 mb-5"
              onClick={handleBack}
            >
              Tagasi
            </button>
            <button
              type="submit"
              className="btn bg-custom-blue mt-5 mb-5 mx-2"
              onClick={handleSubmit}
            >
              Lisa
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default ParticipantsView;
