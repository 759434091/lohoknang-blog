import axios from "axios";
import Vue from "vue";

axios.defaults.baseURL = "http://127.0.0.1:8080";

Vue.prototype.$http = axios;
