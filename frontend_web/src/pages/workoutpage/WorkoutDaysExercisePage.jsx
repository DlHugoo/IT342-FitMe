import { useParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const WorkoutDaysExercisePage = () => {
  const { id, dayId } = useParams(); // id = workoutId
  const [exercises, setExercises] = useState([]);
  const [dayNumber, setDayNumber] = useState(null);
  const [workoutTitle, setWorkoutTitle] = useState("");
  const [allExercises, setAllExercises] = useState([]);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newExercise, setNewExercise] = useState({
    exercise: { exerciseId: null },
    reps: "",
    duration: "",
  });

  useEffect(() => {
    if (!dayId || !id) return;
    fetchWorkoutDay();
    fetchDayExercises();
    fetchWorkoutTitle();
    fetchAllExercises(); // 👈 new
  }, [dayId, id]);

  const fetchWorkoutDay = async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      console.error("No token found. Cannot fetch workout day.");
      setDayNumber("Unknown");
      return;
    }

    try {
      const res = await axios.get(`/api/workout-days/${dayId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDayNumber(res.data.dayNumber);
    } catch (err) {
      console.error(
        "Failed to fetch workout day",
        err.response?.data || err.message
      );
      setDayNumber("Unknown");
    }
  };

  const fetchDayExercises = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const res = await axios.get(`/api/day-exercises/${dayId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const sorted = res.data.sort((a, b) => a.id - b.id);
      setExercises(sorted);
    } catch (err) {
      console.error("Failed to fetch exercises", err);
      setExercises([]);
    }
  };

  const fetchWorkoutTitle = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const res = await axios.get(`/api/workouts/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setWorkoutTitle(res.data.title);
    } catch (err) {
      console.error("Failed to fetch workout title", err);
    }
  };

  const fetchAllExercises = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const res = await axios.get(`/api/exercises`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setAllExercises(res.data);
    } catch (err) {
      console.error("Failed to fetch exercise list", err);
    }
  };

  const handleEdit = (exercise) => {
    console.log("Edit clicked for:", exercise);
    // TODO: Open modal for editing exercise
  };

  const handleDelete = async (exerciseId) => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      await axios.delete(`/api/day-exercises/${exerciseId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchDayExercises(); // Refresh list
    } catch (err) {
      console.error("Failed to delete exercise", err);
    }
  };

  const handleAddExercise = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      await axios.post(
        "/api/day-exercises",
        {
          workoutDay: { dayId: parseInt(dayId) },
          exercise: { exerciseId: newExercise.exercise.exerciseId },
          reps: parseInt(newExercise.reps),
          duration: parseInt(newExercise.duration),
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setShowAddModal(false);
      setNewExercise({
        exercise: { exerciseId: null },
        reps: "",
        duration: "",
      });
      fetchDayExercises(); // refresh table
    } catch (err) {
      console.error("Failed to add exercise", err);
    }
  };

  return (
    <div className="flex bg-gray-100 min-h-screen">
      <Sidebar />
      <div className="ml-60 flex flex-col flex-grow">
        <Headerbar />

        <div className="bg-blue-header2 text-white px-4 pb-0">
          <div className="inline-block px-4 py-2">
            <Link to="/workout" className="hover:underline">
              Workouts
            </Link>{" "}
            <span className="mx-2">›</span>
            <Link to={`/workout/${id}`} className="hover:underline">
              {workoutTitle || "Loading..."}
            </Link>{" "}
            <span className="mx-2">›</span>
            Day {dayNumber ?? dayId}
          </div>
        </div>

        <div className="flex-1 p-6">
          <div className="flex items-center justify-between mb-4">
            <div className="text-lg font-semibold text-blue-header">
              Exercises for Day {dayNumber ?? dayId}
            </div>
            <button
              className="bg-fitme-blue text-white px-4 py-2 rounded shadow hover:bg-fitme-blue-hover"
              onClick={() => setShowAddModal(true)}
            >
              Add Exercise
            </button>
          </div>

          <div className="bg-white rounded-md shadow overflow-x-auto">
            <table className="min-w-full table-fixed">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-4 px-6 text-left font-medium w-1/4">
                    Exercise Name
                  </th>
                  <th className="py-4 px-6 text-left font-medium w-1/4">
                    Reps
                  </th>
                  <th className="py-4 px-6 text-left font-medium w-1/4">
                    Duration (sec)
                  </th>
                  <th className="py-4 px-6 text-left font-medium w-1/4">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody>
                {exercises.length > 0 ? (
                  exercises.map((ex) => (
                    <tr
                      key={ex.id}
                      className="border-t border-gray-200 hover:bg-gray-50"
                    >
                      <td className="py-4 px-6">{ex.exerciseName}</td>
                      <td className="py-4 px-6">{ex.reps ?? "—"}</td>
                      <td className="py-4 px-6">{ex.duration ?? "—"}</td>
                      <td className="py-4 px-6">
                        <div className="flex items-center gap-4">
                          <button
                            className="text-blue-500 hover:text-blue-700"
                            onClick={() => handleEdit(ex)}
                            title="Edit"
                          >
                            <EditIcon />
                          </button>
                          <button
                            className="text-red-500 hover:text-red-700"
                            onClick={() => handleDelete(ex.id)}
                            title="Delete"
                          >
                            <DeleteIcon />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="4" className="text-center py-6 text-gray-500">
                      No exercises found for this day.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Add Exercise Modal */}
      {showAddModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-96">
            <h2 className="text-lg font-semibold mb-4 text-blue-header">
              Add Exercise
            </h2>

            <label className="block mb-2 text-sm font-medium">
              Select Exercise
            </label>
            <select
              className="w-full border border-gray-300 rounded p-2 mb-4"
              value={newExercise.exercise.exerciseId || ""}
              onChange={(e) =>
                setNewExercise({
                  ...newExercise,
                  exercise: { exerciseId: parseInt(e.target.value) },
                })
              }
            >
              <option value="" disabled>
                Select an exercise
              </option>
              {allExercises.map((ex) => (
                <option key={ex.exerciseId} value={ex.exerciseId}>
                  {ex.name}
                </option>
              ))}
            </select>

            <label className="block mb-2 text-sm font-medium">Reps</label>
            <input
              type="number"
              value={newExercise.reps}
              onChange={(e) =>
                setNewExercise({ ...newExercise, reps: e.target.value })
              }
              className="w-full border border-gray-300 rounded p-2 mb-4"
            />

            <label className="block mb-2 text-sm font-medium">
              Duration (sec)
            </label>
            <input
              type="number"
              value={newExercise.duration}
              onChange={(e) =>
                setNewExercise({ ...newExercise, duration: e.target.value })
              }
              className="w-full border border-gray-300 rounded p-2 mb-4"
            />

            <div className="flex justify-between mt-6">
              <button
                className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow"
                onClick={handleAddExercise}
              >
                Save
              </button>
              <button
                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow"
                onClick={() => setShowAddModal(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

// Icon Components
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

export default WorkoutDaysExercisePage;
