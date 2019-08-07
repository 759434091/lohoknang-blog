<template>
  <div class="blog-intro">
    <div class="blog-intro-title" v-text="blog.title"></div>
    <div class="blog-intro-text" @click="introClick">
      <span v-text="blog.intro"></span>
      <span>…</span>
      <el-button class="blog-intro-button" type="text" :underline="false">
        阅读全文
        <i class="el-icon-arrow-down"></i>
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "BlogIntro",
  props: {
    blog: Object
  },
  methods: {
    introClick() {
      this.$emit("intro-click", this.blog.id);
    }
  }
};
</script>

<style scoped>
.blog-intro {
  padding: 15px;
}

.blog-intro-title {
  cursor: pointer;
  line-height: 1.7;
  font-size: 20px;
  color: #606266;
  margin: 10px 0;
}

.blog-intro-title:hover {
  color: #409eff;
}

.blog-intro-text {
  cursor: pointer;
  line-height: 1;
  font-size: 14px;
  color: #606266;
}

.blog-intro-text:hover {
  color: #909399;
}

.blog-intro-button {
  margin: 0 5px;
}
</style>
