import React from "react";
import { Link } from "react-router-dom";
import homeIcon from "../assets/images/logo.svg";
import symbol from "../assets/images/symbol.svg";

function Navbar() {
  return (
    <nav className="navbar navbar-expand-lg mt-3 bg-secondary">
      <img className="mx-5" src={homeIcon} alt="Home" srcset="" />
      <button
        className="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav ms-auto me-auto">
          <li className="nav-item">
            <Link className="navbar-brand d-flex align-items-center" to="/">
              <div className="bg-primary py-4 mx-5 text-white">AVALEHT</div>
            </Link>
          </li>

          <li className="nav-item">
            <Link className="nav-link" to="/add-event">
              <div className="text-black fs-5">ÜRITUSE LISAMINE</div>
            </Link>
          </li>
        </ul>

        <ul className="navbar-nav">
          <li className="nav-item">
            <img
              src={symbol}
              alt="symbol"
              className="d-flex me-3"
              style={{ width: "60px" }}
            />
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
