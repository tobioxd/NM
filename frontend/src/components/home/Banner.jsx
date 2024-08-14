import React from "react";
import BannerCard from "./BannerCard";
import mal from "../../assets/icon/mal.png";
import crunchyroll from "../../assets/icon/crunchyroll.png";

const Banner = () => {
  return (
    <div className="px-4 lg:px-24 bg-teal-100 flex items-center">
      <div className="flex w-full flex-col md:flex-row justify-between items-center gap-12 py-60">
        {/* left side*/}
        <div className=" md:w-1/2 space-y-8">
          <h1 className="text-4xl font-bold text-gray-800">
            Welcome to <span className="text-red-600">Anime</span> World
          </h1>
          <p className="text-lg text-gray-600 mt-4">
            Welcome to the ultimate anime haven! 🌟 Dive into a world where
            imagination meets reality, and every frame tells a story. Whether
            you’re a seasoned otaku or new to the realm of anime, we’ve got
            something special for you. From the classics that set our hearts
            racing to the latest releases that have us on the edge of our seats,
            this is your one-stop destination for all things anime.
          </p>
          <div>
            <div className="flex items-center">
              <p className="text-lg text-sky-600 font-semibold underline">
                Explore more about animes and mangas:
              </p>
              <a href="https://myanimelist.net/">
                <img
                  src={mal}
                  className="w-7 h-7 rounded-full cursor-pointer mr-2 ml-2"
                />
              </a>
            </div>
            <div className="flex items-center">
              <p className="text-lg text-sky-600 mt-2 font-semibold underline">
                Wanna watch animes? You can visit:
              </p>
              <a href="https://www.crunchyroll.com/">
                <img
                  src={crunchyroll}
                  className="w-7 h-7 rounded-full cursor-pointer mr-2 ml-2 mt-2"
                />
              </a>
            </div>
          </div>
        </div>

        {/* right side*/}
        <div>
          <BannerCard />
        </div>
      </div>
    </div>
  );
};

export default Banner;
