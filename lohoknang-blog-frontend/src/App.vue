<template>
  <div id="app">
    <Home />
  </div>
</template>

<script>
import Home from "./views/Home";

export default {
  name: "app",
  components: { Home }
};
</script>

<style>
#app {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
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
