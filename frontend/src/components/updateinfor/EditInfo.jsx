import React, { useState } from "react";
import UpdateMe from "./UpdateMe";
import UpdatePassword from "./UpdatePassword";

const EditInfo = () => {
  const user = localStorage.getItem("user");
  const [Info, setInfo] = useState(true);
  const [ChangePass, setChangePass] = useState(false);
  const [file, setFile] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!file) {
      alert("Please select a file first!");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch(import.meta.env.VITE_BACKEND_URL + "/api/v1/users/uploading", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      },
      body: formData,
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        localStorage.setItem("user", JSON.stringify(data));
        window.location.reload();
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };

  const handleMyInfo = () => {
    setInfo(true);
    setChangePass(false);
  };

  const handleChangePass = () => {
    setInfo(false);
    setChangePass(true);
  };

  if (!user) {
    window.location.href = "/login";
  } else {
    return (
      <div className="px-1 lg:px-1 flex items-center">
        <div className="flex w-full flex-col md:flex-row items-center py-40">
          <div className=" md:w-1/4 mb-40 mr-80 ml-40">
            <span className="font-bold">My Profile Picture</span>
            <div>
              <img
                src={
                  JSON.parse(user).photo_url !== "default.jpg"
                    ? JSON.parse(user).photo_url
                    : "/images/default.jpg"
                }
                alt=""
              />
              <input type="file" onChange={(e) => setFile(e.target.files[0])} />
            </div>
            <div className="flex justify-center mt-4">
              <button
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                type="button"
                onClick={handleSubmit}
              >
                Upload
              </button>
            </div>
          </div>
          <div className="mb-80 mr-20">
            <h1 className="text-2xl font-bold text-gray-800 mb-4">Edit Info</h1>
            <div className="flex flex-col items-left justify-center">
              <button
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                onClick={handleMyInfo}
                type="button"
              >
                My Info
              </button>
              <button
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                onClick={handleChangePass}
                type="button"
              >
                Change Password
              </button>
            </div>
          </div>
          <div className="w-full">
            {Info && <UpdateMe />}
            {ChangePass && <UpdatePassword />}
          </div>
        </div>
      </div>
    );
  }
};

export default EditInfo;
