import flowbite from "flowbite-react/tailwind";
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
    "node_modules/react-router-dom/**/*.{js,ts,jsx,tsx}",
    "./node_modules/flowbite/**/*.js",
    flowbite.content(),
  ],
  theme: {
    zIndex: {
      100: "100",
      50: "50",
    },
  },
  // eslint-disable-next-line no-undef
  plugins: [require("flowbite/plugin"), flowbite.plugin()],
};
