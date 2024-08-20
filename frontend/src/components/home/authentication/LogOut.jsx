const LogOut = () => {

    localStorage.removeItem("user");
    localStorage.removeItem("token");
    window.location.href = "/";
}

export default LogOut