import React from "react";
import FitmeLogo from "../../assets/FitmeLogo.png";
import AppStoreImage from "../../assets/app-store-badge.png";
import GooglePlayImage from "../../assets/google-play-badge.png";
import bgImage from "../../assets/bgimage2.webp";
import { useNavigate } from "react-router-dom";

const FooterSection = () => {
  const navigate = useNavigate();

  return (
    <>
      {/* Footer */}
      <footer className="bg-white py-12 px-4">
        <div className="container mx-auto max-w-6xl">
          {/* Logo and Divider */}
          <div className="mb-8">
            <div className="flex items-center mb-8">
              <img src={FitmeLogo} alt="FITME Logo" className="h-12" />
              <span className="text-fitme-blue text-3xl font-bold ml-4">
                FITME
              </span>
            </div>
            <hr className="border-gray-200" />
          </div>

          {/* Copyright and Download Links */}
          <div className="flex flex-col md:flex-row justify-between items-center">
            <div className="text-gray-500 mb-6 md:mb-0">
              © 2025 Jefit Inc. All rights reserved.
            </div>
            <div className="flex space-x-4">
              <a href="#" className="transition-opacity hover:opacity-80">
                <img
                  src={AppStoreImage}
                  alt="Download on App Store"
                  className="h-10"
                />
              </a>
              <a href="#" className="transition-opacity hover:opacity-80">
                <img
                  src={GooglePlayImage}
                  alt="Get it on Google Play"
                  className="h-10"
                />
              </a>
            </div>
          </div>
        </div>
      </footer>
    </>
  );
};

export default FooterSection;
