import React,{useState} from "react";
import backgroundImage from "../../assets/background/3.jpg";
import profilepic from "../../assets/profilepic/hero.jpg";

const ForgotPassword = () => {
  const [input, setEmail] = useState("");
  const [inputError, setInputError] = useState("");

  const handleResetPassword = () => {
    fetch(import.meta.env.VITE_BACKEND_URL + "/api/v1/users/forgot-password", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ input }),
    })
      .then((response) => {
        if (!response.ok) {
          if (response.status === 404) {
            setInputError("Your email or phonenumber is incorrect");
          }
          if(response.status === 400) {
            setInputError("Please enter a valid email or phone number");
          }
          throw new Error("Network response was not ok")  ;
        }
        return response.json();
      })
      .then((data) => {
        if(data.status === "success") {
          alert("Password reset link sent to your email");
        } else {
          alert(data.message);
        }
        setInputError("");
      });
  };
  return (
    <div
      className="w-screen h-screen flex items-center justify-center"
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <div className="w-full xl:w-3/4 lg:w-11/12 flex">
        <div
          className="w-full h-auto bg-gray-400 hidden lg:block lg:w-1/2 bg-cover rounded-l-lg"
          style={{
            backgroundImage: `url(${profilepic})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        ></div>
        <div className="w-full lg:w-1/2 bg-white p-5 rounded-lg lg:rounded-l-none">
          <div className="px-8 mb-4 text-center">
            <h3 className="pt-4 mb-2 text-2xl">Forgot Your Password?</h3>
            <p className="mb-4 text-sm text-gray-700">
              We get it, stuff happens. Just enter your email address or phone number below and
              we'll send you a link to reset your password!
            </p>
          </div>
          <form className="px-8 pt-6 pb-8 mb-4 bg-white rounded">
            <div className="mb-4">
              <label
                className="block mb-2 text-sm font-bold text-gray-700"
                htmlFor="input"
              >
                Email/Phone number
              </label>
              <input
                className="w-full px-3 py-2 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
                id="input"
                type="text"
                value = {input}
                placeholder="Enter Email Address/ Phone Number..."
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <label htmlFor="password" className="text-red-500">
                {inputError}
            </label>
            <div className="mb-6 text-center">
              <button
                className="w-full px-4 py-2 font-bold text-white bg-red-500 rounded-full hover:bg-red-700 focus:outline-none focus:shadow-outline"
                type="button"
                onClick={handleResetPassword}
              >
                Reset Password
              </button>
            </div>
            <hr className="mb-6 border-t" />
            <div className="text-center">
              <a
                className="inline-block text-sm text-blue-500 align-baseline hover:text-blue-800"
                href="/sign-up"
              >
                Create an Account!
              </a>
            </div>
            <div className="text-center">
              <a
                className="inline-block text-sm text-blue-500 align-baseline hover:text-blue-800"
                href="/login"
              >
                Already have an account? Login!
              </a>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
export default ForgotPassword;
