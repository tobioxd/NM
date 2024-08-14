const LogOut = () => {

    localStorage.removeItem("user");
    localStorage.removeItem("token");
    localStorage.removeItem("password");
    window.location.href = "/";
}

export default LogOut