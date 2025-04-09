import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import UserWhiteIcon from "../../assets/user-white.png";

const Headerbar = () => {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login"); // or location.reload();
  };

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <header className="bg-blue-header text-white p-4 relative flex items-center justify-end">
      {/* Centered Title */}
      <h1 className="absolute left-1/2 transform -translate-x-1/2 text-2xl font-semibold">
        Admin Management
      </h1>

      {/* Right-aligned Admin Info */}
      <div className="relative" ref={dropdownRef}>
        <div
          className="flex items-center cursor-pointer"
          onClick={() => setDropdownOpen((prev) => !prev)}
        >
          <img src={UserWhiteIcon} alt="User" className="h-4 w-4 mr-2" />
          <span className="mr-1">admin</span>
          <div className="text-white text-[10px]">▼</div>
        </div>

        {dropdownOpen && (
          <div className="absolute left-0 mt-2 w-15 bg-white text-gray-700 rounded shadow-md z-10">
            <button
              onClick={handleLogout}
              className="block w-full text-left px-4 py-2 text-sm hover:bg-gray-100 rounded"
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  );
};

export default Headerbar;
