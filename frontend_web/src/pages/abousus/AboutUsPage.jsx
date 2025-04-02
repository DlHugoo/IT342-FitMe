import React from "react";
import { useNavigate } from "react-router-dom";
import Navigation from "../../pages/landingpage/Navigation";
import FooterSection from "../landingpage/FooterSection";
import AUImage from "../../assets/aboutus.jpg";
import WWAImage from "../../assets/au-1.png";
import TYPImage from "../../assets/au-2.png";

const AboutUsPage = () => {
  const navigate = useNavigate();
  return (
    <>
      <Navigation />
      {/* Banner with workout image and CTA */}
      <section className="relative w-full" style={{ height: "700px" }}>
        {/* Background Image */}
        <div
          className="absolute inset-0 bg-cover bg-center"
          style={{
            backgroundImage: `url(${AUImage})`,
            backgroundPosition: "center 50%",
            filter: "brightness(0.65)",
          }}
        ></div>

        {/* Content Overlay */}
        <div className="relative h-full flex flex-col items-center justify-center text-white text-center px-4">
          <p className="text-xl font-bold">OUR MISSION</p>
          <p className="text-9xl md:text-5xl font-extrabold mb-2 leading-6 tracking-wider">
            TO EMPOWER PEOPLE TO LIVE
            <br />
            WELL BY ENERGIZING <br />
            THEIR EVERY DAY.
          </p>
        </div>
      </section>
      {/* Alternating 2-Column, 3-Row Grid Section */}
      <section className="py-16 px-36">
        <div className="container mx-auto px-4">
          {/* Row 1: Text on left, Image on right */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-16 mt-10 items-center">
            {/* Right side - Image */}
            <div>
              <img
                src={WWAImage}
                alt="Workout Plans"
                className="mx-auto"
                width="500"
                height="400"
              />
            </div>
            {/* Left side - Text */}
            <div>
              <h2 className="text-2xl md:text-3xl font-bold text-fitme-blue mb-4">
                WHO WE ARE
              </h2>
              <p className="text-gray-800">
                We are a passionate team dedicated to making fitness accessible,
                <br />
                engaging, and personalized for everyone. Our goal is to help
                <br />
                users achieve their health and wellness goals through an
                <br />
                interactive and effective fitness platform.
              </p>
            </div>
          </div>

          {/* Row 2: Image on left, Text on right (alternating) */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-2 items-center">
            {/* Left side - Image */}
            <div>
              <img
                src={TYPImage}
                alt="Workout Plans"
                className="mx-auto"
                width="500"
                height="400"
              />
            </div>

            {/* Right side - Text */}
            <div className="md:order-2 order-1">
              <h2 className="text-2xl md:text-3xl font-bold text-fitme-blue mb-4">
                OUR VALUES
              </h2>
              <p className="text-gray-800">
                We believe that fitness is a lifelong journey. Our platform is
                <br />
                designed to help users build sustainable habits for a healthier
                <br />
                lifestyle. With guidance and engaging challenges, we make it
                <br />
                easier to stay consistent and reach your goals.
              </p>
            </div>
          </div>
        </div>
      </section>
      <FooterSection />
    </>
  );
};

export default AboutUsPage;
