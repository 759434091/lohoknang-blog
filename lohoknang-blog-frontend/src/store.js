import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    auth: null
  },
  mutations: {
    setAuth(state, auth) {
      state.auth = auth;
    }
  },
  actions: {}
});
