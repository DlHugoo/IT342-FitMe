import React from "react";
import FitmeLogo from "../../assets/FitmeLogo.png";
import { useNavigate } from "react-router-dom";

const Navigation = () => {
  const navigate = useNavigate();
  return (
    <>
      {/* Header */}
      <header className="sticky top-0 bg-white z-10">
        <div className="container mx-auto px-36 py-4 flex items-center">
          <div className="flex items-center mr-auto">
            {/* Logo */}
            <img src={FitmeLogo} alt="Fitme Logo" className="h-10 w-10 mr-2" />
            <div className="text-3xl font-bold text-fitme-blue mr-auto">
              FITME
            </div>
          </div>

          {/* Navigation - Desktop */}
          <nav className="hidden md:flex space-x-6 mr-6">
            <a
              href="#about"
              className="text-gray-700 hover:text-fitme-blue transition-colors"
            >
              About Us
            </a>
            <a
              onClick={() => navigate("/download")}
              className="text-gray-700 hover:text-fitme-blue transition-colors"
            >
              Download
            </a>
            <a
              onClick={() => navigate("/login")}
              className="text-gray-700 hover:text-fitme-blue transition-colors cursor-pointer"
            >
              Login
            </a>
          </nav>

          {/* CTA Button */}
          <button
            onClick={() => navigate("/register")}
            className="hidden md:block bg-fitme-blue text-white font-semibold py-2 px-4 rounded-lg hover:bg-fitme-blue-hover transition-colors"
          >
            Try FITME for free
          </button>

          {/* Mobile Menu Button */}
          <button className="md:hidden text-gray-700 ml-auto">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </button>
        </div>

        {/* Mobile Menu */}
        <div className="hidden md:hidden px-4 py-2 bg-gray-50">
          <a
            href="#about"
            className="block py-2 text-gray-700 hover:text-fitme-blue"
          >
            About Us
          </a>
          <a
            onClick={() => navigate("/download")}
            className="block py-2 text-gray-700 hover:text-fitme-blue"
          >
            Download
          </a>
          <a
            onClick={() => navigate("/login")}
            className="block py-2 text-gray-700 hover:text-fitme-blue"
          >
            Login
          </a>
          <button className="mt-3 w-full bg-fitme-blue text-white font-semibold py-2 px-4 rounded-lg hover:bg-fitme-blue-hover transition-colors">
            Try FITME for free
          </button>
        </div>
      </header>
    </>
  );
};

export default Navigation;
