<template>
  <transition name="slide">
    <div class="bottom-bar" v-if="bar.show">
      <el-button
        type="primary"
        size="mini"
        @click="collapse"
        v-viewport="handlebar"
        >收起</el-button
      >
    </div>
  </transition>
</template>

<script>
export default {
  name: "BlogIntro",
  props: {
    bar: Object
  },
  data() {
    return {};
  },
  computed: {},
  methods: {}
};
</script>

<style scoped>
.bottom-bar {
  display: flex !important;
  justify-content: flex-end !important;
  position: fixed !important;
  bottom: 15px !important;
  right: 455px !important;
  width: 100% !important;
}
</style>
