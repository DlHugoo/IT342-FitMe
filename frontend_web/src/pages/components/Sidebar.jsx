import { useNavigate, useLocation } from "react-router-dom";
import FitmeLogo from "../../assets/FitmeLogo.png";
import UserIcon from "../../assets/user.png";
import WorkoutSetIcon from "../../assets/workoutset.png";
import ExerciseIcon from "../../assets/exercise.png";

const Sidebar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const getActiveItem = () => {
    if (location.pathname.startsWith("/user")) return "User";
    if (location.pathname.startsWith("/workout")) return "Workout Set";
    if (location.pathname.startsWith("/exercise")) return "Exercises";
    return "";
  };

  const activeItem = getActiveItem();

  return (
    <div className="fixed top-0 left-0 h-screen w-60 bg-white border-r border-gray-200 flex flex-col justify-between z-50">
      {/* Logo */}
      <div className="p-4 px-10 mt-3">
        <div className="flex items-center cursor-pointer">
          <img src={FitmeLogo} alt="Fitme Logo" className="h-10 w-10 mr-2" />
          <div className="text-3xl font-bold text-fitme-blue">FITME</div>
        </div>
      </div>

      {/* Menu Items */}
      <div className="mt-5 flex-grow">
        <SidebarItem
          icon={<img src={UserIcon} alt="User" className="h-6 w-6" />}
          text="User"
          active={activeItem === "User"}
          onClick={() => navigate("/user")}
        />
        <SidebarItem
          icon={
            <img src={WorkoutSetIcon} alt="Workout Set" className="h-6 w-6" />
          }
          text="Workout Set"
          active={activeItem === "Workout Set"}
          onClick={() => navigate("/workout")}
        />
        <SidebarItem
          icon={<img src={ExerciseIcon} alt="Exercises" className="h-6 w-6" />}
          text="Exercises"
          active={activeItem === "Exercises"}
          onClick={() => navigate("/exercise")}
        />
      </div>

      {/* Footer */}
      <div className="p-4 text-sm text-gray-500 text-center">
        © 2025 Fitme Inc.
      </div>
    </div>
  );
};

const SidebarItem = ({ icon, text, active, onClick }) => {
  return (
    <div
      className={`flex items-center pl-4 py-3 cursor-pointer relative ${
        active ? "" : "opacity-50"
      }`}
      onClick={onClick}
    >
      {active && (
        <div className="absolute left-0 top-0 bottom-0 w-1 bg-fitme-blue"></div>
      )}
      <div className="mr-3 text-fitme-blue">{icon}</div>
      <div
        className={`text-lg text-fitme-blue ${
          active ? "font-bold" : "font-normal"
        }`}
      >
        {text}
      </div>
    </div>
  );
};

export default Sidebar;
