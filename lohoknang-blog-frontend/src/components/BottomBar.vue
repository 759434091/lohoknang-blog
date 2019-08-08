<template>
    <transition name="slide">
        <div class="bottom-bar" v-if="bar.show">
            <div>赞同</div>
            <div>评论</div>
            <div>收起</div>
        </div>
    </transition>
</template>

<script>

    export default {
        name: "BlogIntro",
        props: {
            bar: Object,
        },
        data() {
            return {
            };
        },
        computed: {
        },
        methods: {

        }
    };
</script>

<style scoped>
    .bottom-bar {
        display: flex;
        justify-content: space-around;
        background: #66b1ff;
        position: fixed;
        bottom: 0;
        left: 0;
        height: 50px;
        width: 100%;
    }
    .bottom-bar div {

    }
    .slide-enter-active, .slide-leave-active {
        transition: all .5s;
    }
    .slide-enter, .slide-leave-to  {
        bottom: -50px;
    }

</style>
