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
</style>
