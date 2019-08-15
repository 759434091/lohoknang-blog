<template>
  <div class="blog-intro">
    <div class="blog-intro-title" v-text="blog.title"></div>
    <div class="blog-intro-text" @click="introClick" v-if="blogDetail == null">
      <div v-if="blogDetail == null">
        <span v-text="blog.intro"></span>
        <span>…</span>
        <el-button class="blog-intro-button" type="text" :underline="false">
          阅读全文
          <i class="el-icon-arrow-down"></i>
        </el-button>
      </div>
    </div>
    <div class="blog-intro-content" v-else>
      <div
        class="blog-intro-content-info"
        v-text="
          `${blog.author} | ${blog.category} |
      ${new Date(blog.createdAt).toLocaleString()} |
      ${new Date(blog.updatedAt).toLocaleString()}`
        "
      ></div>
      <div v-html="blogDetail.content"></div>
      <div class="blog-intro-content-footer">
        <el-row type="flex" justify="end" :class="buttonClass">
          <el-button
            type="primary"
            size="mini"
            @click="collapse"
            v-viewport="handlebar"
            >收起</el-button
          >
        </el-row>
        <BottomBar :bar="bar" @collapse="collapse"></BottomBar>
      </div>
    </div>
  </div>
</template>

<script>
import marked from "marked";
import BottomBar from "./BottomBar";

marked.setOptions({
  breaks: true
});

export default {
  name: "BlogIntro",
  props: {
    blog: Object
  },
  components: { BottomBar },
  data() {
    return {
      blogDetail: null,
      buttonClass: "",
      bar: {
        show: false,
        id: this.blog.id
      },
      lastScrollTop: document.querySelector("html").scrollTop
    };
  },
  methods: {
    introClick() {
      this.lastScrollTop = document.querySelector("html").scrollTop;
      this.$http
        .get(`/blogs/${this.blog.id}`)
        .then(res => {
          this.blogDetail = res.data;
          this.blogDetail.content = marked(
            this.blogDetail.content.replace(/\\r|\\n/g, "\n")
          );
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    collapse() {
      this.blogDetail = null;
      document.querySelector("html").scrollTop = this.lastScrollTop;
    },
    handlebar(dataset, flag) {
      this.bar.show = !flag;
    }
  }
};
</script>
<style scoped>
.blog-intro {
  width: 640px;
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
  line-height: 1.5;
  font-size: 14px;
  color: #606266;
}

.blog-intro-text:hover {
  color: #909399;
}

.blog-intro-button {
  padding: 0;
  margin: 0 5px;
}

.blog-intro-content {
  line-height: 1.5;
  color: #606266;
}

.blog-intro-content-info {
  color: #909399;
  font-size: 13px;
}

.blog-intro-content-footer {
  padding: 20px 0 0 0;
}
</style>
