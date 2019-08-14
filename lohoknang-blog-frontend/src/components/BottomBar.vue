<template>
  <transition name="fade">
    <div class="bar" v-if="bar.show">
      <div class="bottom-bar">
        <el-row type="flex" align="middle">
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
    </div>
  </transition>
</template>

<script>
export default {
  name: "BlogIntro",
  props: {
    bar: Object,
    id: String
  },
  methods: {
    collapse() {
      this.$emit("collapse", this.id);
    }
  }
};
</script>

<style scoped>
.bottom-bar {
  background-color: #ffffff;
  display: flex;
  justify-content: flex-end;
  position: fixed;
  bottom: 0;
  width: 640px;
  height: 45px;
}
.bar::before {
  content: "" !important;
  width: 640px;
  height: 45px;
  border-radius: 50%;
  box-shadow: 0 -11px 11px -11px #c0c4cc;
  position: fixed;
  bottom: 0;
}

.fade-enter-active {
  transition: all 0.2s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
