import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import libledImage from "../assets/images/libled.jpg";

function CustomerView() {
  const { customerId } = useParams();
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const [customer, setCustomer] = useState({
    firstName: "",
    lastName: "",
    idCode: "",
    paymentMethod: "",
    info: "",
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    fetch(`http://localhost:8080/api/customers/${customerId}`)
      .then((response) => response.json())
      .then((data) => {
        setCustomer(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Failed to load customer", error);
        setLoading(false);
      });
  }, [customerId]);

  const handleChange = (e) => {
    setCustomer({ ...customer, [e.target.name]: e.target.value });

    if (errors[e.target.id]) {
      setErrors({ ...errors, [e.target.id]: null });
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();

    setErrors({});

    if (!validateForm()) return;

    const response = await fetch(
      `http://localhost:8080/api/customers/${customerId}`,
      {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(customer),
      }
    );
    if (response.ok) {
      console.log("Updated successfully!");
      navigate("/");
    } else {
      alert("Failed to update!");
    }
  };

  const handleBack = (e) => {
    e.preventDefault();
    navigate("/");
  };

  if (loading) return <p>Loading...</p>;

  const validateForm = () => {
    let newErrors = {};
    if (!customer.firstName.trim())
      newErrors.firstName = "Eesnimi on kohustuslik";
    if (!customer.lastName.trim())
      newErrors.lastName = "Perekonnanimi on kohustuslik";
    if (!customer.idCode.trim()) newErrors.idCode = "Id-kood on kohustuslik";
    if (!customer.paymentMethod.trim())
      newErrors.paymentMethod = "Makseviis on kohustuslik";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  return (
    <div>
      <div className="row add-event-row g-0 mt-3">
        <div className="col bg-custom-blue d-flex align-items-center justify-content-center add-event-text-col">
          <p className="text-center mb-0 h4">Osavõtja info</p>
        </div>
        <div className="col">
          <img src={libledImage} alt="Libled" className="add-event-image" />
        </div>
      </div>

      <form>
        <div className="row mb-5 mt-2">
          <div className="col-3"></div>
          <div className="col-md-8">
            <p className="add-event-title">Osavõtja info</p>
            <div className="row justify-content-start">
              <div className="col-md-2">
                <p>Eesnimi:</p>
              </div>
              <div className="col-md-10">
                <input
                  type="text"
                  name="firstName"
                  value={customer.firstName}
                  onChange={handleChange}
                />
                {errors.firstName && (
                  <div className="text-danger">{errors.firstName}</div>
                )}
              </div>
            </div>

            <div className="row justify-content-start">
              <div className="col-md-2">
                <p>Perekonnanimi:</p>
              </div>
              <div className="col-md-10">
                <input
                  type="text"
                  name="lastName"
                  value={customer.lastName}
                  onChange={handleChange}
                />
                {errors.lastName && (
                  <div className="text-danger">{errors.lastName}</div>
                )}
              </div>
            </div>

            <div className="row justify-content-start">
              <div className="col-md-2">
                <p>Isikukood:</p>
              </div>
              <div className="col-md-10">
                <input
                  type="text"
                  name="idCode"
                  value={customer.idCode}
                  onChange={handleChange}
                />
                {errors.idCode && (
                  <div className="text-danger">{errors.idCode}</div>
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
                  id="paymentMethod"
                  name="paymentMethod"
                  value={customer.paymentMethod}
                  onChange={handleChange}
                >
                  <option value="">Vali makseviis</option>
                  <option value="cash">Sula</option>
                  <option value="card">Kaart</option>
                </select>
                {errors.paymentMethod && (
                  <div className="text-danger">{errors.paymentMethod}</div>
                )}
              </div>
            </div>

            <div className="row justify-content-start">
              <div className="col-md-2">
                <p>Lisainfo:</p>
              </div>
              <div className="col-md-4">
                <textarea
                  type="text"
                  className="form-control"
                  name="info"
                  value={customer.info}
                  onChange={handleChange}
                ></textarea>
                {errors.customerInfo && (
                  <div className="text-danger">{errors.customerInfo}</div>
                )}
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
              onClick={handleSave}
            >
              Salvesta
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default CustomerView;
