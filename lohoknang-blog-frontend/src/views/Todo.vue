<template>
  <el-container class="blog-not-found-container">
    <el-main class="blog-not-found-main">
      <el-row type="flex" justify="center">
        <el-card>
          <div slot="header">
            <div class="blog-not-found-text">还没写完 ~</div>
          </div>
          <img
            src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
            class="image"
          />
          <p class="blog-not-found-time" v-text="date"></p>
          <el-link href="/#/" type="primary" class="blog-not-found-back"
            >返回</el-link
          >
        </el-card>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "Todo",
  created() {
    let _this = this;
    this.timer = setInterval(function() {
      _this.date = new Date().toLocaleString();
    });
  },
  data() {
    return {
      timer: null,
      date: new Date().toLocaleString()
    };
  },
  beforeDestroy: function() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
};
</script>

<style scoped>
.blog-not-found-container {
  display: flex;
  width: 1024px;
  margin: 0 auto;
}

.blog-not-found-text {
  font-size: 20px;
  line-height: 1.7;
  color: #606266;
}

.blog-not-found-time {
  font-size: 18px;
  line-height: 1.5;
  color: #909399;
}

.blog-not-found-back {
  font-size: 18px;
  line-height: 1.5;
  font-weight: normal;
}
</style>
