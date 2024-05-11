// src/components/Footer.jsx
import React from "react";

function Footer() {
  return (
    <footer className="bg-custom-grey text-white text-lg-start">
      <div className="row text-center">
        <div className="col">
          <p>Curabitur</p>
          <p>Emauris</p>
          <p>Kfringilla</p>
          <p>Oin magna sem</p>
          <p>Kelementum</p>
        </div>
        <div className="col">
          <p>Fusce</p>
          <p>Econsectetur</p>
          <p>Omvulputate</p>
          <p>Nunc fringilla tellu</p>
        </div>
        <div className="col">
          <p>Kontakt</p>
          <p>
            <strong>Peakontor:Tallinnas</strong>
          </p>
          <p>Väike-Ameerika 1, 11415 Tallinn</p>
          <p>Telefon: 6054450</p>
          <p>Faks: 6053186</p>
        </div>
        <div className="col">
          <p>
            <strong>Harukontor: VÕrus</strong>
          </p>
          <p>Oja tn 7 (külastusaadress)</p>
          <p>Telefon: 6053330</p>
          <p>Faks: 6053155</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
