import { useState, useEffect } from "react";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const WorkoutManagementPage = () => {
  const [workouts, setWorkouts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [newWorkout, setNewWorkout] = useState({
    title: "",
    difficulty: "",
    days: 1,
  });

  useEffect(() => {
    fetchWorkouts();
  }, []);

  const fetchWorkouts = async () => {
    try {
      const res = await axios.get("/api/workouts", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setWorkouts(res.data);
    } catch (err) {
      console.error("Failed to fetch workouts", err);
    }
  };

  const handleAddWorkout = async () => {
    if (!newWorkout.title || !newWorkout.difficulty || newWorkout.days < 1) {
      alert("Please fill in all fields correctly.");
      return;
    }

    try {
      await axios.post("/api/workouts", newWorkout, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setNewWorkout({ title: "", difficulty: "", days: 1 });
      setShowModal(false);
      fetchWorkouts();
    } catch (err) {
      console.error("Failed to add workout", err);
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar />
      <div className="flex-1 flex flex-col">
        <Headerbar />

        <div className="bg-blue-header2 text-white px-4 pb-0">
          <div className="inline-block px-4 py-2">Workouts</div>
        </div>

        <div className="flex-1 p-6">
          <div className="flex items-center justify-between mb-6">
            <div className="w-1/3" />
            <div className="w-1/3 flex justify-center">
              <div className="relative w-96">
                <input
                  type="text"
                  placeholder="Search"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full border border-gray-300 rounded p-2 pl-10"
                />
                <div className="absolute left-3 top-3 text-gray-400">
                  <SearchIcon />
                </div>
              </div>
            </div>
            <div className="w-1/3 flex justify-end">
              <button
                className="bg-fitme-blue text-white px-4 py-2 rounded shadow hover:bg-fitme-blue-hover"
                onClick={() => setShowModal(true)}
              >
                Add Workout
              </button>
            </div>
          </div>

          <div className="bg-white rounded-md shadow">
            <table className="w-full table-fixed">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-4 px-20 text-left font-medium">ID</th>
                  <th className="py-4 px-6 text-left font-medium">TITLE</th>
                  <th className="py-4 px-6 text-left font-medium">
                    DIFFICULTY
                  </th>
                  <th className="py-4 px-6 text-left font-medium">DAYS</th>
                </tr>
              </thead>
              <tbody>
                {workouts
                  .filter((w) =>
                    w.title.toLowerCase().includes(searchTerm.toLowerCase())
                  )
                  .map((w) => (
                    <tr
                      key={w.workoutId}
                      className="border-t border-gray-200 hover:bg-gray-50"
                    >
                      <td className="py-4 px-20">{w.workoutId}</td>
                      <td className="py-4 px-6">{w.title}</td>
                      <td className="py-4 px-6">{w.difficulty}</td>
                      <td className="py-4 px-6">{w.days}</td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {showModal && (
        <Modal
          title="Add Workout"
          onClose={() => setShowModal(false)}
          onSubmit={handleAddWorkout}
        >
          <input
            className="w-full border border-gray-300 rounded p-2 mb-4"
            placeholder="Title"
            value={newWorkout.title}
            onChange={(e) =>
              setNewWorkout((prev) => ({ ...prev, title: e.target.value }))
            }
          />

          <select
            className="w-full border border-gray-300 rounded p-2 mb-4"
            value={newWorkout.difficulty}
            onChange={(e) =>
              setNewWorkout((prev) => ({ ...prev, difficulty: e.target.value }))
            }
          >
            <option value="">Select Difficulty</option>
            <option value="Easy">Easy</option>
            <option value="Medium">Medium</option>
            <option value="Hard">Hard</option>
          </select>

          <input
            type="number"
            className="w-full border border-gray-300 rounded p-2 mb-4"
            placeholder="Days"
            value={newWorkout.days}
            onChange={(e) =>
              setNewWorkout((prev) => ({
                ...prev,
                days: parseInt(e.target.value),
              }))
            }
            min={1}
          />
        </Modal>
      )}
    </div>
  );
};

const Modal = ({ title, onClose, onSubmit, children }) => (
  <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div className="bg-white rounded shadow-lg w-96 relative">
      <div className="bg-blue-header text-white px-4 py-3 rounded-t flex justify-between items-center">
        <h2 className="text-lg font-semibold">{title}</h2>
        <button className="text-white text-xl leading-none" onClick={onClose}>
          ×
        </button>
      </div>
      <div className="p-6">
        {children}
        <div className="flex justify-between mt-6">
          <button
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow"
            onClick={onSubmit}
          >
            Save
          </button>
          <button
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow"
            onClick={onClose}
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
);

const SearchIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    className="h-5 w-5"
    fill="none"
    viewBox="0 0 24 24"
    stroke="currentColor"
  >
    <path
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
    />
  </svg>
);

export default WorkoutManagementPage;
