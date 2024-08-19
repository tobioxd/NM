import React, { useState } from "react";
import { Label, TextInput } from "flowbite-react";

const UpdatePassword = () => {
  const curpassword = localStorage.getItem("password");
  const [passwordError, setPasswordError] = useState("");
  const [newpasswordError, setNewpasswordError] = useState("");
  const [newpasswordconfirmedError, setNewpasswordconfirmedError] =
    useState("");

  const handleUpdatePassword = (e) => {
    e.preventDefault();

    if (e.target.password.value !== curpassword) {
      setPasswordError("Your password is incorrect");
      return;
    } else {
      setPasswordError("");
    }

    if (e.target.newpassword.value.length < 8) {
      setNewpasswordError("Password must be at least 8 characters long");
      return;
    } else {
      setNewpasswordError("");
    }

    if (e.target.newpassword.value === curpassword) {
      setNewpasswordError(
        "New password must be different from the current password"
      );
      return;
    } else {
      setNewpasswordError("");
    }

    if (e.target.newpassword.value !== e.target.passwordconfirmed.value) {
      setNewpasswordconfirmedError(
        "Password and Confirm Password do not match"
      );
      return;
    } else {
      setNewpasswordconfirmedError("");
    }

    if (
      window.confirm("Are you sure you want to update your password?") === true
    ) {
      const userData = {
        password: e.target.password.value,
        newPassword: e.target.newpassword.value,
      };

      //Send data to the server
      fetch(import.meta.env.VITE_BACKEND_URL + "/api/v1/users/updatePassword", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(userData),
      })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
              return response.json();
        })
        .then((data) => {
          console.log(data);
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          localStorage.removeItem("password");
          alert("Password updated successfully, please login again");
          window.location.href = "/login";
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  };

  return (
    <div className="w-full myy-12">
      <h2 className="mb-8 text-3xl font-bold mt-10">Change Password </h2>
      <form
        className="flex flex-col flex-wrap gap-4"
        onSubmit={handleUpdatePassword}
      >
        <div className="flex gap-8">
          {/*your password*/}
          <div className="lg:w-1/2">
            <div className="mb-2 block">
              <Label htmlFor="password" value="Your Password" />
            </div>
            <TextInput
              id="password"
              placeholder="Your Password"
              required
              type="password"
            />
            <label htmlFor="password" className="text-red-500">
              {passwordError}
            </label>
          </div>
        </div>

        <div className="flex gap-8">
          {/*yournewpassword*/}
          <div className="lg:w-1/2">
            <div className="mb-2 block">
              <Label htmlFor="newpassword " value="Your New Password" />
            </div>
            <TextInput
              id="newpassword"
              placeholder="Enter your new password"
              required
              type="password"
            />
            <label htmlFor="newpassword" className="text-red-500">
              {newpasswordError}
            </label>
          </div>
        </div>

        <div className="flex gap-8">
          {/*passwordconfirm*/}
          <div className="lg:w-1/2">
            <div className="mb-2 block">
              <Label htmlFor="passwordconfirmed" value="Confirm New Password" />
            </div>
            <TextInput
              id="passwordconfirmed"
              placeholder="Confirm your new password"
              required
              type="password"
            />
            <label htmlFor="passwordconfirmed" className="text-red-500">
              {newpasswordconfirmedError}
            </label>
          </div>
        </div>
        <button
          type="submit"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-1/6"
        >
          Update
        </button>
      </form>
    </div>
  );
};

export default UpdatePassword;
