<template>
  <el-container class="blog-container">
    <el-main>
      <div class="blog-title" v-text="blog.title"></div>
      <div class="blog-content">
        <div
          class="blog-content-info"
          v-text="
            `${blog.author} | ${blog.category} |
      ${new Date(blog.createdAt).toLocaleString()} |
      ${new Date(blog.updatedAt).toLocaleString()}`
          "
        ></div>
        <div v-html="marked"></div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import marked from "marked";

marked.setOptions({
  breaks: true
});

export default {
  name: "Blog",
  data() {
    return {
      blog: null,
      marked: ""
    };
  },
  created() {
    const id = this.$route.params.id;
    this.$http
      .get(`/blogs/${id}`)
      .then(res => {
        this.blog = res.data;
        this.marked = marked(this.blog.content);
      })
      .catch(err => {
        this.$message.error(err);
      });
  }
};
</script>

<style scoped>
.blog-container {
  display: flex;
  width: 1024px;
  margin: 0 auto;
}

.blog-title {
  line-height: 1.7;
  font-size: 20px;
  color: #606266;
  margin: 10px 0;
}

.blog-content {
  line-height: 1.5;
  color: #606266;
}

.blog-content-info {
  color: #909399;
  font-size: 13px;
}
</style>
