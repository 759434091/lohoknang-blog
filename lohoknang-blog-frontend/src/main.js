import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./plugins/element";
import "./plugins/axios";
import "./plugins/inViewport.js";
import "./plugins/freshScroll.js";

Vue.config.productionTip = false;

// noinspection JSUnusedGlobalSymbols
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
