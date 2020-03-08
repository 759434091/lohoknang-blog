<template>
  <div class="blog-intro">
    <div class="blog-intro-title">
      <a
        class="blog-intro-title-link"
        :href="`/#/blogs/${blog.id}`"
        v-text="blog.title"
      ></a>
    </div>
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
      <div class="blog-intro-content-info">
        <span
          v-text="
            `${blog.author} | ${blog.category} |
      ${new Date(blog.createdAt).toLocaleString()} |
      ${new Date(blog.updatedAt).toLocaleString()} |
      ${blog.viewNum} 阅读`
          "
        ></span>
        <span v-if="this.auth != null">
          |
          <el-button type="text" class="blog-intro-content-edit" @click="edit"
            >编辑</el-button
          ></span
        >
      </div>
      <div v-html="marked" v-highlight></div>
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
import storage from "../plugins/storage";
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
  computed: {
    auth() {
      return this.$store.state.auth;
    }
  },
  data() {
    return {
      blogDetail: null,
      marked: "",
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
          this.marked = marked(this.blogDetail.content);
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
    },
    edit() {
      localStorage.setItem(storage.titleKey, this.blogDetail.title);
      localStorage.setItem(storage.categoryKey, this.blogDetail.category);
      localStorage.setItem(storage.authorKey, this.blogDetail.author);
      localStorage.setItem(storage.contentKey, this.blogDetail.content);
      localStorage.setItem(storage.idKey, this.blogDetail.id);
      this.$router.push("/editor");
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
  margin: 10px 0;
}

.blog-intro-title-link {
  line-height: 1.7;
  font-size: 20px;
  color: #606266;
  text-decoration: unset;
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

.blog-intro-content-info span {
  color: #909399;
  font-size: 12px;
}

.blog-intro-content-edit {
  font-size: 12px;
}

.blog-intro-content-footer {
  padding: 20px 0 0 0;
}
</style>
