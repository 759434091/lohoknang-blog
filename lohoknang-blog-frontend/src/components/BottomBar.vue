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
  display: flex;
  justify-content: flex-end;
  position: fixed;
  bottom: 15px;
  width: 640px;
;
}
</style>
