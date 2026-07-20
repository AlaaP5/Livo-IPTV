import { createRouter, createWebHistory } from "vue-router";
import { routes } from "./routes";
import { ROUTES_ENUM } from "../utils/constants";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const publicPages = [ROUTES_ENUM.AUTH.BASE + ROUTES_ENUM.AUTH.LOGIN];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem("auth_token");

  if (to.path === "/") {
    return next(ROUTES_ENUM.HOME);
  }

  if (authRequired && !loggedIn) {
    return next(ROUTES_ENUM.AUTH.BASE + ROUTES_ENUM.AUTH.LOGIN);
  }
  next();
});

// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
  if (err?.message?.includes?.("Failed to fetch dynamically imported module")) {
    if (!localStorage.getItem("vuetify:dynamic-reload")) {
      localStorage.setItem("vuetify:dynamic-reload", "true");
      location.assign(to.fullPath);
    } else {
    }
  } else {
  }
});

router.isReady().then(() => {
  localStorage.removeItem("vuetify:dynamic-reload");
});

export default router;
