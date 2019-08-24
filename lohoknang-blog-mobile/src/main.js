import Vue from 'vue'
import App from './App.vue'
import router from './router'
import request from '@/request'
import hljs from 'hljs'

Vue.config.productionTip = false
Vue.prototype.$request = request

Vue.directive('highlight', (el) => {
    const blocks = el.querySelectorAll('pre code')
    blocks.forEach((block) => {
        hljs.highlightBlock(block)
    })
})

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
