import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Home from "../screen/Home";
import SignUp from "../components/home/authentication/Signup";
import LogIn from "../components/home/authentication/LogIn";
import LogOut from "../components/home/authentication/LogOut";
import ForgotPassword from "../components/home/authentication/ForgotPassword";
import ResetPassword from "../components/home/authentication/ResetPassword";
import EditInfo from "../components/updateinfor/EditInfo";
import Success from "../components/home/authentication/Success";
import DashboardLayout from "../components/dashboard/DashboardLayout";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "login",
        element: <LogIn />,
      },
      {
        path: "logout",
        element: <LogOut />,
      },
      {
        path: "sign-up",
        element: <SignUp />,
      },
      {
        path: "forgot-password",
        element: <ForgotPassword />,
      },
      {
        path: "reset-password/:token",
        element: <ResetPassword />,
      },
      {
        path: "edit-info",
        element: <EditInfo />,
      },
      {
        path: "success",
        element: <Success />,
      },
      {
        path: "admin-dashboard",
        element: <DashboardLayout />,
      }
    ],
  },
]);

export default router;
