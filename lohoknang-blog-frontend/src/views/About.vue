<template>
  <div class="about-container">
    <el-container class="about-main-container">
      <el-header>
        <h1 class="about-brand">关于我</h1>
      </el-header>
      <el-main>
        <el-row type="flex">
          <el-col :span="12">
            <el-form class="intro-form" size="medium" label-position="left">
              <el-form-item label="BLOG">
                <el-link href="https://lohoknang.com">
                  LOHOKNANG.COM
                </el-link>
              </el-form-item>
              <el-form-item label="GITHUB">
                <el-link href="https://github.com/759434091"
                  >HOKNANG_LO
                </el-link>
              </el-form-item>
              <el-form-item label="MAIL">
                <el-link href="mailto:zzz13129180808@Gmail.com"
                  >blog@lohoknang.com
                </el-link>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="12">
            <div class="about-img-container">
              <el-image fit="contain" :src="`${publicPath}blog-logo.jpeg`">
              </el-image>
              <div class="login-box" @click="loginClick"></div>
            </div>
          </el-col>
        </el-row>
        <pre class="self-intro">
  UESTC在读, 准入职。

  全栈开发，熟悉 Vue 全家桶 + Spring Boot 全家桶。会点Node.JS写写爬虫之类好玩的。

  算法极其乐色。涉猎比较广泛，不定时更新。
        </pre>
        <el-row justify="end" type="flex" class="self-intro">
          2019/08/27
        </el-row>
      </el-main>
      <LoginBox :visible="loginBoxVisible" @close="closeLoginBox"></LoginBox>
    </el-container>
  </div>
</template>

<script>
import LoginBox from "../components/LoginBox";
export default {
  name: "About",
  components: { LoginBox },
  data() {
    return {
      publicPath: process.env.BASE_URL,
      timer: null,
      click: 0,
      loginBoxVisible: false
    };
  },
  methods: {
    loginClick() {
      if (this.timer == null) {
        this.timer = setTimeout(() => {
          this.timer = null;
          this.click = 0;
        }, 800);
      }

      this.click += 1;
      this.$message({
        duration: 300,
        dangerouslyUseHTMLString: true,
        message: `<i class="el-icon-bell"></i>`
      });

      if (this.click === 3) {
        this.loginBoxVisible = true;
      }
    },
    closeLoginBox() {
      this.loginBoxVisible = false;
    }
  }
};
</script>

<style>
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

.intro-form {
  margin-top: 20px;
}

.intro-form,
.intro-form label,
.intro-form span {
  font-weight: normal;
  font-size: 16px;
  color: #606266;
}

.intro-form label {
  width: 80px;
}

.self-intro {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-weight: normal;
  font-size: 16px;
  color: #606266;
}

.about-img-container {
  position: relative;
  margin: 0 auto;
  width: 150px;
  height: 150px;
}

.login-box {
  position: absolute;
  top: 90px;
  left: 90px;
  width: 10px;
  height: 10px;
  background-color: transparent;
}
</style>
