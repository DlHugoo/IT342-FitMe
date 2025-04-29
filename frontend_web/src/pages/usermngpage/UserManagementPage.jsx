import { useState, useEffect } from "react";
import axios from "../../api/axiosInstance";
import Sidebar from "../components/Sidebar";
import Headerbar from "../components/Headerbar";

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [pendingUpdate, setPendingUpdate] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [userToDelete, setUserToDelete] = useState(null);
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    role: "user",
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get("/api/users", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setUsers(response.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const handleEdit = (user) => {
    setSelectedUser(user);
    setFormData({
      username: user.username,
      email: user.email,
      role: user.role,
      age: user.age,
      height: user.height,
      weight: user.weight,
    });
    setShowModal(true);
  };

  const handleDeleteClick = (user) => {
    setUserToDelete(user);
    setShowDeleteModal(true);
  };

  const confirmDelete = async () => {
    try {
      await axios.delete(`/api/users/${userToDelete.id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setShowDeleteModal(false);
      setUserToDelete(null);
      fetchUsers();
    } catch (error) {
      console.error("Error deleting user:", error);
    }
  };

  const handleUpdate = () => {
    if (selectedUser.role !== formData.role) {
      setShowConfirmModal(true);
      setPendingUpdate(true);
    } else {
      submitUpdate();
    }
  };

  const submitUpdate = async () => {
    try {
      await axios.put(`/api/users/${selectedUser.id}`, formData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setShowModal(false);
      setShowConfirmModal(false);
      setPendingUpdate(false);
      fetchUsers();
    } catch (error) {
      console.error("Error updating user:", error);
    }
  };

  return (
    <div className="flex bg-gray-100 min-h-screen">
      <Sidebar />
      <div className="ml-60 flex flex-col flex-grow">
        <Headerbar />

        <div className="bg-blue-header2 text-white px-4 pb-0">
          <div className="inline-block px-4 py-2">Users</div>
        </div>

        <div className="flex-1 p-6">
          <div className="mb-6 flex justify-center">
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

          <div className="bg-white rounded-md shadow">
            <table className="w-full table-fixed">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-4 px-24 text-left font-medium">ID</th>
                  <th className="py-4 px-6 text-left font-medium">USERNAME</th>
                  <th className="py-4 px-6 text-left font-medium">
                    EMAIL ADDRESS
                  </th>
                  <th className="py-4 px-6 text-left font-medium">ROLE</th>
                  <th className="py-4 px-6 text-left font-medium">ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {users
                  .filter(
                    (user) =>
                      user.username
                        .toLowerCase()
                        .includes(searchTerm.toLowerCase()) ||
                      user.email
                        .toLowerCase()
                        .includes(searchTerm.toLowerCase())
                  )
                  .map((user) => (
                    <tr
                      key={user.id}
                      className="border-t border-gray-200 hover:bg-gray-50"
                    >
                      <td className="py-4 px-24">{user.id}</td>
                      <td className="py-4 px-6">{user.username}</td>
                      <td className="py-4 px-6">{user.email}</td>
                      <td className="py-4 px-6">{user.role}</td>
                      <td className="py-4 px-6">
                        <div className="flex gap-3 items-center">
                          <button
                            className="text-blue-600 hover:text-blue-800"
                            onClick={() => handleEdit(user)}
                            title="Edit"
                          >
                            <EditIcon />
                          </button>
                          <button
                            className="text-red-500 hover:text-red-700"
                            onClick={() => handleDeleteClick(user)}
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
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded shadow-lg w-96 relative">
            <div className="bg-blue-header text-white px-4 py-3 rounded-t flex justify-between items-center">
              <h2 className="text-lg font-semibold">Update User</h2>
              <button
                className="text-white text-xl leading-none"
                onClick={() => setShowModal(false)}
              >
                ×
              </button>
            </div>

            <div className="p-6">
              <label className="block text-sm font-medium mb-1">Username</label>
              <input
                className="w-full border border-gray-300 rounded p-2 mb-4"
                value={formData.username}
                onChange={(e) =>
                  setFormData({ ...formData, username: e.target.value })
                }
              />

              <label className="block text-sm font-medium mb-1">
                Email Address
              </label>
              <input
                className="w-full border border-gray-300 rounded p-2 mb-4"
                value={formData.email}
                onChange={(e) =>
                  setFormData({ ...formData, email: e.target.value })
                }
              />

              <label className="block text-sm font-medium mb-1">Role</label>
              <select
                className="w-full border border-gray-300 rounded p-2 mb-4"
                value={formData.role}
                onChange={(e) =>
                  setFormData({ ...formData, role: e.target.value })
                }
              >
                <option value="user">user</option>
                <option value="admin">admin</option>
              </select>

              <div className="flex justify-between mt-6">
                <button
                  className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow"
                  onClick={handleUpdate}
                >
                  Update
                </button>
                <button
                  className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Role Change Confirmation Modal */}
      {showConfirmModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded shadow-lg w-80 p-6">
            <h2 className="text-lg font-semibold text-gray-800 mb-4">
              Confirm Role Change
            </h2>
            <p className="text-gray-700 mb-6">
              Are you sure you want to change the user's role from{" "}
              <span className="font-semibold">{selectedUser.role}</span> to{" "}
              <span className="font-semibold">{formData.role}</span>?
            </p>
            <div className="flex justify-end gap-3">
              <button
                className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded"
                onClick={() => {
                  setShowConfirmModal(false);
                  setPendingUpdate(false);
                }}
              >
                Cancel
              </button>
              <button
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
                onClick={submitUpdate}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-md p-6 shadow-lg w-80 relative">
            <button
              className="absolute top-2 right-3 text-gray-500"
              onClick={() => setShowDeleteModal(false)}
            >
              ✕
            </button>
            <h2 className="text-center text-lg font-semibold text-gray-800 mb-6">
              Are you sure you want <br /> to delete user?
            </h2>
            <div className="flex justify-between px-4">
              <button
                className="border border-gray-400 text-gray-700 px-4 py-2 rounded"
                onClick={() => setShowDeleteModal(false)}
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

export default UserManagementPage;
