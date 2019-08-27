import Vue from "vue";
import hljs from "hljs";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./plugins/axios";
import "./plugins/inViewport.js";

Vue.config.productionTip = false;

Vue.directive("highlight", el => {
  let blocks = el.querySelectorAll("pre code");
  blocks.forEach(block => hljs.highlightBlock(block));
});

// noinspection JSUnusedGlobalSymbols
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
