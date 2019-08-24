<template>
    <div id="app">
        <mu-container class="main-container">
            <mu-tabs :value.sync="active" indicator-color="primary" inverse center>
                <mu-tab to="/blogs">BLOG</mu-tab>
                <mu-tab to="/contact">CONTACT</mu-tab>
                <mu-tab to="/about">ABOUT</mu-tab>
            </mu-tabs>
            <mu-fade-transition :duration="{ enter: 800, leave: 0 }">
                <keep-alive>
                    <router-view v-if="$route.meta.keepAlive"/>
                </keep-alive>
            </mu-fade-transition>
            <mu-fade-transition :duration="{ enter: 800, leave: 0 }">
                <router-view v-if="!$route.meta.keepAlive"></router-view>
            </mu-fade-transition>
        </mu-container>
    </div>
</template>

<script>
    export default {
        name: 'App',
        watch: {
            '$route'(to) {
                switch (to.name) {
                    case 'home': {
                        this.active = 0;
                        break;
                    }
                    case 'blogPage': {
                        this.active = 1;
                        break;
                    }
                    case 'blogContent': {
                        this.active = 1;
                        break;
                    }
                    case 'contactPage': {
                        this.active = 2;
                        break;
                    }
                    case 'aboutPage': {
                        this.active = 3;
                        break;
                    }
                }
            }
        },
        data() {
            return {
                active: 0
            }
        }
    }
</script>

<style>
    pre {
        white-space: pre !important;
        overflow-x: auto !important;
    }

    .main-container {
        padding: 0 !important;
    }
</style>
