import React, { useEffect, useState } from "react";
import UpdateMe from "./UpdateMe";
import UpdatePassword from "./UpdatePassword";

const EditInfo = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  const [Info, setInfo] = useState(true);
  const [ChangePass, setChangePass] = useState(false);

  useEffect(() => {
    if (!user) {
      window.location.href = "/login";
    }
  })

  const handleMyInfo = () => {
    setInfo(true);
    setChangePass(false);
  };

  const handleChangePass = () => {
    setInfo(false);
    setChangePass(true);
  };

  return (
    <div className="px-1 lg:px-1 flex items-center">
      <div className="flex w-full flex-col md:flex-row items-center py-40">
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
};

export default EditInfo;