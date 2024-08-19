import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Home from "../screen/Home";
import SignUp from "../components/home/Signup";
import LogIn from "../components/home/LogIn";
import LogOut from "../components/home/LogOut";
import ForgotPassword from "../components/home/ForgotPassword";
import ResetPassword from "../components/home/ResetPassword";
import EditInfo from "../components/updateinfor/EditInfo";

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
    ],
  },
]);

export default router;
