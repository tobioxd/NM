import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebook } from "@fortawesome/free-brands-svg-icons";

const LoginFacebook = () => {

  const facebooklogin = () =>{

    window.location.href = `${import.meta.env.VITE_BACKEND_URL}/oauth2/authorization/facebook`;

  };

  return (
    <div className="mt-4">
      <button
        className="w-full px-3 py-4 text-white bg-blue-500 rounded-md focus:bg-blue-600 focus:outline-none flex items-center justify-center"
        onClick={facebooklogin}
      >
        <FontAwesomeIcon icon={faFacebook} className="mr-2" />
        Log in with Facebook
      </button>
    </div>
  );
};

export default LoginFacebook;