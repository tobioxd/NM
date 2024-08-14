import React from "react";
import NavbarGuest from "./NavbarGuest";
import NavbarUser from "./NavbarUser";

const Navbar = () => {
  const user = localStorage.getItem("user");

  if (user) {
    const userObj = JSON.parse(user);
    if (userObj.role === "admin" || userObj.role === "user") {
      return <NavbarUser />;
    } 
  }else {
    return <NavbarGuest />;
  }
};

export default Navbar;
