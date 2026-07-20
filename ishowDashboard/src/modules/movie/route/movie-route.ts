import { ROUTES_ENUM } from "../../../utils/constants";
import MovieList from "../views/MovieList.vue";
import ViewMovieBanner from "../views/ViewMovieBanner.vue";
import ViewMovieSections from "../views/ViewMovieSections.vue";
import ViewSectionMovies from "../views/ViewSectionMovies.vue";

export const movieRoute = [
  {
    path: ROUTES_ENUM.MOVIE.BASE + ROUTES_ENUM.MOVIE.FILTER,
    name: "Movies",
    component: MovieList,
  },
  {
    path:
      ROUTES_ENUM.MOVIE.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Movies For Section",
    component: ViewSectionMovies,
    props: true,
  },
  {
    path: ROUTES_ENUM.MOVIE.BASE + ROUTES_ENUM.MOVIE.VIEW_MOVIE_SECTIONS,
    name: "View Movie Sections",
    component: ViewMovieSections,
  },
  {
    path: ROUTES_ENUM.MOVIE.BASE + ROUTES_ENUM.MOVIE.VIEW_MOVIE_BANNER,
    name: "View Movie Banner",
    component: ViewMovieBanner,
  },
];
