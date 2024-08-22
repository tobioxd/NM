import React, { useEffect, useState } from "react";
import { Table } from "flowbite-react";

const ManageUser = () => {
  const [allUsers, setAllUsers] = useState([]);
  const [totalpages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const limit = 10;

  useEffect(() => {
    const fetchAllUsers = async () => {
      try {
        const res = await fetch(
          `${
            import.meta.env.VITE_BACKEND_URL
          }/api/v1/users?page=${currentPage}&limit=${limit}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        const data = await res.json();
        setAllUsers(data.users);
        setTotalPages(data.totalPages);
      } catch (error) {
        console.log(error);
      }
    };

    fetchAllUsers();
  }, [currentPage, limit]);

  const handleBlockUser = (user) => async () => {
    fetch(
      `${import.meta.env.VITE_BACKEND_URL}/api/v1/users/blockOrEnable/${
        user.id
      }/false`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    )
      .then((res) => {
        if (res.status === 202) {
          window.location.reload();
        }
      })
      .then((data) => {
        console.log(data);
      })
      .catch((err) => console.log(err));
  };

  const handleEnableUser = (user) => async () => {
    fetch(
      `${import.meta.env.VITE_BACKEND_URL}/api/v1/users/blockOrEnable/${
        user.id
      }/true`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    )
      .then((res) => {
        if (res.status === 202) {
          window.location.reload();
        }
      })
      .then((data) => {
        console.log(data);
      })
      .catch((err) => console.log(err));
  };

  return (
    <>
      <div className="px-4 mt-40">
        <h2 className="mb-8 text-3xl font-bold flex justify-center">
          Manage Your User
        </h2>
        {/* Table */}
        <Table className="w-[2000px]">
          <Table.Head>
            <Table.HeadCell className="text-left whitespace-nowrap">
              No
            </Table.HeadCell>
            <Table.HeadCell className="text-left whitespace-nowrap">
              id
            </Table.HeadCell>
            <Table.HeadCell className="text-left whitespace-nowrap">
              Name
            </Table.HeadCell>
            <Table.HeadCell className="text-left whitespace-nowrap">
              Email
            </Table.HeadCell>
            <Table.HeadCell className="text-left whitespace-nowrap">
              Password Changed At
            </Table.HeadCell>
            <Table.HeadCell className="text-left whitespace-nowrap">
              <span>Active</span>
            </Table.HeadCell>
          </Table.Head>
          <Table.Body className="divide-y">
            {allUsers.map((user, index) => (
              <Table.Row key={user.id}>
                <Table.Cell>{index + 1}</Table.Cell>
                <Table.Cell>{user.id}</Table.Cell>
                <Table.Cell>{user.name}</Table.Cell>
                <Table.Cell>{user.email}</Table.Cell>
                <Table.Cell>{user.password_change_at}</Table.Cell>
                <Table.Cell>
                  {user.is_active === true ? (
                    <button
                      className="px-4 py-2 mr-4 text-sm text-white bg-blue-500 rounded hover:bg-red-500 w-24"
                      onMouseEnter={(e) => (e.target.innerText = "Block")}
                      onMouseLeave={(e) => (e.target.innerText = "Enable")}
                      onClick={handleBlockUser(user)}
                    >
                      Enable
                    </button>
                  ) : (
                    <button
                      className="px-4 py-2 mr-4 text-sm text-white bg-red-500 rounded hover:bg-blue-500 w-24"
                      onMouseEnter={(e) => (e.target.innerText = "Enable")}
                      onMouseLeave={(e) => (e.target.innerText = "Block")}
                      onClick={handleEnableUser(user)}
                    >
                      Block
                    </button>
                  )}
                </Table.Cell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table>
        <div className="flex justify-center space-x-4 mb-5 mt-10">
          <button
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage + 1 === 1}
            className={`px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-700 focus:outline-none ${
              currentPage + 1 === 1 ? "opacity-50 cursor-not-allowed" : ""
            }`}
          >
            &#8249;
          </button>

          {[...Array(totalpages).keys()].map((_, index) => {
            const pageNumber = index + 1;
            return (
              <button
                key={pageNumber}
                onClick={() => setCurrentPage(pageNumber - 1)}
                className={`px-4 py-2 text-white ${
                  pageNumber === currentPage ? "bg-blue-700" : "bg-blue-500"
                } rounded hover:bg-blue-700 focus:outline-none`}
              >
                {pageNumber}
              </button>
            );
          })}

          <button
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage + 1 === totalpages}
            className={`px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-700 focus:outline-none ${
              currentPage + 1 === totalpages
                ? "opacity-50 cursor-not-allowed"
                : ""
            }`}
          >
            &#8250;
          </button>
        </div>
      </div>
    </>
  );
};

export default ManageUser;
