import React, { useState } from "react";
import backgroundImage from "../../../assets/background/1.jpg";
import LoginGoogle from "./LoginGoogle";
import LoginFacebook from "./LoginFacebook";
import LoginGithub from "./LoginGithub";

const LogIn = () => {
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const handleLogin = (e) => {
    setEmailError("");
    setPasswordError("");

    e.preventDefault();
    const form = e.target;
    const input = form.email.value;
    const password = form.password.value;

    if (input === "") {
      setEmailError("Please enter your Email or Phone number");
      return;
    }

    if (password === "") {
      setPasswordError("Please enter your Password");
      return;
    }

    const user = {
      input,
      password,
    };

    //send data to the server
    fetch(import.meta.env.VITE_BACKEND_URL + "/api/v1/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    })
      .then((response) => {
        if (!response.ok && response.status !== 401) {
          if (response.status === 404) {
            setPasswordError("Your email or password is incorrect");
          }
          if (response.status === 400) {
            setPasswordError("Please enter a valid email or phone number");
          }
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        if (data.message === "User is blocked !") {
          alert(data.message);
          window.location.href = "/login";
        } else {
          localStorage.setItem("user", JSON.stringify(data.user));
          localStorage.setItem("token", data.token);
          window.location.href = "/";
        }
      })
      .catch((error) => {
        console.error(
          "There has been a problem with your fetch operation: ",
          error
        );
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
            <h1 className="my-3 text-3xl font-semibold text-black-700 dark:text-black-200">
              Sign in
            </h1>
            <p className="text-black-500 dark:text-black-400">
              Sign in to access your account
            </p>
          </div>
          <div className="m-7">
            <form onSubmit={handleLogin}>
              <div className="mb-6">
                <label
                  htmlFor="email"
                  className="block mb-2 text-sm text-white-600 dark:text-white-400"
                >
                  Email Address/ Phone Number
                </label>
                <input
                  type="text"
                  name="email"
                  id="email"
                  placeholder="Your email/ Phone Number"
                  className="w-full px-3 py-2 placeholder-white-300 border border-white-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-white-700 dark:text-white dark:placeholder-white-500 dark:border-white-600 dark:focus:ring-white-900 dark:focus:border-white-500"
                />
                <label htmlFor="email" className="text-red-500">
                  {emailError}
                </label>
              </div>
              <div className="mb-6">
                <div className="flex justify-between mb-2">
                  <label
                    htmlFor="password"
                    className="text-sm text-white-600 dark:text-white-400"
                  >
                    Password
                  </label>
                  <a
                    href="/forgot-password"
                    className="text-sm text-white-400 focus:outline-none focus:text-indigo-500 hover:text-indigo-500 dark:hover:text-indigo-300"
                  >
                    Forgot password?
                  </a>
                </div>
                <input
                  type="password"
                  name="password"
                  id="password"
                  placeholder="Your Password"
                  className="w-full px-3 py-2 placeholder-white-300 border border-white-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-100 focus:border-indigo-300 dark:bg-white-700 dark:text-white dark:placeholder-white-500 dark:border-white-600 dark:focus:ring-white-900 dark:focus:border-white-500"
                />
                <label htmlFor="password" className="text-red-500">
                  {passwordError}
                </label>
              </div>
              <div className="mb-6">
                <button
                  type="submit"
                  className="w-full px-3 py-4 text-white bg-indigo-500 rounded-md focus:bg-indigo-600 focus:outline-none"
                >
                  Sign in
                </button>
              </div>
              <p className="text-sm text-center text-white-400 ">
                  Or
              </p>
            </form>
          </div>
          <div className="m-7">
            <LoginGoogle />
            <LoginFacebook />
            <LoginGithub />
            <p className="text-sm text-center text-white-400">
              Don&#x27;t have an account yet?{" "}
              <a
                href="/sign-up"
                className="text-red-600 focus:outline-none focus:underline focus:text-indigo-500 dark:focus:border-indigo-800"
              >
                Sign up
              </a>
              .
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
export default LogIn;
