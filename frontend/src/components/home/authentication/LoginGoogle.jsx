import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGoogle } from "@fortawesome/free-brands-svg-icons";

const LoginGoogle = () => {

    const googleLogin = () =>{

      window.location.href = `${import.meta.env.VITE_BACKEND_URL}/oauth2/authorization/google`; 

    };

    return(
        <div className="mt-4">
                <button
                  className="w-full px-3 py-4 text-white bg-red-500 rounded-md focus:bg-red-600 focus:outline-none"
                  onClick={googleLogin}
                >
                <FontAwesomeIcon icon={faGoogle} className="mr-2" />
                  Log in with Google 
                </button>
              </div>
    )
}

export default LoginGoogle