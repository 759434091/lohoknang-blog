<template>
  <el-container>
    <el-header>
      <el-row :gutter="10">
        <el-col :span="3">
          <div class="blog-brand">
            lo's blog
          </div>
        </el-col>
        <el-col :span="21">
          <el-menu
            :router="true"
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
          >
            <el-menu-item index="/">POST</el-menu-item>
            <el-menu-item index="/support">SUPPORT</el-menu-item>
            <el-menu-item index="/about">ABOUT</el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </el-header>
    <el-container class="blog-home-container">
      <el-main class="blog-home-main">
        <!--suppress HtmlUnknownAttribute -->
        <ul class="blog-home-infinite-list" v-infinite-scroll="load">
          <li v-for="blog in blogs" :key="blog.id" class="infinite-list-item">
            <BlogIntro :blog="blog" />
            <el-divider></el-divider>
          </li>
        </ul>
      </el-main>
      <el-aside class="blog-home-aside" width="300px"></el-aside>
    </el-container>
  </el-container>
</template>

<script>
import BlogIntro from "../components/BlogIntro";

export default {
  name: "Home",
  components: { BlogIntro },
  data() {
    return {
      activeIndex: "/",
      page: 0,
      type: "raw",
      value: "",
      blogs: []
    };
  },
  methods: {
    load() {
      this.$http
        .get("/blogs", {
          params: {
            type: this.type,
            value: this.value,
            page: this.page
          }
        })
        .then(res => {
          this.page++;
          this.blogs.push(...res.data);
        })
        .catch(err => {
          console.error(err);
        });
    }
  }
};
</script>

<style scoped>
.blog-brand {
  font-size: 20px;
  line-height: 60px;
  color: #606266;
  text-align: center;
  height: 60px;
}

.blog-home-container {
  padding: 10px 100px;
  height: calc(100vh - 80px);
}

.blog-home-infinite-list {
  list-style: none;
}
</style>
