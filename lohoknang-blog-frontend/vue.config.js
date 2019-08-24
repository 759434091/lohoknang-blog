module.exports = {
  configureWebpack: {
    externals: {
      vue: "Vue",
      "vue-router": "VueRouter",
      vuex: "Vuex",
      axios: "axios",
      "element-ui": "ELEMENT",
      marked: "marked",
      "vue-infinite-loading": "VueInfiniteLoading"
    }
  }
};
