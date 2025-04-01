import React from "react";
import { useNavigate } from "react-router-dom";
import PhoneImage from "../../assets/phone.webp";

const HeroSection = () => {
  const navigate = useNavigate();

  return (
    <>
      {/* Hero Section */}
      <section className="py-16 md:py-18 px-4">
        <div className="container mx-auto text-center max-w-3xl">
          <h1 className="text-3xl md:text-5xl font-bold mb-6 text-gray-800">
            ACHIEVE YOUR <br />
            FITNESS GOALS TODAY
          </h1>
          <p className="text-lg md:text-xl text-gray-600 mb-10 leading-relaxed">
            Plan workouts, access elite plans, discover personalized metrics,
            <br /> and connect with the FITME community.
          </p>
          <button
            onClick={() => navigate("/register")}
            className="bg-fitme-blue font-semibold text-white py-3 px-8 rounded-lg text-lg hover:bg-fitme-blue-hover transition-colors shadow-lg"
          >
            START YOUR FITQUIZ
          </button>
        </div>
      </section>

      {/* Phone Image Section with Blue Background */}
      <section className="relative w-full">
        {/* Blue background covering 60% from bottom */}
        <div
          className="absolute bottom-0 left-0 right-0 h-3/5 bg-blue-600"
          style={{ backgroundColor: "#1174CB" }}
        ></div>

        {/* Content container */}
        <div className="container mx-auto relative px-4 md:px-0">
          {/* Phone image in center */}
          <div className="flex justify-center">
            <div className>
              <img
                src={PhoneImage}
                alt="FITME App"
                className="mx-auto"
                width="537"
                height="604"
              />
            </div>
          </div>

          {/* Stats positioned lower in the blue section */}
          <div className="absolute bottom-20 left-0 right-0">
            <div className="flex flex-col md:flex-row items-center justify-between">
              {/* Left side stats */}
              <div className="text-white text-center md:text-right md:w-1/3 mb-8 md:mb-0">
                <div className="mb-10">
                  <div className="text-4xl md:text-5xl font-bold mb-2">
                    42,000+
                  </div>
                  <div className="text-xl md:text-2xl">Five star Ratings</div>
                </div>

                <div>
                  <div className="text-2xl md:text-xl font-bold mb-2">
                    2023 Best App
                  </div>
                  <div className="text-sm md:text-base">
                    Featured by Men's
                    <br /> Health, PC Magazine,
                    <br className="hidden md:block" />
                    USA TODAY, and more
                  </div>
                </div>
              </div>

              {/* Empty center space where phone appears above */}
              <div className="md:w-1/3"></div>

              {/* Right side stats */}
              <div className="text-white text-center md:text-left md:w-1/3 mt-8 md:mt-0">
                <div className="mb-10">
                  <div className="text-4xl md:text-5xl font-bold mb-2">
                    20M+
                  </div>
                  <div className="text-xl md:text-2xl">Downloads</div>
                </div>

                <div>
                  <div className="text-4xl md:text-5xl font-bold mb-2">
                    12M+
                  </div>
                  <div className="text-xl md:text-2xl">Body Builders</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default HeroSection;
