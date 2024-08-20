import { useEffect } from "react";

const Success = () => {
  useEffect(() => {
    fetch(`${import.meta.env.VITE_BACKEND_URL}/user/info`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        accept: "*/*",
      },
      credentials: "include", // Ensure cookies are included in the request
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        return res.json();
      })
      .then((data) => {
        console.log(data);
        localStorage.setItem("user", JSON.stringify(data.user));
        localStorage.setItem("token", data.token);
        window.location.href = "/";
      })
      .catch((error) => {
        console.error("Error fetching user info:", error);
      });
  }, []);

  return <div>Success Component</div>;
};

export default Success;
