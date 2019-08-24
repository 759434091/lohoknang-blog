import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

export default new Router({
    mode: 'hash',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'root',
            redirect: '/blogs'
        },
        {
            path: '/blogs',
            name: 'blogPage',
            meta: {
                keepAlive: true
            },
            component: () => import('./views/BlogPage.vue'),
        }, {
            path: '/blogs/:blogId',
            name: 'blogContent',
            component: () => import('./views/BlogContent.vue'),
            props: true
        }, {
            path: '/contact',
            name: 'contactPage',
            component: () => import('./views/ContactPage.vue')
        }, {
            path: '/about',
            name: 'aboutPage',
            component: () => import('./views/AboutPage.vue')
        }
    ]
})
