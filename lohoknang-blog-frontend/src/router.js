import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: "hash",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("./views/Home.vue")
    },
    {
      path: "/contact",
      name: "contact",
      component: () => import("./views/Contact.vue")
    },
    {
      path: "/about",
      name: "about",
      component: () => import("./views/About.vue")
    },
    {
      path: "/blogs/:id",
      name: "blog",
      component: () => import("./views/Blog.vue")
    },
    {
      path: "/editor",
      name: "editor",
      component: () => import("./views/MarkedEditor.vue")
    },
    {
      path: "*",
      name: "NotFound",
      component: () => import("./views/NotFound")
    }
  ]
});
