import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import libledImage from "../assets/images/libled.jpg";

function CompanyView() {
  const { companyId } = useParams();
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const [company, setCompany] = useState({
    companyName: "",
    companyCode: "",
    participantCount: 0,
    paymentMethod: "",
    info: "",
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    fetch(`http://localhost:8080/api/companies/${companyId}`)
      .then((response) => response.json())
      .then((data) => {
        setCompany(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Failed to load company", error);
        setLoading(false);
      });
  }, [companyId]);

  const handleChange = (e) => {
    setCompany({ ...company, [e.target.name]: e.target.value });

    if (errors[e.target.id]) {
      setErrors({ ...errors, [e.target.id]: null });
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();

    setErrors({});

    if (!validateForm()) return;

    const response = await fetch(
      `http://localhost:8080/api/companies/${companyId}`,
      {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(company),
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
    if (!company.companyName.trim())
      newErrors.companyName = "Eesnimi on kohustuslik";
    if (!company.companyCode.trim())
      newErrors.companyCode = "Firmakood on kohustuslik";
    if (
      !company.participantCount.toString().trim() ||
      company.participantCount == 0
    )
      newErrors.participantCount = "Osalejate arv on kohustuslik";
    if (!/^\d+$/.test(company.participantCount.toString()))
      errors.companyParticipants = "Osalejate arv peab olema number";
    if (!company.paymentMethod.trim())
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
                <p>Ettevõtte nimi:</p>
              </div>
              <div className="col-md-10">
                <input
                  type="text"
                  name="companyName"
                  value={company.companyName}
                  onChange={handleChange}
                />
                {errors.companyName && (
                  <div className="text-danger">{errors.companyName}</div>
                )}
              </div>
            </div>

            <div className="row justify-content-start">
              <div className="col-md-2">
                <p>Ettevõtte kood</p>
              </div>
              <div className="col-md-10">
                <input
                  type="text"
                  name="companyCode"
                  value={company.companyCode}
                  onChange={handleChange}
                />
                {errors.companyCode && (
                  <div className="text-danger">{errors.companyCode}</div>
                )}
              </div>
            </div>

            <div className="row justify-content-start">
              <div className="col-md-2">
                <p>Isikute arv</p>
              </div>
              <div className="col-md-10">
                <input
                  type="text"
                  name="participantCount"
                  value={company.participantCount}
                  onChange={handleChange}
                />
                {errors.participantCount && (
                  <div className="text-danger">{errors.participantCount}</div>
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
                  value={company.paymentMethod}
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
                  value={company.info}
                  onChange={handleChange}
                ></textarea>
                {errors.Info && (
                  <div className="text-danger">{errors.Info}</div>
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

export default CompanyView;
