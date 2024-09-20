import React, { useEffect, useState } from "react";
import { Label, TextInput } from "flowbite-react";

const UpdateMe = () => {
  const curuser = JSON.parse(localStorage.getItem("user"));
  const [emailError, setEmailError] = useState("");

  useEffect(() => {
    // Fetch user data using the id
    const fetchUser = async () => {};

    fetchUser();
  }, []);

  if (!curuser) {
    window.location.href = "/login";
  }

  const { name, email } = curuser;

  const handleUpdateMe = (e) => {
    if (window.confirm("Are you sure you want to update your info?") === true) {
      e.preventDefault();
      const userData = {
        name: e.target.name.value,
        email: e.target.email.value,
      };

      //Send data to the server
      fetch(import.meta.env.VITE_BACKEND_URL + "/api/v1/users/updateMe", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(userData),
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.message === "Email exists already !") {
            setEmailError(data.message);
          } else {
            console.log(data);
            alert(
              "Your info has been updated successfully, please log in again !"
            );
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/login";
          }
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  };

  return (
    <div className="w-full my-12">
      <div className="w-3/5 my-12">
        <h2 className="mb-8 text-3xl font-bold">Update Your Info </h2>
        <form
          className="flex flex-col flex-wrap gap-4"
          onSubmit={handleUpdateMe}
        >
          <div className="flex gap-8">
            {/*animename*/}
            <div className="lg:w-1/2">
              <div className="mb-2 block">
                <Label htmlFor="name" value="Name" />
              </div>
              <TextInput
                id="name"
                placeholder="Your name"
                required
                type="text"
                defaultValue={name}
              />
            </div>
          </div>

          <div className="flex gap-8">
            {/*email*/}
            <div className="lg:w-1/2">
              <div className="mb-2 block">
                <Label htmlFor="email" value="Email" />
              </div>
              <TextInput
                id="email"
                placeholder="Your email"
                required
                type="email"
                defaultValue={email}
              />
              <label htmlFor="email" className="text-red-500">
                {emailError}
              </label>
            </div>
          </div>
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded rounded w-1/4"
          >
            Update
          </button>
        </form>
      </div>
    </div>
  );
};

export default UpdateMe;
