<template>
  <el-container class="blog-home-container">
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
    <el-container class="blog-home-main-container">
      <!--suppress HtmlUnknownAttribute -->
      <el-main class="blog-home-main">
        <ul class="blog-home-infinite-list" v-infinite-scroll="load">
          <li :key="blog.id" class="infinite-list-item" v-for="blog in blogs">
            <BlogIntro :blog="blog" />
            <el-divider></el-divider>
          </li>
        </ul>
      </el-main>
      <el-aside class="blog-home-aside" width="300px">
        <div>
          <div class="blog-home-aside-title">
            Category
          </div>
          <ul class="blog-home-aside-list">
            <li :key="idx" v-for="(it, idx) in categories">
              <el-link
                class="blog-home-aside-link"
                icon="el-icon-collection-tag"
              >
                <span v-text="it.toUpperCase()"></span>
              </el-link>
            </li>
          </ul>
        </div>
        <div>
          <div class="blog-home-aside-title">
            Date
          </div>
          <ul class="blog-home-aside-list">
            <li :key="idx" v-for="(it, idx) in dates">
              <el-link class="blog-home-aside-link" icon="el-icon-date">
                <span v-text="getDateText(it)"></span>
              </el-link>
            </li>
          </ul>
        </div>
        <div>
          <div class="blog-home-aside-title">
            Update
          </div>
          <ul class="blog-home-aside-list">
            <li :key="idx" v-for="(it, idx) in updateds">
              <el-link class="blog-home-aside-link" icon="el-icon-news">
                <span v-text="it.title"></span>
              </el-link>
            </li>
          </ul>
        </div>
      </el-aside>
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
      top: 5,
      page: 0,
      type: "raw",
      value: "",
      blogs: [],
      categories: [],
      dates: [],
      updateds: []
    };
  },
  created() {
    this.$http
      .get("/categories", {
        params: {
          top: this.top
        }
      })
      .then(res => {
        this.categories = res.data;
      })
      .catch(err => {
        console.error(err);
      });

    this.$http
      .get("/dates", {
        params: {
          top: this.top
        }
      })
      .then(res => {
        this.dates = res.data;
      })
      .catch(err => {
        console.error(err);
      });

    this.$http
      .get("/updateds", {
        params: {
          top: this.top
        }
      })
      .then(res => {
        this.updateds = res.data;
      })
      .catch(err => {
        console.error(err);
      });
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
    },
    getDateText(date) {
      const arr = date.split("-");
      return `${arr[0]}年 - ${arr[1]}月`;
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
}

.blog-home-main-container {
  display: flex;
  width: 1024px;
  margin: 0 auto;
}

.blog-home-main {
  padding: 0;
}

.blog-home-aside {
  padding-top: 30px;
}

.blog-home-infinite-list {
  padding: 0;
}

.blog-home-infinite-list,
.blog-home-aside-list {
  list-style: none;
}

.blog-home-aside-title {
  padding-left: 40px;
  font-size: 20px;
  line-height: 1.5;
  color: #606266;
}

.blog-home-aside-link {
  font-size: 16px;
  line-height: 1.7;
  max-width: 200px;
  font-weight: normal;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
}
</style>
