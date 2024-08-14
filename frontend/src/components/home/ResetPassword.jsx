import React, { useState } from "react";
import backgroundImage from "../../assets/background/4.jpg";
import { useParams } from "react-router-dom";

const ResetPassword = () => {
  const { token } = useParams();
  const [passwordError, setPasswordError] = useState("");
  const [passwordconfirmError, setPasswordconfirmError] = useState("");

  const handleReset = (e) => {
    e.preventDefault();
    const form = e.target;
    const password = form.password.value;
    const passwordconfirm = form.passwordconfirm.value;
    if(password.length < 8) {
      setPasswordError("Password must be at least 8 characters");
      return;
    }else{
      setPasswordError("");
    }
    if (password !== passwordconfirm) {
      setPasswordconfirmError("Passwords do not match");
      return;
    }else{
      setPasswordconfirmError("");
    }

    const passwordData = {
      "newPassword": password
    };

    fetch(import.meta.env.VITE_BACKEND_URL + `/api/v1/users/reset-password/${token}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(passwordData),
    })
      .then((response) =>  response.json())
      .then((data) => {
        if (data.status === "success") {
          alert("Password reset successfully");
          window.location.href = "/login";
        } else {
          alert(data.message);
          
        }
      });

    
  };

  return (
    <div
      className="flex items-center min-h-screen bg-white dark:bg-black-900"
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: "cover",
      }}
    >
      <div className="container mx-auto">
        <div className="max-w-md mx-auto my-10 bg-white bg-opacity-50 p-5 rounded-lg border border-black-300">
          <div className="text-center">
            <p className="text-black-500 dark:text-black-400 font-bold">
              Enter your new password
            </p>
          </div>
          <div className="m-7">
            <form onSubmit={handleReset}>
              <div className="mb-6">
                <label
                  htmlFor="email"
                  className="block mb-2 text-sm text-white-600 dark:text-white-400"
                >
                  Password
                </label>
                <input
                  type="password"
                  name="password"
                  id="email"
                  placeholder="New password"
                  className="w-full px-3 py-2 placeholder-white-300 border border-white-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-white-700 dark:text-white dark:placeholder-white-500 dark:border-white-600 dark:focus:ring-white-900 dark:focus:border-white-500"
                />
                <label htmlFor="password" className="text-red-500">
                  {passwordError}
                </label>
              </div>
              <div className="mb-6">
                <div className="flex justify-between mb-2">
                  <label
                    htmlFor="password"
                    className="text-sm text-white-600 dark:text-white-400"
                  >
                    Password confirm{" "}
                  </label>
                </div>
                <input
                  type="password"
                  name="passwordconfirm"
                  id="passwordconfirm"
                  placeholder="New password confirm"
                  className="w-full px-3 py-2 placeholder-white-300 border border-white-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-white-700 dark:text-white dark:placeholder-white-500 dark:border-white-600 dark:focus:ring-white-900 dark:focus:border-white-500"
                />
                <label htmlFor="password" className="text-red-500">
                  {passwordconfirmError}
                </label>
              </div>
              <div className="mb-6">
                <button
                  type="submit"
                  className="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Reset
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};
export default ResetPassword;
