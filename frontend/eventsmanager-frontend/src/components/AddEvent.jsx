import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import libledImage from "../assets/images/libled.jpg";

function AddEvent() {
  const [formData, setFormData] = useState({
    eventName: "",
    eventTime: "",
    eventLocation: "",
    eventInfo: "",
  });
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.id]: e.target.value });

    if (errors[e.target.id]) {
      setErrors({ ...errors, [e.target.id]: null });
    }
  };

  const validateForm = () => {
    let formIsValid = true;
    let errors = {};

    if (!formData.eventName.trim()) {
      errors.eventName = "Ürituse nimi on kohustuslik.";
      formIsValid = false;
    }

    if (!formData.eventLocation.trim()) {
      errors.eventLocation = "Koht on kohustuslik.";
      formIsValid = false;
    }

    const dateFormatRegex = /^(\d{2})\.(\d{2})\.(\d{4})$/;
    if (!formData.eventTime.match(dateFormatRegex)) {
      errors.eventTime = "Toimumisaeg peab olema formaadis pp.kk.aaaa.";
      formIsValid = false;
    } else {
      const dateParts = formData.eventTime.split(".");
      const year = parseInt(dateParts[2], 10);
      const month = parseInt(dateParts[1], 10) - 1;
      const day = parseInt(dateParts[0], 10);
      const eventDate = new Date(year, month, day);

      if (eventDate <= new Date() || month > 12 || day > 31) {
        errors.eventTime =
          "Toimumisaeg peab olema tulevikus formaadis pp.kk.aaaa.";
        formIsValid = false;
      }
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      console.log("Form is valid", formData);
    } else {
      return;
    }

    const eventDetails = {
      title: formData.eventName,
      startTime: new Date(
        formData.eventTime.split(".").reverse().join("-")
      ).toISOString(),
      location: formData.eventLocation,
      info: formData.eventInfo,
    };

    try {
      const response = await fetch("http://localhost:8080/api/events", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(eventDetails),
      });
      if (!response.ok) throw new Error("Failed to create event.");
      alert("Event added successfully!");
      navigate("/");
    } catch (error) {
      console.error("Failed to submit event:", error);
      alert("Failed to add event.");
    }
  };

  const handleBack = (e) => {
    e.preventDefault();
    navigate("/");
  };

  return (
    <div className="mt-3 p-0 bg-white">
      <div className="row add-event-row g-0 mt-3">
        <div className="col bg-custom-blue d-flex align-items-center justify-content-center add-event-text-col">
          <p className="text-center mb-0 h4">Ürituse lisamine</p>
        </div>
        <div className="col">
          <img src={libledImage} alt="Libled" className="add-event-image" />
        </div>
      </div>

      <div className="row mt-5 mb-5 justify-content-md-center">
        <div className="col-md-auto">
          <div className="row mb-1">
            <div className="col">
              <p className="add-event-title">Ürituse lisamine</p>
            </div>
          </div>
          <form>
            <div className="row mb-1">
              <div className="col">
                <label htmlFor="eventName">Ürituse nimi:</label>
              </div>
              <div className="col">
                <div className="form-group">
                  <input
                    type="text"
                    className="form-control"
                    id="eventName"
                    value={formData.eventName}
                    onChange={handleChange}
                  />
                  {errors.eventName && (
                    <div className="text-danger">{errors.eventName}</div>
                  )}
                </div>
              </div>
            </div>
            <div className="row mb-1">
              <div className="col">
                <label htmlFor="eventTime">Toimumisaeg:</label>
              </div>
              <div className="col">
                <div className="form-group">
                  <input
                    type="text"
                    className="form-control"
                    id="eventTime"
                    value={formData.eventTime}
                    onChange={handleChange}
                    placeholder="pp.kk.aaa hh:mm"
                  />
                  {errors.eventTime && (
                    <div className="text-danger">{errors.eventTime}</div>
                  )}
                </div>
              </div>
            </div>
            <div className="row mb-1">
              <div className="col">
                <label htmlFor="eventLocation">Koht:</label>
              </div>
              <div className="col">
                <div className="form-group">
                  <input
                    type="text"
                    className="form-control"
                    id="eventLocation"
                    value={formData.eventLocation}
                    onChange={handleChange}
                  />
                  {errors.eventLocation && (
                    <div className="text-danger">{errors.eventLocation}</div>
                  )}
                </div>
              </div>
            </div>
            <div className="row mb-1">
              <div className="col">
                <label htmlFor="eventInfo">Lisainfo:</label>
              </div>
              <div className="col">
                <div className="form-group">
                  <textarea
                    style={{ resize: "none" }}
                    className="form-control"
                    id="eventInfo"
                    rows="3"
                    value={formData.eventInfo}
                    onChange={handleChange}
                  ></textarea>
                </div>
              </div>
            </div>
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

export default AddEvent;
