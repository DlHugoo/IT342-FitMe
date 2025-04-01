import React from "react";

import { useNavigate } from "react-router-dom";
import User1Image from "../../assets/user1.png";
import User2Image from "../../assets/user2.png";
import User3Image from "../../assets/user3.png";
import User4Image from "../../assets/user4.png";

const ReviewsSection = () => {
  const navigate = useNavigate();

  // User review data
  const reviews = [
    {
      username: "@Bigdaddydmun",
      image: User1Image,
      review:
        "Chest and abs day!! Felt great with a good pump. Shirt seemed fitting since I was starting a new routine this week.",
    },
    {
      username: "@KimSoohyun",
      image: User2Image,
      review: "Pull day pump, starting to see more definition",
    },
    {
      username: "@Khokak",
      image: User3Image,
      review:
        "Chest and abs day!! Felt great with a good pump. Shirt seemed fitting since I was starting a new routine this week.",
    },
    {
      username: "@mcshnizzle",
      image: User4Image,
      review:
        "Work is taking up far more time than I expected and my workouts have suffered...here's to finding a way and getting back on track in March!!",
    },
  ];

  return (
    <section className="py-16 px-4">
      <div className="container mx-auto max-w-6xl">
        {/* Section header */}
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Connect with over 12 million FITME members
          </h2>
          <p className="text-gray-700 max-w-3xl mx-auto">
            We invite you and your friends to join us and support each other in
            <br />
            our active workout community.
          </p>
        </div>

        {/* User reviews grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {reviews.map((review, index) => (
            <div
              key={index}
              className="bg-white rounded-3xl overflow-hidden shadow-md"
            >
              {/* User image */}
              <div className="h-64 overflow-hidden">
                <img
                  src={review.image}
                  alt={`${review.username} profile`}
                  className="w-full h-full object-cover"
                />
              </div>

              {/* Username and review */}
              <div className="p-4">
                <h3 className="font-bold text-lg mb-2">{review.username}</h3>
                <p className="text-gray-700 text-sm">{review.review}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Join button */}
        <div className="text-center mt-12">
          <button
            onClick={() => navigate("/register")}
            className="bg-fitme-blue hover:bg-fitme-blue-hover text-white font-medium py-2 px-6 rounded-lg transition-colors"
          >
            Join Now
          </button>
        </div>
      </div>
    </section>
  );
};

export default ReviewsSection;
