<template>
  <transition name="fade">
    <div class="bar" v-if="bar.show">
      <div class="bottom-bar">
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
.bar::before {
  content: '' !important;
  width: 640px;
  height: 40px;
  border-radius: 50%;
  box-shadow: 0px -11px 11px -11px #C0C4CC;
  position: absolute;
  bottom: 15px;
}

.fade-enter-active {
  transition: all 0.2s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
