import axios from "axios";
import Vue from "vue";

// axios.defaults.baseURL = "http://127.0.0.1:8080";
axios.defaults.baseURL = "http://188.119.103.206:8080";

// noinspection JSUnusedGlobalSymbols
Vue.prototype.$http = axios;
