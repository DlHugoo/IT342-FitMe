import React from "react";
import { useNavigate } from "react-router-dom";
import Navigation from "../../pages/landingpage/Navigation";
import AppStoreImage from "../../assets/app-store-badge.png";
import GooglePlayImage from "../../assets/google-play-badge.png";
import woPlansImg from "../../assets/wo-plans.png";
import workoutImg from "../../assets/workout.png";
import calendarImg from "../../assets/calendar.png";

const DownloadPage = () => {
  const navigate = useNavigate();
  return (
    <>
      <div className="min-h-screen font-sans">
        <Navigation />
        <div className="flex flex-col items-center justify-center min-h-screen bg-white">
          {/* Header Section */}
          <div className="text-center max-w-3xl mx-auto px-4 pt-10 pb-8">
            <h1 className="text-5xl font-bold mb-2">DOWNLOAD FITME NOW</h1>
            <p className="text-lg">
              Plan workouts, access elite plans, discover personalized metrics,
              <br />
              and connect with the FITME community
            </p>
          </div>

          {/* App Screenshots */}
          <div className="relative w-full max-w-3xl mx-auto h-96 mb-8">
            {/* Left Phone */}
            <div className="absolute left-4 md:left-20 top-8 w-64 transform -rotate-6 z-10">
              <img src={calendarImg} alt="Calendar view" className="w-full" />
            </div>

            {/* Middle Phone - Larger and in front */}
            <div className="absolute left-1/2 transform -translate-x-1/2 w-72 z-30">
              <img src={woPlansImg} alt="Workout plans" className="w-full" />
            </div>

            {/* Right Phone */}
            <div className="absolute right-4 md:right-20 top-8 w-64 transform rotate-6 z-10">
              <img src={workoutImg} alt="Workout view" className="w-full" />
            </div>
          </div>

          {/* Download Section */}
          <div className="w-full bg-blue-footer py-10 px-4">
            <div className="flex flex-col items-center justify-center">
              {/* App Store Badges */}
              <div className="flex flex-wrap justify-center gap-4 mb-8">
                <a href="#" className="w-40">
                  <img
                    src={AppStoreImage}
                    alt="Download on App Store"
                    className="w-full"
                  />
                </a>
                <a href="#" className="w-40">
                  <img
                    src={GooglePlayImage}
                    alt="Get it on Google Play"
                    className="w-full"
                  />
                </a>
              </div>

              {/* Logo */}
              <div className="text-white text-5xl font-bold tracking-wider mb-8">
                FITME
              </div>

              {/* Footer */}
              <div className="text-white text-sm">
                © 2025 Fitme Inc. All rights reserved.
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default DownloadPage;
