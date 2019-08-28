<template>
  <div class="about-container">
    <el-container class="about-main-container">
      <el-header>
        <h1 class="about-brand">关于我</h1>
      </el-header>
      <el-main>
        <div class="about-img-container">
          <el-image fit="contain" :src="`${publicPath}blog-logo.jpeg`">
          </el-image>
        </div>
        <pre class="self-intro">
  UESTC准备毕业。实习刚离职，2020年应届生

  全栈开发，熟悉 Vue 全家桶 + Spring Boot 全家桶。会点Node.JS写写爬虫之类好玩的。

  会点微服务，会点RPC，会点响应式，还想学点DDD……

  算法及其乐色，满脑子架构系统。

  涉猎比较广泛，不定时更新。
        </pre>
        <el-row justify="end" type="flex" class="self-intro">
          2019/08/27
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "About",
  data() {
    return {
      publicPath: process.env.BASE_URL
    };
  }
};
</script>

<style scoped>
.about-container {
  width: 100vw;
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.about-main-container {
  display: flex;
  width: 768px;
  margin: 20px auto;
}

.about-brand {
  font-weight: normal;
  font-size: 20px;
  color: #606266;
}

.self-intro {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-weight: normal;
  font-size: 16px;
  color: #606266;
}

.about-img-container {
  margin: 0 auto;
  width: 300px;
  height: 300px;
}
</style>
