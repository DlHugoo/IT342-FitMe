import { useState, useEffect } from "react";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const WorkoutManagementPage = () => {
  const [workouts, setWorkouts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [selectedWorkout, setSelectedWorkout] = useState(null);
  const [workoutToDelete, setWorkoutToDelete] = useState(null);

  const [newWorkout, setNewWorkout] = useState({
    title: "",
    difficulty: "",
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

      // Fetch days for each workout
      const workoutsWithDays = await Promise.all(
        res.data.map(async (workout) => {
          const daysRes = await axios.get(
            `/api/workout-days/${workout.workoutId}`,
            {
              headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
              },
            }
          );
          return { ...workout, days: daysRes.data.length };
        })
      );

      setWorkouts(workoutsWithDays);
    } catch (err) {
      console.error("Failed to fetch workouts", err);
    }
  };

  const handleAddWorkout = async () => {
    if (!newWorkout.title || !newWorkout.difficulty) {
      alert("Please fill in all fields correctly.");
      return;
    }

    try {
      await axios.post("/api/workouts", newWorkout, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setNewWorkout({ title: "", difficulty: "" });
      setShowModal(false);
      fetchWorkouts();
    } catch (err) {
      console.error("Failed to add workout", err);
    }
  };

  const handleEditClick = (workout) => {
    setSelectedWorkout(workout);
    setNewWorkout({
      title: workout.title,
      difficulty: workout.difficulty,
    });
    setShowModal(true);
  };

  const handleUpdateWorkout = async () => {
    try {
      await axios.put(
        `/api/workouts/${selectedWorkout.workoutId}`,
        newWorkout,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      setShowModal(false);
      setSelectedWorkout(null);
      fetchWorkouts();
    } catch (err) {
      console.error("Failed to update workout", err);
    }
  };

  const handleDeleteClick = (workout) => {
    setWorkoutToDelete(workout);
  };

  const confirmDelete = async () => {
    try {
      await axios.delete(`/api/workouts/${workoutToDelete.workoutId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setWorkoutToDelete(null);
      fetchWorkouts();
    } catch (err) {
      console.error("Failed to delete workout", err);
    }
  };

  return (
    <div className="flex bg-gray-100 min-h-screen">
      <Sidebar />
      <div className="ml-60 flex flex-col flex-grow">
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
                onClick={() => {
                  setSelectedWorkout(null);
                  setNewWorkout({ title: "", difficulty: "" });
                  setShowModal(true);
                }}
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
                  <th className="py-4 px-6 text-left font-medium">ACTIONS</th>
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
                      <td className="py-4 px-6 text-blue-600 cursor-pointer">
                        <a href={`/workouts/${w.workoutId}/days`}>{w.title}</a>
                      </td>
                      <td className="py-4 px-6">{w.difficulty}</td>
                      <td className="py-4 px-6">{w.days}</td>
                      <td className="py-4 px-6">
                        <div className="flex gap-3 items-center">
                          <button
                            className="text-blue-600 hover:text-blue-800"
                            onClick={() => handleEditClick(w)}
                            title="Edit"
                          >
                            <EditIcon />
                          </button>
                          <button
                            className="text-red-500 hover:text-red-700"
                            onClick={() => handleDeleteClick(w)}
                            title="Delete"
                          >
                            <DeleteIcon />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {showModal && (
        <Modal
          title={selectedWorkout ? "Update Workout" : "Add Workout"}
          onClose={() => setShowModal(false)}
          onSubmit={selectedWorkout ? handleUpdateWorkout : handleAddWorkout}
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
        </Modal>
      )}

      {workoutToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-md p-6 shadow-lg w-80 relative">
            <button
              className="absolute top-2 right-3 text-gray-500"
              onClick={() => setWorkoutToDelete(null)}
            >
              ✕
            </button>
            <h2 className="text-center text-lg font-semibold text-gray-800 mb-6">
              Are you sure you want <br /> to delete this workout?
            </h2>
            <div className="flex justify-between px-4">
              <button
                className="border border-gray-400 text-gray-700 px-4 py-2 rounded"
                onClick={() => setWorkoutToDelete(null)}
              >
                Cancel
              </button>
              <button
                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
                onClick={confirmDelete}
              >
                Delete
              </button>
            </div>
          </div>
        </div>
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

const EditIcon = () => (
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
      d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
    />
  </svg>
);

const DeleteIcon = () => (
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
      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
    />
  </svg>
);

export default WorkoutManagementPage;
