import { useState, useEffect } from "react";
import axios from "../../api/axiosInstance";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const ExerciseManagementPage = () => {
  const [exercises, setExercises] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedExercise, setSelectedExercise] = useState(null);
  const [exerciseToDelete, setExerciseToDelete] = useState(null);
  const BASE_API_URL = import.meta.env.VITE_API_URL;
  const [uploadingGif, setUploadingGif] = useState(false);

  const [formData, setFormData] = useState({
    name: "",
    gifUrl: "",
  });

  const [newExerciseData, setNewExerciseData] = useState({
    name: "",
    gifUrl: "",
  });

  useEffect(() => {
    fetchExercises();
  }, []);

  const fetchExercises = async () => {
    try {
      const response = await axios.get("/api/exercises", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setExercises(response.data);
    } catch (error) {
      console.error("Error fetching exercises:", error);
    }
  };

  const handleEdit = (exercise) => {
    setSelectedExercise(exercise);
    setFormData({
      name: exercise.name,
      gifUrl: exercise.gifUrl,
    });
    setShowModal(true);
  };

  const handleDeleteClick = (exercise) => {
    setExerciseToDelete(exercise);
  };

  const confirmDelete = async () => {
    try {
      await axios.delete(`/api/exercises/${exerciseToDelete.exerciseId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setExerciseToDelete(null);
      fetchExercises();
    } catch (error) {
      console.error("Error deleting exercise:", error);
    }
  };

  const handleUpdate = async () => {
    try {
      await axios.put(
        `/api/exercises/${selectedExercise.exerciseId}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      setShowModal(false);
      fetchExercises();
    } catch (error) {
      console.error("Error updating exercise:", error);
    }
  };

  const handleAddExercise = async () => {
    try {
      await axios.post("/api/exercises", newExerciseData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setShowAddModal(false);
      setNewExerciseData({ name: "", gifUrl: "" });
      fetchExercises();
    } catch (error) {
      console.error("Error adding exercise:", error);
    }
  };

  const handleGifUpload = async (e, isEdit = false) => {
    const file = e.target.files[0];
    if (!file) return;

    const formDataUpload = new FormData();
    formDataUpload.append("file", file);

    try {
      setUploadingGif(true); // 🔵 Start loader
      const response = await axios.post(
        "/api/exercises/upload-gif",
        formDataUpload,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      const uploadedUrl = response.data;

      if (isEdit) {
        setFormData((prev) => ({ ...prev, gifUrl: uploadedUrl }));
      } else {
        setNewExerciseData((prev) => ({ ...prev, gifUrl: uploadedUrl }));
      }
    } catch (error) {
      console.error("GIF upload failed", error);
    } finally {
      setUploadingGif(false); // 🛑 Stop loader after success or error
    }
  };

  return (
    <div className="flex bg-gray-100 min-h-screen">
      <Sidebar />
      <div className="ml-60 flex flex-col flex-grow">
        <Headerbar />

        <div className="bg-blue-header2 text-white px-4 pb-0">
          <div className="inline-block px-4 py-2">Exercises</div>
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
                onClick={() => setShowAddModal(true)}
              >
                Add Exercise
              </button>
            </div>
          </div>

          <div className="bg-white rounded-md shadow">
            <table className="w-full table-fixed">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-4 px-20 text-left font-medium">ID</th>
                  <th className="py-4 px-6 text-left font-medium">NAME</th>
                  <th className="py-4 px-6 text-left font-medium">GIF</th>
                  <th className="py-4 px-6 text-left font-medium">ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {exercises
                  .filter((ex) =>
                    ex.name.toLowerCase().includes(searchTerm.toLowerCase())
                  )
                  .map((exercise) => (
                    <tr
                      key={exercise.exerciseId}
                      className="border-t border-gray-200 hover:bg-gray-50"
                    >
                      <td className="py-4 px-20">{exercise.exerciseId}</td>
                      <td className="py-4 px-6">{exercise.name}</td>
                      <td className="py-4 px-6">
                        {exercise.gifUrl ? (
                          <div className="w-20 h-20 border rounded overflow-hidden bg-white flex items-center justify-center">
                            <img
                              src={
                                exercise.gifUrl.startsWith("http")
                                  ? exercise.gifUrl
                                  : `${BASE_API_URL}${exercise.gifUrl}`
                              }
                              alt="Exercise GIF"
                              className="object-contain w-full h-full"
                            />
                          </div>
                        ) : (
                          "—"
                        )}
                      </td>

                      <td className="py-4 px-6">
                        <div className="flex gap-3 items-center">
                          <button
                            className="text-blue-600 hover:text-blue-800"
                            onClick={() => handleEdit(exercise)}
                            title="Edit"
                          >
                            <EditIcon />
                          </button>
                          <button
                            className="text-red-500 hover:text-red-700"
                            onClick={() => handleDeleteClick(exercise)}
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

      {/* Edit Modal */}
      {showModal && (
        <Modal
          title="Update Exercise"
          onClose={() => setShowModal(false)}
          onCancel={() => {
            setFormData({ name: "", gifUrl: "" });
            setSelectedExercise(null);
            setShowModal(false);
          }}
          onSubmit={handleUpdate}
        >
          <input
            className="w-full border border-gray-300 rounded p-2 mb-4"
            placeholder="Exercise Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
          <input
            type="file"
            accept=".gif"
            onChange={(e) => handleGifUpload(e, true)}
            className="w-full border border-gray-300 rounded p-2 mb-4"
          />
          {uploadingGif ? (
            <div className="text-center text-blue-500">Uploading GIF...</div>
          ) : (
            formData.gifUrl && (
              <img
                src={
                  formData.gifUrl.startsWith("http")
                    ? formData.gifUrl
                    : `${BASE_API_URL}${formData.gifUrl}`
                }
                alt="Preview"
                className="w-24 h-24 object-contain mb-4"
              />
            )
          )}
        </Modal>
      )}

      {/* Add Exercise Modal */}
      {showAddModal && (
        <Modal
          title="Add New Exercise"
          onClose={() => setShowAddModal(false)}
          onCancel={() => {
            setNewExerciseData({ name: "", gifUrl: "" });
            setShowAddModal(false);
          }}
          onSubmit={handleAddExercise}
        >
          <input
            className="w-full border border-gray-300 rounded p-2 mb-4"
            placeholder="Exercise Name"
            value={newExerciseData.name}
            onChange={(e) =>
              setNewExerciseData({
                ...newExerciseData,
                name: e.target.value,
              })
            }
          />
          <input
            type="file"
            accept=".gif"
            onChange={(e) => handleGifUpload(e, false)}
            className="w-full border border-gray-300 rounded p-2 mb-4"
          />
          {uploadingGif ? (
            <div className="text-center text-blue-500">Uploading GIF...</div>
          ) : (
            newExerciseData.gifUrl && (
              <img
                src={
                  newExerciseData.gifUrl.startsWith("http")
                    ? newExerciseData.gifUrl
                    : `${BASE_API_URL}${newExerciseData.gifUrl}`
                }
                alt="Preview"
                className="w-24 h-24 object-contain mb-4"
              />
            )
          )}
        </Modal>
      )}

      {/* Delete Confirmation Modal */}
      {exerciseToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-md p-6 shadow-lg w-80 relative">
            <button
              className="absolute top-2 right-3 text-gray-500"
              onClick={() => setExerciseToDelete(null)}
            >
              ✕
            </button>
            <h2 className="text-center text-lg font-semibold text-gray-800 mb-6">
              Are you sure you want <br /> to delete this exercise?
            </h2>
            <div className="flex justify-between px-4">
              <button
                className="border border-gray-400 text-gray-700 px-4 py-2 rounded"
                onClick={() => setExerciseToDelete(null)}
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

const Modal = ({ title, onClose, onSubmit, onCancel, children }) => (
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
            onClick={onCancel}
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

export default ExerciseManagementPage;
