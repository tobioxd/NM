import React, { useState } from "react";
import { Link } from "react-router-dom";
import backgroundImage from "../../../assets/background/2.jpg";

const Signup = () => {
  const [nameError, setNameError] = useState("");
  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [passwordConfirmError, setPasswordConfirmError] = useState("");

  const handleSignUp = (e) => {
    setNameError("");
    setPhoneNumberError("");
    setEmailError("");
    setPasswordError("");
    setPasswordConfirmError("");

    e.preventDefault();
    const form = e.target;
    const name = form.name.value;
    const phoneNumber = form.phoneNumber.value;
    const email = form.email.value;
    const password = form.password.value;
    const passwordConfirm = form.passwordConfirm.value;

    if (name === "") {
      setNameError("Please enter your Name");
      return;
    }

    if (email === "") {
      setEmailError("Please enter your Email");
      return;
    }

    if (email === "") {
      setEmailError("Please enter your Email");
      return;
    }

    if (password === "") {
      setPasswordError("Please enter your Password");
      return;
    }

    if (passwordConfirm === "") {
      setPasswordConfirmError("Please confirm your Password");
      return;
    }

    if (password.length < 8) {
      setPasswordError("Password must be at least 8 characters long");
      return;
    }

    if (password !== passwordConfirm) {
      setPasswordConfirmError("Password and Confirm Password do not match");
      return;
    }

    const userdata = {
      phone_number: phoneNumber,
      email: email,
      password: password,
      retype_password: passwordConfirm,
      name: name,
    };

    //send data to the server
    fetch(import.meta.env.VITE_BACKEND_URL + "/api/v1/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userdata),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.user === null) {
          if (data.message === "Email exists already !") {
            setEmailError("Email exists already !");
          }
          if (data.message === "Phone number exists already !") {
            setPhoneNumberError("Phone number exists already !");
          }
          else setEmailError(data.message);
        } else {
          console.log("Success:", data);
          alert("User created successfully, please login !");
          window.location.href = "/login";
        }
      });
  };

  return (
    <div
      className="min-h-screen bg-gray-100 py-6 flex flex-col justify-center sm:py-12"
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: "cover",
      }}
    >
      <div className="relative py-3 sm:max-w-xl sm:mx-auto">
        <div className="absolute inset-0 bg-gradient-to-r from-blue-300 to-blue-600 shadow-lg transform -skew-y-6 sm:skew-y-0 sm:-rotate-6 sm:rounded-3xl"></div>
        <div className="relative px-4 py-10 bg-white shadow-lg sm:rounded-3xl sm:p-20">
          <div className="max-w-md mx-auto">
            <div>
              <h1 className="text-2xl font-semibold">Sign Up Form</h1>
            </div>
            <div className="divide-y divide-gray-200">
              <form
                onSubmit={handleSignUp}
                className="py-8 text-base leading-6 space-y-4 text-gray-700 sm:text-lg sm:leading-7"
              >
                <div className="relative">
                  <input
                    id="name"
                    name="name"
                    type="text"
                    className="peer h-10 w-full border-b-2 border-gray-300 text-gray-900 focus:outline-none focus:borer-rose-600"
                    placeholder="Name"
                  />
                  <label htmlFor="name" className="text-red-500">
                    {nameError}
                  </label>
                </div>
                <div className="relative">
                  <input
                    id="phoneNumber"
                    name="phoneNumber"
                    type="text"
                    className="peer h-10 w-full border-b-2 border-gray-300 text-gray-900 focus:outline-none focus:borer-rose-600"
                    placeholder="Phone Number"
                  />
                  <label htmlFor="phoneNumber" className="text-red-500">
                    {phoneNumberError}
                  </label>
                </div>
                <div className="relative">
                  <input
                    id="email"
                    name="email"
                    type="text"
                    className="peer h-10 w-full border-b-2 border-gray-300 text-gray-900 focus:outline-none focus:borer-rose-600"
                    placeholder="Email address"
                  />
                  <label htmlFor="email" className="text-red-500">
                    {emailError}
                  </label>
                </div>
                <div className="relative">
                  <input
                    id="password"
                    name="password"
                    type="password"
                    className="peer h-10 w-full border-b-2 border-gray-300 text-gray-900 focus:outline-none focus:borer-rose-600"
                    placeholder="Password"
                  />
                  <label htmlFor="password" className="text-red-500">
                    {passwordError}
                  </label>
                </div>
                <div className="relative">
                  <input
                    id="passwordConfirm"
                    name="passwordConfirm"
                    type="password"
                    className="peer h-10 w-full border-b-2 border-gray-300 text-gray-900 focus:outline-none focus:borer-rose-600"
                    placeholder="PasswordConfirm"
                  />
                  <label htmlFor="passwordConfirm" className="text-red-500">
                    {passwordConfirmError}
                  </label>
                </div>

                <p>
                  If you have an account. PLease{" "}
                  <Link to="/login" className="text-blue-600 underline">
                    Login
                  </Link>
                </p>

                <div className="relative">
                  <button className="bg-blue-500 text-white rounded-md px-2 py-1">
                    Submit
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Signup;
