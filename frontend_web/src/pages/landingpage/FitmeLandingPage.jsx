import React from "react";
import HeroSection from "./HeroSection";
import FeaturesSection from "./FeaturesSection";
import FitmeLogo from "../../assets/FitmeLogo.png";
import bgImage from "../../assets/bgimage2.webp";
import Navigation from "./Navigation";
import ReviewsSection from "./ReviewsSection";
import FooterSection from "./FooterSection";
import { useNavigate } from "react-router-dom";

const FitmeLandingPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen font-sans">
      <Navigation />
      <HeroSection />
      <FeaturesSection />
      <ReviewsSection />
      {/* Banner with workout image and CTA */}
      <section className="relative w-full mt-10" style={{ height: "500px" }}>
        {/* Background Image */}
        <div
          className="absolute inset-0 bg-cover bg-center"
          style={{
            backgroundImage: `url(${bgImage})`,
            backgroundPosition: "center 0%",
            filter: "brightness(0.65)",
          }}
        ></div>

        {/* Content Overlay */}
        <div className="relative h-full flex flex-col items-center justify-center text-white text-center px-4">
          <h2 className="text-4xl md:text-5xl font-bold mb-2">
            TRACK & PLAN
            <br />
            WORKOUTS
          </h2>
          <p className="text-xl mb-6 italic">all in one place with FITME</p>
          <button
            onClick={() => navigate("/register")}
            className="bg-fitme-blue hover:bg-fitme-blue-hover text-white font-medium py-2 px-6 rounded-md transition-colors"
          >
            Start Now
          </button>
        </div>
      </section>
      <FooterSection />
    </div>
  );
};

export default FitmeLandingPage;
