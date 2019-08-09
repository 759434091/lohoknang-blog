<template>
  <transition name="fade">
    <div class="bottom-bar" v-if="bar.show">
      <el-row>
        <el-col>
          <el-button
            type="primary"
            size="mini"
            @click="collapse"
            v-viewport="handlebar"
            >收起</el-button
          >
        </el-col>
      </el-row>
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
  background-color: #ffffff;
  display: flex;
  justify-content: flex-end;
  position: fixed;
  bottom: 15px;
  width: 640px;
  height: 40px;
}

.fade-enter-active {
  transition: all 0.2s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
