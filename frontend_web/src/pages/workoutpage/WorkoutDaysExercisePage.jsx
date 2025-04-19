import { useParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const WorkoutDaysExercisePage = () => {
  const { id, dayId } = useParams(); // id = workoutId
  const [exercises, setExercises] = useState([]);
  const [dayNumber, setDayNumber] = useState(null);

  useEffect(() => {
    if (!dayId) return;
    fetchDayExercises();
  }, [dayId]);

  const fetchDayExercises = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const res = await axios.get(`/api/day-exercises/${dayId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.data.length > 0) {
        const sorted = res.data.sort((a, b) => a.id - b.id);
        setExercises(sorted);
        setDayNumber(res.data[0].workoutDay.dayNumber);
      } else {
        setExercises([]);
        setDayNumber("Unknown");
      }
    } catch (err) {
      console.error("Failed to fetch exercises", err);
    }
  };

  return (
    <div className="flex bg-gray-100 min-h-screen">
      <Sidebar />
      <div className="ml-60 flex flex-col flex-grow">
        <Headerbar />

        <div className="bg-blue-header2 text-white px-4 pb-0">
          <div className="inline-block px-4 py-2">
            <Link to={`/workout/${id}`} className="hover:underline">
              Back to Days
            </Link>{" "}
            <span className="mx-2">›</span> Day {dayNumber || dayId}
          </div>
        </div>

        <div className="flex-1 p-6">
          <div className="text-lg font-semibold text-blue-header mb-4">
            Exercises for Day {dayNumber || dayId}
          </div>

          <div className="bg-white rounded-md shadow overflow-x-auto">
            <table className="min-w-full table-fixed">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-4 px-6 text-left font-medium w-1/3">
                    Exercise Name
                  </th>
                  <th className="py-4 px-6 text-left font-medium w-1/3">
                    Reps
                  </th>
                  <th className="py-4 px-6 text-left font-medium w-1/3">
                    Duration (sec)
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
                      <td className="py-4 px-6 text-blue-600">
                        {ex.exercise?.name}
                      </td>
                      <td className="py-4 px-6">{ex.reps ?? "—"}</td>
                      <td className="py-4 px-6">{ex.duration ?? "—"}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="3" className="text-center py-6 text-gray-500">
                      No exercises found for this day.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WorkoutDaysExercisePage;
