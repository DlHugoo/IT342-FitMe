import { useState } from "react";
import Sidebar from "../components/Sidebar";
import UserWhiteIcon from "../../assets/user-white.png";
import Headerbar from "../components/Headerbar";

const UserManagementPage = () => {
  const [users, setUsers] = useState([
    { id: 1, username: "BigDaddyDmun", email: "raymundlaude@cit.edu" },
    { id: 2, username: "Khokak", email: "khokak@gmail.com" },
    { id: 3, username: "Proffeseur", email: "johnmarlo@gmail.com" },
    { id: 4, username: "Katheyismey", email: "kathmiguelle@gmail.com" },
    { id: 5, username: "InvicibleMarc", email: "marcdotarot@gmail.com" },
    { id: 5, username: "InvicibleMarc", email: "marcdotarot@gmail.com" },
  ]);

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar Component */}
      <Sidebar />

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <Headerbar />

        {/* Tabs */}
        <div className="bg-blue-header2 text-white px-4 pb-0">
          <div className="inline-block px-4 py-2">Users</div>
        </div>

        {/* Content */}
        <div className="flex-1 p-6">
          {/* Search Bar */}
          <div className="mb-6 flex justify-center">
            <div className="relative w-96">
              <input
                type="text"
                placeholder="Search"
                className="w-full border border-gray-300 rounded p-2 pl-10"
              />
              <div className="absolute left-3 top-3 text-gray-400">
                <SearchIcon />
              </div>
            </div>
          </div>

          {/* User Table */}
          <div className="bg-white rounded-md shadow">
            <table className="w-full">
              <thead>
                <tr className="bg-gray-200 text-gray-500">
                  <th className="py-3 px-4 text-left">ID</th>
                  <th className="py-3 px-4 text-left">USERNAME</th>
                  <th className="py-3 px-4 text-left">EMAIL ADDRESS</th>
                  <th className="py-3 px-4 text-left">ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id} className="border-t border-gray-200">
                    <td className="py-3 px-4">{user.id}</td>
                    <td className="py-3 px-4">{user.username}</td>
                    <td className="py-3 px-4">{user.email}</td>
                    <td className="py-3 px-4 flex space-x-2">
                      <button className="text-fitme-blue">
                        <EditIcon />
                      </button>
                      <button className="text-red-500">
                        <DeleteIcon />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

// Icons for the main component
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
