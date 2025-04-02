import React from "react";
import HeroSection from "./HeroSection";
import FeaturesSection from "./FeaturesSection";
import FitmeLogo from "../../assets/FitmeLogo.png";
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
      <FooterSection />
    </div>
  );
};

export default FitmeLandingPage;
