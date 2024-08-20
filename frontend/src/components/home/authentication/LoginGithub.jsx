import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGithub } from "@fortawesome/free-brands-svg-icons";

const LoginGithub = () => {

  const githublogin = () =>{

    alert("This feature is not available yet. Please try another method.");

  };

  return (
    <div className="mt-4">
      <button
        className="w-full px-3 py-4 text-white bg-gray-800 rounded-md focus:bg-gray-900 focus:outline-none flex items-center justify-center"
        onClick={githublogin}
      >
        <FontAwesomeIcon icon={faGithub} className="mr-2" />
        Log in with GitHub
      </button>
    </div>
  );
};

export default LoginGithub;