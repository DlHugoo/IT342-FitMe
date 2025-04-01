import React from "react";
import WPlans from "../../assets/wo-plans.png";
import WOut from "../../assets/workout.png";
import WCalendar from "../../assets/calendar.png";

const FeaturesSection = () => {
  return (
    <>
      {/* Alternating 2-Column, 3-Row Grid Section */}
      <section className="py-16 px-36">
        <div className="container mx-auto px-4">
          {/* Row 1: Text on left, Image on right */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-16 items-center">
            {/* Left side - Text */}
            <div>
              <h2 className="text-2xl md:text-3xl font-bold text-fitme-blue mb-4">
                FIND A WORKOUT PLAN
              </h2>
              <p className="text-gray-800">
                Choose from thousands of expert-designed workout plans tailored{" "}
                <br /> to your fitness goals, lifestyle, and experience level.
              </p>
            </div>

            {/* Right side - Image */}
            <div>
              <img
                src={WPlans}
                alt="Workout Plans"
                className="mx-auto"
                width="250"
                height="300"
              />
            </div>
          </div>

          {/* Row 2: Image on left, Text on right (alternating) */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-16 items-center">
            {/* Left side - Image */}
            <div>
              <img
                src={WOut}
                alt="Workout Plans"
                className="mx-auto"
                width="250"
                height="300"
              />
            </div>

            {/* Right side - Text */}
            <div className="md:order-2 order-1">
              <h2 className="text-2xl md:text-3xl font-bold text-fitme-blue mb-4">
                TRACK YOUR PROGRESS
              </h2>
              <p className="text-gray-800">
                Log workouts, monitor strength gains, and track improvements
                over time. Stay motivated with detailed progress tracking and
                achieve your fitness goals efficiently.
              </p>
            </div>
          </div>

          {/* Row 3: Text on left, Image on right (alternating back) */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-center">
            {/* Left side - Text */}
            <div>
              <h2 className="text-2xl md:text-3xl font-bold text-fitme-blue mb-4">
                RECEIVE DAILY REMINDERS
              </h2>
              <p className="text-gray-800">
                Get personalized notifications to stay on track, never miss a
                workout, and build consistent fitness habits for long-term
                success.
              </p>
            </div>

            {/* Right side - Image */}
            <div>
              <img
                src={WCalendar}
                alt="Workout Plans"
                className="mx-auto"
                width="250"
                height="300"
              />
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default FeaturesSection;
