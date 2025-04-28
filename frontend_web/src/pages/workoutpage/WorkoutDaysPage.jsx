import { useParams, Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const WorkoutDaysPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [days, setDays] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editModal, setEditModal] = useState(false);
  const [workoutTitle, setWorkoutTitle] = useState("");
  const [daysToAdd, setDaysToAdd] = useState(1);
  const [editDay, setEditDay] = useState(null);

  useEffect(() => {
    if (!id) return;
    fetchWorkoutDays();
    fetchWorkoutTitle();
  }, [id]);

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

  const fetchWorkoutDays = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      console.warn("No token found. Cannot fetch workout days.");
      return;
    }

    try {
      const res = await axios.get(`/api/workout-days/workout/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (Array.isArray(res.data)) {
        const sorted = res.data.sort((a, b) => a.dayNumber - b.dayNumber);
        setDays(sorted);
      } else {
        console.error("Unexpected response format for workout days:", res.data);
        setDays([]); // Clear the days if the data is invalid
      }
    } catch (err) {
      console.error("Failed to fetch workout days", err);
      setDays([]); // Also clear days on error
    }
  };

  const handleAddMultipleDays = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    const sorted = [...days].sort((a, b) => a.dayNumber - b.dayNumber);
    let currentMax =
      sorted.length > 0 ? sorted[sorted.length - 1].dayNumber : 0;

    try {
      for (let i = 0; i < daysToAdd; i++) {
        currentMax++;
        await axios.post(
          "/api/workout-days",
          {
            dayNumber: currentMax,
            restDay: false,
            workout: { workoutId: id },
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
      }
      fetchWorkoutDays();
      setDaysToAdd(1);
      setShowModal(false);
    } catch (err) {
      console.error("Failed to add days", err);
    }
  };

  const handleDeleteDay = async (dayId) => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      await axios.delete(`/api/workout-days/${dayId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchWorkoutDays();
    } catch (err) {
      console.error("Failed to delete workout day", err);
    }
  };

  const handleSaveEditDay = async () => {
    const token = localStorage.getItem("token");
    if (!token || !editDay?.dayId) return;

    try {
      await axios.put(
        `/api/workout-days/${editDay.dayId}`,
        {
          dayNumber: editDay.dayNumber,
          restDay: editDay.restDay,
          workout: editDay.workout,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setEditModal(false);
      fetchWorkoutDays();
    } catch (err) {
      console.error("Failed to update workout day", err);
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
            <span className="mx-2">›</span> {workoutTitle || "Loading..."}
          </div>
        </div>

        <div className="flex-1 p-6">
          <div className="flex items-center justify-between mb-6">
            <div className="text-lg font-semibold text-blue-header">
              {workoutTitle} has {days.length} day(s)
            </div>
            <button
              className="bg-fitme-blue text-white px-4 py-2 rounded shadow hover:bg-fitme-blue-hover"
              onClick={() => setShowModal(true)}
            >
              Add Day(s)
            </button>
          </div>

          <div className="bg-white rounded-md shadow">
            <table className="w-full table-fixed">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-4 px-36 text-left font-medium w-1/3">
                    DAY #
                  </th>
                  <th className="py-4 px-24 text-left font-medium w-1/3">
                    REST DAY
                  </th>
                  <th className="py-4 px-24 text-left font-medium w-1/3">
                    ACTIONS
                  </th>
                </tr>
              </thead>
              <tbody>
                {days.map((day) => (
                  <tr
                    key={day.dayId}
                    className="border-t border-gray-200 hover:bg-gray-50"
                  >
                    <td
                      className="py-4 px-36 text-blue-600 hover:underline cursor-pointer"
                      onClick={() =>
                        navigate(`/workout/${id}/days/${day.dayId}`)
                      }
                    >
                      Day {day.dayNumber}
                    </td>
                    <td className="py-4 px-24">{day.restDay ? "Yes" : "No"}</td>
                    <td className="py-4 px-24">
                      <div className="flex items-center gap-4">
                        <button
                          className="text-blue-500 hover:text-blue-700"
                          title="Edit"
                          onClick={() => {
                            setEditDay(day);
                            setEditModal(true);
                          }}
                        >
                          <EditIcon />
                        </button>
                        <button
                          className="text-red-500 hover:text-red-700"
                          onClick={() => handleDeleteDay(day.dayId)}
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
          title="Add Workout Day(s)"
          onClose={() => setShowModal(false)}
          onSubmit={handleAddMultipleDays}
        >
          <label className="block text-sm mb-2 font-medium text-gray-700">
            How many days do you want to add?
          </label>
          <input
            type="number"
            min={1}
            value={daysToAdd}
            onChange={(e) => setDaysToAdd(Number(e.target.value))}
            className="w-full border border-gray-300 rounded p-2 mb-4"
            placeholder="Number of days"
          />
        </Modal>
      )}

      {editModal && editDay && (
        <Modal
          title={`Edit Day ${editDay.dayNumber}`}
          onClose={() => setEditModal(false)}
          onSubmit={handleSaveEditDay}
        >
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">
              Day Number
            </label>
            <input
              type="number"
              value={editDay.dayNumber}
              disabled
              className="w-full border border-gray-300 rounded p-2 bg-gray-100 cursor-not-allowed"
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700">
              Rest Day
            </label>
            <label className="flex items-center space-x-2">
              <input
                type="checkbox"
                checked={editDay.restDay}
                onChange={(e) =>
                  setEditDay({ ...editDay, restDay: e.target.checked })
                }
              />
              <span>{editDay.restDay ? "Yes" : "No"}</span>
            </label>
          </div>
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

export default WorkoutDaysPage;
