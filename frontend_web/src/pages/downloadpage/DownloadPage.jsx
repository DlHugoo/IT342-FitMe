import React from "react";
import { useNavigate } from "react-router-dom";
import Navigation from "../../pages/landingpage/Navigation";

const DownloadPage = () => {
  const navigate = useNavigate();
  return (
    <>
      <div className="min-h-screen font-sans">
        <Navigation />
      </div>
    </>
  );
};

export default DownloadPage;
