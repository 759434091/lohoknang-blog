<template>
  <div class="blog-container">
    <el-container class="blog-main-container">
      <el-main v-if="blog != null">
        <div class="blog-title" v-text="blog.title"></div>
        <div class="blog-content">
          <div class="blog-content-info">
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
              <el-button class="blog-content-edit" type="text" @click="edit"
                >编辑</el-button
              >
            </span>
          </div>
          <div v-html="marked" v-highlight></div>
        </div>
        <el-footer class="blog-footer">
          <el-row type="flex">
            <el-col>
              <el-link href="/#/" class="blog-back" icon="el-icon-d-arrow-left"
                >返回</el-link
              >
            </el-col>
          </el-row>
        </el-footer>
      </el-main>
      <el-backtop target=".blog-container"></el-backtop>
    </el-container>
  </div>
</template>

<script>
import marked from "marked";
import storage from "../plugins/storage";

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
  computed: {
    auth() {
      return this.$store.state.auth;
    }
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
  },
  methods: {
    edit() {
      localStorage.setItem(storage.titleKey, this.blog.title);
      localStorage.setItem(storage.categoryKey, this.blog.category);
      localStorage.setItem(storage.authorKey, this.blog.author);
      localStorage.setItem(storage.contentKey, this.blog.content);
      localStorage.setItem(storage.idKey, this.blog.id);
      this.$router.push("/editor");
    }
  }
};
</script>

<style scoped>
.blog-container {
  width: 100vw;
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.blog-main-container {
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

.blog-content-info span {
  color: #909399;
  font-size: 13px;
}

.blog-content-edit {
  font-size: 13px;
}

.blog-footer {
  margin: 40px 0;
}

.blog-back {
  font-size: 20px;
  font-weight: normal;
  line-height: 1.7;
  color: #606266;
}
</style>
