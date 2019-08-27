<template>
  <div id="app">
    <el-container class="app-container">
      <el-header class="app-header">
        <el-row>
          <el-col :span="3">
            <div class="app-brand">
              <a class="app-brand-link" href="/#/">A9043 BLOG</a>
            </div>
          </el-col>
          <el-col :span="21">
            <el-menu
              :router="true"
              :default-active="activeIndex"
              class="app-menu"
              mode="horizontal"
            >
              <el-menu-item class="app-menu-item" index="/">
                <a href="/#/" class="app-menu-link">POST</a>
              </el-menu-item>
              <el-menu-item class="app-menu-item" index="/contact">
                <a href="/#/contact" class="app-menu-link">CONTACT</a>
              </el-menu-item>
              <el-menu-item class="app-menu-item" index="/about">
                <a href="/#/about" class="app-menu-link">ABOUT</a>
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>
      <keep-alive include="Home">
        <router-view></router-view>
      </keep-alive>
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
body {
  margin: 0;
}

#app {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}

.app-header {
  height: 60px !important;
}

.app-brand {
  height: 60px;
  font-size: 20px;
  line-height: 60px;
  color: #606266;
  text-align: center;
  border-bottom: solid 1px #e6e6e6;
}

.app-menu-item {
  line-height: 54px !important;
  height: 60px !important;
  font-size: 16px;
}

.app-brand-link,
.app-menu-link {
  color: unset;
  text-decoration: unset;
}

.blog-content,
.blog-intro-content,
.editor-preview {
  font-size: 14px;
}

.blog-content img,
.blog-intro-content img,
.editor-preview img {
  width: 100%;
}

.blog-content pre,
.blog-intro-content pre,
.editor-preview pre {
  overflow: auto;
}

.blog-content a,
.editor-preview a,
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
