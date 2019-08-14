<template>
  <div id="app">
    <el-container class="app-container">
      <el-header>
        <el-row>
          <el-col :span="3">
            <div class="app-brand">
              <a class="app-brand-link" href="/#/">lo's blog</a>
            </div>
          </el-col>
          <el-col :span="21">
            <el-menu
              :router="true"
              :default-active="activeIndex"
              class="app-menu"
              mode="horizontal"
            >
              <el-menu-item index="/">
                <a href="/#/" class="app-menu-link">POST</a>
              </el-menu-item>
              <el-menu-item index="/support">
                <a href="/#/support" class="app-menu-link">SUPPORT</a>
              </el-menu-item>
              <el-menu-item index="/about">
                <a href="/#/about" class="app-menu-link">ABOUT</a>
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>
      <router-view></router-view>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "app",
  watch: {
    $route(newRoute) {
      this.activeIndex = newRoute.path;
    }
  },
  data() {
    return {
      activeIndex: this.$route.path
    };
  }
};
</script>

<style>
#app {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}

.app-brand {
  font-size: 20px;
  line-height: 60px;
  color: #606266;
  text-align: center;
  height: 60px;
  border-bottom: solid 1px #e6e6e6;
}

.app-brand-link,
.app-menu-link {
  color: unset;
  text-decoration: unset;
}

.blog-intro-content pre {
  overflow: auto;
}

.blog-intro-content a {
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  vertical-align: middle;
  position: relative;
  text-decoration: none;
  outline: none;
  cursor: pointer;
  padding: 0;
  color: #409eff;
}

.blog-intro-content a:hover {
  color: #66b1ff;
}
</style>
