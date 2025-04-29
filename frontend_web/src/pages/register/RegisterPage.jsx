import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import FitmeLogo from "../../assets/FitmeLogo.png";
import axios from "../../api/axiosInstance";

const RegisterPage = () => {
  const navigate = useNavigate();
  const [currentStep, setCurrentStep] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedOptions, setSelectedOptions] = useState({
    goal: null,
    frequency: null,
  });
  const [formValues, setFormValues] = useState({
    age: "",
    weight: "",
    height: "",
    username: "",
    email: "",
    password: "",
  });

  // Questions data
  const questions = [
    {
      id: "goal",
      question: "What is your top goal?",
      options: [
        {
          title: "Maintaining",
          description:
            "General Fitness: Staying healthy and good cardiovascular health",
        },
        {
          title: "Bulking",
          description:
            "Strength Training: Focus on increasing muscle mass and strength",
        },
        {
          title: "Cutting",
          description:
            "Weight Loss: Emphasizing calorie burn and calorie deficit",
        },
      ],
    },
    {
      id: "frequency",
      question: "How often do you exercise per week?",
      options: [
        {
          title: "Rarely (0-1 times)",
          description: "",
        },
        {
          title: "Sometimes (2-3 times)",
          description: "",
        },
        {
          title: "Often (4+ times)",
          description: "",
        },
      ],
    },
    {
      id: "age",
      question: "What is your age?",
      inputType: "number",
      unit: "years",
      fieldName: "age",
    },
    {
      id: "bodyMetrics",
      question: "What is your weight and height?",
      inputType: "multiple",
      fields: [
        {
          id: "weight",
          label: "Weight",
          unit: "KG",
          fieldName: "weight",
        },
        {
          id: "height",
          label: "Height",
          unit: "CM",
          fieldName: "height",
        },
      ],
    },
    {
      id: "accountCreation",
      question: "You're almost there! Sign up now to get started",
      inputType: "account",
      fields: [
        {
          id: "username",
          label: "Username",
          fieldName: "username",
          type: "text",
        },
        {
          id: "email",
          label: "Email address",
          fieldName: "email",
          type: "email",
        },
        {
          id: "password",
          label: "Password",
          fieldName: "password",
          type: "password",
        },
      ],
      buttonText: "CREATE ACCOUNT",
      alternativeOption: {
        text: "Or continue with",
        button: "Sign in with Google",
      },
    },
  ];

  const handleOptionSelect = (questionId, optionTitle) => {
    setSelectedOptions({
      ...selectedOptions,
      [questionId]: optionTitle,
    });
  };

  const handleInputChange = (fieldName, value) => {
    setFormValues({
      ...formValues,
      [fieldName]: value,
    });
  };

  const createUser = async () => {
    setLoading(true);
    setError(null);

    try {
      // Parse numeric values to ensure they're sent as numbers
      const userData = {
        username: formValues.username,
        email: formValues.email,
        password: formValues.password,
        age: parseInt(formValues.age),
        weight: parseFloat(formValues.weight),
        height: parseFloat(formValues.height),
        // These fields aren't in your UserEntity but you can store them elsewhere if needed
        goal: selectedOptions.goal,
        frequency: selectedOptions.frequency,
      };

      // Call your backend API to create a user
      const response = await axios.post("/api/users", userData);

      console.log("User created successfully:", response.data);

      // Redirect to login or dashboard
      navigate("/login");
    } catch (err) {
      console.error("Error creating user:", err);
      setError(
        err.response?.data?.message ||
          "Failed to create account. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleContinue = () => {
    const currentQuestion = questions[currentStep - 1];

    // Validation based on question type
    if (currentQuestion.options) {
      // For option-based questions
      if (!selectedOptions[currentQuestion.id]) {
        return;
      }
    } else if (currentQuestion.inputType === "number") {
      // For single input questions
      if (!formValues[currentQuestion.fieldName]) {
        return;
      }
    } else if (currentQuestion.inputType === "multiple") {
      // For questions with multiple inputs
      const isValid = currentQuestion.fields.every(
        (field) => formValues[field.fieldName]
      );
      if (!isValid) {
        return;
      }
    } else if (currentQuestion.inputType === "account") {
      // For account creation
      const isValid = currentQuestion.fields.every(
        (field) => formValues[field.fieldName]
      );
      if (!isValid) {
        return;
      }

      // If we're at the account creation step and all fields are valid, create the user
      if (currentStep === questions.length) {
        createUser();
        return;
      }
    }

    if (currentStep < questions.length) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handleGoogleSignIn = () => {
    // Handle Google sign-in logic
    console.log("Signing in with Google");
  };

  const handleBack = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const currentQuestion = questions[currentStep - 1];

  // Render different question types
  const renderQuestionContent = () => {
    if (currentQuestion.options) {
      // Option selection type question
      return (
        <div className="w-full max-w-md space-y-4 mb-6">
          {currentQuestion.options.map((option, index) => (
            <div
              key={index}
              onClick={() =>
                handleOptionSelect(currentQuestion.id, option.title)
              }
              className={`
                border rounded-lg p-4 cursor-pointer transition-all
                ${
                  selectedOptions[currentQuestion.id] === option.title
                    ? "border-blue-400 border-2"
                    : "border-gray-300 hover:border-blue-200"
                }
              `}
            >
              <h3 className="text-lg font-semibold text-center mb-1">
                {option.title}
              </h3>
              {option.description && (
                <p className="text-gray-500 text-center text-sm">
                  {option.description}
                </p>
              )}
            </div>
          ))}
        </div>
      );
    } else if (currentQuestion.inputType === "number") {
      // Single input type question
      return (
        <div className="w-full max-w-md mb-6">
          <div className="mb-4">
            <label className="block text-lg font-bold text-gray-800 mb-2">
              {currentQuestion.id.charAt(0).toUpperCase() +
                currentQuestion.id.slice(1)}
            </label>
            <div className="relative">
              <input
                type="number"
                value={formValues[currentQuestion.fieldName]}
                onChange={(e) =>
                  handleInputChange(currentQuestion.fieldName, e.target.value)
                }
                className="w-full p-4 border border-gray-300 rounded-lg no-spinner"
                placeholder={`Enter your ${currentQuestion.id}`}
              />
              <span className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-500">
                {currentQuestion.unit}
              </span>
            </div>
          </div>
        </div>
      );
    } else if (currentQuestion.inputType === "multiple") {
      // Multiple input type question
      return (
        <div className="w-full max-w-md mb-6">
          {currentQuestion.fields.map((field) => (
            <div key={field.id} className="mb-4">
              <label className="block text-lg font-bold text-gray-800 mb-2">
                {field.label}
              </label>
              <div className="relative">
                <input
                  type="number"
                  value={formValues[field.fieldName]}
                  onChange={(e) =>
                    handleInputChange(field.fieldName, e.target.value)
                  }
                  className="w-full p-4 border border-gray-300 rounded-lg no-spinner"
                  placeholder={`Enter your ${field.label.toLowerCase()}`}
                />
                <span className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-500">
                  {field.unit}
                </span>
              </div>
            </div>
          ))}
        </div>
      );
    } else if (currentQuestion.inputType === "account") {
      // Account creation form
      return (
        <div className="w-full max-w-md mb-6">
          {currentQuestion.fields.map((field) => (
            <div key={field.id} className="mb-4">
              <label className="block text-base font-medium text-gray-800 mb-2">
                {field.label}
              </label>
              <div className="relative">
                <input
                  type={field.type}
                  value={formValues[field.fieldName]}
                  onChange={(e) =>
                    handleInputChange(field.fieldName, e.target.value)
                  }
                  className="w-full p-4 border border-gray-300 rounded-lg"
                  placeholder={`Enter your ${field.label.toLowerCase()}`}
                />
              </div>
            </div>
          ))}

          {error && (
            <div className="mb-4 p-3 bg-red-100 text-red-700 rounded-lg">
              {error}
            </div>
          )}

          <button
            onClick={handleContinue}
            disabled={!isContinueEnabled() || loading}
            className={`
              w-full py-3 px-4 rounded-lg text-white font-medium mb-6 flex justify-center items-center
              ${
                isContinueEnabled() && !loading
                  ? "bg-blue-400 hover:bg-blue-500"
                  : "bg-blue-300 cursor-not-allowed"
              }
            `}
          >
            {loading ? (
              <>
                <svg
                  className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  ></circle>
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
                Creating Account...
              </>
            ) : (
              currentQuestion.buttonText || "Continue"
            )}
          </button>

          {currentQuestion.alternativeOption && (
            <>
              <div className="flex items-center my-4">
                <div className="flex-1 h-px bg-gray-200"></div>
                <div className="px-4 text-gray-500">
                  {currentQuestion.alternativeOption.text}
                </div>
                <div className="flex-1 h-px bg-gray-200"></div>
              </div>

              <button
                onClick={handleGoogleSignIn}
                className="w-full py-3 px-4 border border-gray-300 rounded-lg text-gray-700 font-medium flex justify-center items-center"
              >
                <svg
                  width="24"
                  height="24"
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 48 48"
                  className="mr-2"
                >
                  <path
                    fill="#FFC107"
                    d="M43.611,20.083H42V20H24v8h11.303c-1.649,4.657-6.08,8-11.303,8c-6.627,0-12-5.373-12-12c0-6.627,5.373-12,12-12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C12.955,4,4,12.955,4,24c0,11.045,8.955,20,20,20c11.045,0,20-8.955,20-20C44,22.659,43.862,21.35,43.611,20.083z"
                  />
                  <path
                    fill="#FF3D00"
                    d="M6.306,14.691l6.571,4.819C14.655,15.108,18.961,12,24,12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C16.318,4,9.656,8.337,6.306,14.691z"
                  />
                  <path
                    fill="#4CAF50"
                    d="M24,44c5.166,0,9.86-1.977,13.409-5.192l-6.19-5.238C29.211,35.091,26.715,36,24,36c-5.202,0-9.619-3.317-11.283-7.946l-6.522,5.025C9.505,39.556,16.227,44,24,44z"
                  />
                  <path
                    fill="#1976D2"
                    d="M43.611,20.083H42V20H24v8h11.303c-0.792,2.237-2.231,4.166-4.087,5.571c0.001-0.001,0.002-0.001,0.003-0.002l6.19,5.238C36.971,39.205,44,34,44,24C44,22.659,43.862,21.35,43.611,20.083z"
                  />
                </svg>
                {currentQuestion.alternativeOption.button}
              </button>
            </>
          )}
        </div>
      );
    }
  };

  // Check if continue button should be enabled
  const isContinueEnabled = () => {
    if (currentQuestion.options) {
      return !!selectedOptions[currentQuestion.id];
    } else if (currentQuestion.inputType === "number") {
      return !!formValues[currentQuestion.fieldName];
    } else if (currentQuestion.inputType === "multiple") {
      return currentQuestion.fields.every(
        (field) => !!formValues[field.fieldName]
      );
    } else if (currentQuestion.inputType === "account") {
      return currentQuestion.fields.every(
        (field) => !!formValues[field.fieldName]
      );
    }
    return false;
  };

  return (
    <div className="min-h-screen flex flex-col items-center bg-white p-4 relative">
      {/* Style to remove spinner buttons from number inputs */}
      <style jsx>{`
        /* Hide arrows for Chrome, Safari, Edge, Opera */
        input.no-spinner::-webkit-outer-spin-button,
        input.no-spinner::-webkit-inner-spin-button {
          -webkit-appearance: none;
          margin: 0;
        }

        /* Hide arrows for Firefox */
        input.no-spinner {
          -moz-appearance: textfield;
        }
      `}</style>

      {/* Absolute positioned logo */}
      <div className="container mx-auto px-36 flex items-center">
        <div
          className="flex items-center mr-auto cursor-pointer"
          onClick={() => navigate("/")}
        >
          {/* Logo */}
          <img src={FitmeLogo} alt="Fitme Logo" className="h-10 w-10 mr-2" />
          <div className="text-3xl font-bold text-fitme-blue mr-auto">
            FITME
          </div>
        </div>
      </div>
      {/* Content container with margin to account for absolute logo */}
      <div className="w-full max-w-md flex flex-col items-center">
        {/* Progress bar */}
        <div className="w-full max-w-md h-2 bg-gray-200 rounded-full mb-8 mt-8">
          <div
            className="h-full bg-blue-400 rounded-full"
            style={{ width: `${(currentStep / questions.length) * 100}%` }}
          ></div>
        </div>

        {/* Navigation */}
        <div className="w-full max-w-md flex items-center mb-6">
          <button
            onClick={handleBack}
            className="p-2 text-gray-600"
            disabled={currentStep === 1}
          >
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
                d="M15 19l-7-7 7-7"
              />
            </svg>
          </button>
          <div className="text-blue-400 font-medium ml-auto">
            {currentStep}/{questions.length}
          </div>
        </div>

        {/* Question */}
        <div className="w-full max-w-md mb-6">
          <h1 className="text-2xl font-bold text-center text-gray-800">
            {currentQuestion.question}
          </h1>
        </div>

        {/* Question content - options or inputs */}
        {renderQuestionContent()}

        {/* Continue button - only show for non-account creation steps */}
        {currentQuestion.inputType !== "account" && (
          <button
            onClick={handleContinue}
            disabled={!isContinueEnabled()}
            className={`
              w-full max-w-md py-3 px-4 rounded-lg text-white font-medium
              ${
                isContinueEnabled()
                  ? "bg-blue-400 hover:bg-blue-500"
                  : "bg-blue-300 cursor-not-allowed"
              }
            `}
          >
            Continue
          </button>
        )}
      </div>
    </div>
  );
};

export default RegisterPage;
