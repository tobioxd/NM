import { Footer } from "flowbite-react";
import { BsLinkedin , BsFacebook, BsGithub, BsInstagram, BsTwitter } from "react-icons/bs";

function Component() {
  return (
    <Footer bgDark className="">
      <div className="w-full">
        <div className="w-full bg-blue-300 px-4 py-6 lg:px-24 sm:flex sm:items-center sm:justify-between">
          <Footer.Copyright href="#" by=" tobioxd™" year={new Date().getFullYear()} />
          <div className="mt-4 flex space-x-6 sm:mt-0 sm:justify-center">
            <Footer.Icon href="https://www.facebook.com/tobioxd/" icon={BsFacebook} />
            <Footer.Icon href="https://www.instagram.com/_tobioxd/" icon={BsInstagram} />
            <Footer.Icon href="https://twitter.com/_tobioxd/" icon={BsTwitter} />
            <Footer.Icon href="https://github.com/tobioxd/" icon={BsGithub} />
            <Footer.Icon href="https://www.linkedin.com/in/tobioxd/" icon={BsLinkedin } />
          </div>
        </div>
      </div>
    </Footer>
  );
}

export default Component;