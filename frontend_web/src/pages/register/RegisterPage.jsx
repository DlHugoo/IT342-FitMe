import React, { useState } from "react";
import FitmeLogo from "../../assets/FitmeLogo.png";

const steps = [
  {
    id: 1,
    question: "What is your top goal?",
    type: "select",
    options: [
      {
        label: "Maintaining",
        description:
          "General Fitness: Staying healthy and having good cardiovascular health",
      },
      {
        label: "Bulking",
        description:
          "Strength Training: Focus on increasing muscle mass and strength",
      },
      {
        label: "Cutting",
        description:
          "Weight Loss: Emphasizing calorie burn and creating a caloric deficit",
      },
    ],
  },
  {
    id: 2,
    question: "What is your Gender?",
    type: "select",
    options: [{ label: "Male" }, { label: "Female" }],
  },
  {
    id: 3,
    question: "What is your age?",
    type: "input",
    placeholder: "Enter your age",
    suffix: "years",
  },
];

const RegisterPage = () => {
  const [currentStep, setCurrentStep] = useState(0);
  const [answers, setAnswers] = useState({});

  const handleSelection = (option) => {
    setAnswers({ ...answers, [currentStep]: option });
  };

  const handleInputChange = (event) => {
    setAnswers({ ...answers, [currentStep]: event.target.value });
  };

  const handleContinue = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handleBack = () => {
    if (currentStep > 0) {
      setCurrentStep(currentStep - 1);
    }
  };

  return (
    <div className="min-h-screen bg-white flex flex-col items-center p-4">
      {/* Header */}
      <div className="flex items-center mr-auto pl-32">
        <img src={FitmeLogo} alt="Fitme Logo" className="h-10 w-10 mt-2" />
        <div className="text-3xl font-bold text-fitme-blue mr-auto">FITME</div>
      </div>

      {/* Progress Bar */}
      <div className="w-full max-w-md my-6">
        <div className="h-2 bg-gray-200 rounded-full">
          <div
            className="h-full bg-blue-400 rounded-full"
            style={{ width: `${((currentStep + 1) / steps.length) * 100}%` }}
          ></div>
        </div>
        <div className="flex justify-between mt-1 text-sm text-gray-500">
          {currentStep > 0 && (
            <button onClick={handleBack} className="text-blue-500">
              ← Back
            </button>
          )}
          <span>
            {currentStep + 1}/{steps.length}
          </span>
        </div>
      </div>

      {/* Question */}
      <div className="w-full max-w-md mb-6 text-center">
        <h1 className="text-2xl font-bold mb-4">
          {steps[currentStep].question}
        </h1>

        {/* Render Select Buttons */}
        {steps[currentStep].type === "select" && (
          <div className="space-y-4">
            {steps[currentStep].options.map((option) => (
              <button
                key={option.label}
                className={`w-full border rounded-lg p-6 transition ${
                  answers[currentStep] === option.label
                    ? "border-blue-400"
                    : "border-gray-300"
                }`}
                onClick={() => handleSelection(option.label)}
              >
                <h2 className="font-bold text-center mb-2">{option.label}</h2>
                {option.description && (
                  <p className="text-gray-500 text-center text-sm">
                    {option.description}
                  </p>
                )}
              </button>
            ))}
          </div>
        )}

        {/* Render Input Field */}
        {steps[currentStep].type === "input" && (
          <div className="flex items-center border rounded-lg p-4">
            <input
              type="number"
              className="w-full outline-none"
              placeholder={steps[currentStep].placeholder}
              value={answers[currentStep] || ""}
              onChange={handleInputChange}
            />
            {steps[currentStep].suffix && (
              <span className="text-gray-500 ml-2">
                {steps[currentStep].suffix}
              </span>
            )}
          </div>
        )}
      </div>

      {/* Continue Button */}
      <button
        className={`w-full max-w-md p-4 rounded-lg font-bold text-white ${
          answers[currentStep]
            ? "bg-blue-400 hover:bg-blue-500"
            : "bg-gray-300 opacity-90 cursor-not-allowed"
        }`}
        onClick={handleContinue}
        disabled={!answers[currentStep]}
      >
        Continue
      </button>
    </div>
  );
};

export default RegisterPage;
