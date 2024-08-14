import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import icon from "../../assets/icon/icon.jpg";

//react icons
import { FaBarsStaggered, FaXmark } from "react-icons/fa6";

const Navbar = () => {
  const user = localStorage.getItem("user");
  const userId = JSON.parse(user)._id;
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isMenuUserOpen, setIsMenuUserOpen] = useState(false);
  const [isSticky, setIsSticky] = useState(false);

  //toggle menu
  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const toggleMenuUser = () => {
    setIsMenuUserOpen(!isMenuUserOpen);
  };

  useEffect(() => {
    setIsSticky(true);
  }, []);

  //navItems here
  const navItems = [
    {
      label: "Home",
      path: "/",
    },
    {
      label: "Anime List",
      path: "/anime-list",
    },
    {
      label: "Forum",
      path: "/forum",
    },
  ];

  //navUserItems here
  const navUserItems = [
    {
      label: "Profile",
      path: `/profile/${userId}`,
      loader: () =>
        fetch(import.meta.env.VITE_BACKEND_URL + `/api/v1/users/${userId}`),
    },
    {
      label: "Edit Info",
      path: `/edit-info/${userId}`,
      loader: () =>
        fetch(import.meta.env.VITE_BACKEND_URL + `/api/v1/users/${userId}`),
    },
    {
      label: "Inbox",
      path: "/inbox",
    },
    {
      label: "Logout",
      path: "/logout",
    },
  ];

  return (
    <div className="fixed w-full z-100">
      <header className="w-full bg-transparent fixed top-0 right-0 transition-all ease-in duration-300">
        <nav
          className={`py-5 lg:px-24 px-4 ${
            isSticky ? "sticky top-0 left-0 right-0 bg-red-400" : ""
          }`}
        >
          <div className="flex justify-between items-center text-base gap-8">
            {/* logo */}
            <Link
              to="/"
              className="text-2xl font-bold text-red-600 flex items-center gap-2"
            >
              <img src={icon} alt="icon" className="w-10 h-10" />
              Anime Widget
            </Link>

            {/* nav links for large device */}

            <ul className="md:flex ml-auto space-x-12 hidden">
              {navItems.map((item, index) => (
                <li key={index}>
                  <Link
                    to={item.path}
                    className="block text-base text-black uppercase cursor-pointer hover:text-red-700"
                  >
                    {item.label}
                  </Link>
                </li>
              ))}
            </ul>

            <div className="flex items-center ml-auto">
              {/*userprofile*/}
              <img
                src={`/images/default.jpg`}
                alt={user.name}
                className="w-10 h-10 rounded-full cursor-pointer "
                onClick={toggleMenuUser}
              />

              {/* navItemUser*/}
              <div
                className={`space-y-4 px-4 mt-16 py-5 bg-sky-400 ${
                  isMenuUserOpen ? "block fixed top-0 " : "hidden"
                } `}
              >
                {navUserItems.map((item, index) => (
                  <ul key={index}>
                    <Link
                      to={item.path}
                      className="block text-base text-white uppercase cursor-pointer hover:text-black-700"
                    >
                      {item.label}
                    </Link>
                  </ul>
                ))}
              </div>
            </div>

            {/* btn for lg devices */}
            {/* <div className="space-x-12 hidden lg:flex items-center">
            <button>
              <FaBarsStaggered className="w-5 hover:text-red-700" />
            </button>
          </div> */}

            {/* menu btn for the mobile devices */}
            <div className="md:hidden">
              <button
                onClick={toggleMenu}
                className="text-black focus:outline-none"
              >
                {isMenuOpen ? (
                  <FaXmark className="w-5 hover:text-red-700" />
                ) : (
                  <FaBarsStaggered className="w-5 hover:text-red-700" />
                )}
              </button>
            </div>

            {/* navItems for mobile devices */}
            <div
              className={`space-y-4 px-4 mt-16 py-5 bg-red-700 ${
                isMenuOpen ? "block fixed top-0 right-0 left-0" : "hidden"
              } `}
            >
              {navItems.map((item, index) => (
                <ul key={index}>
                  <Link
                    to={item.path}
                    className="block text-base text-white uppercase cursor-pointer hover:text-black-700"
                  >
                    {item.label}
                  </Link>
                </ul>
              ))}
            </div>
          </div>
        </nav>
      </header>
    </div>
  );
};

export default Navbar;
