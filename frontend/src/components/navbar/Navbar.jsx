import React from "react";
import NavbarGuest from "./NavbarGuest";
import NavbarUser from "./NavbarUser";
import NavbarAdmin from "./NavbarAdmin";

const Navbar = () => {
  const user = localStorage.getItem("user");

  if (user) {
    const userObj = JSON.parse(user);
    if (userObj.role === "admin") {
      return <NavbarAdmin />;
    }else {
      return <NavbarUser />;
    }
  }else {
    return <NavbarGuest />;
  }
};

export default Navbar;
