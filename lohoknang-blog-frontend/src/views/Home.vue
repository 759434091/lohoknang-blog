<template>
  <el-container class="blog-home-main-container">
    <!--suppress HtmlUnknownAttribute -->
    <el-main class="blog-home-main">
      <ul class="blog-home-infinite-list" v-fresh-scroll="load">
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
              :href="`/#/?type=category&value=${it}`"
              class="blog-home-aside-link"
              icon="el-icon-collection-tag"
              ><span v-text="it.toUpperCase()"></span>
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
            <el-link
              class="blog-home-aside-link"
              icon="el-icon-date"
              :href="`/#/?type=date&value=${it}`"
            >
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
</template>

<script>
import BlogIntro from "../components/BlogIntro";

export default {
  name: "Home",
  components: { BlogIntro },
  data() {
    return {
      top: 5,
      page: 0,
      blogs: [],
      categories: [],
      dates: [],
      updateds: []
    };
  },
  watch: {
    "$route.query"() {
      this.page = 0;
      this.blogs = [];
      this.load();
    }
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
    getParams() {
      let params;
      if (this.$route.query.type != null && this.$route.query.value != null) {
        params = {
          type: this.$route.query.type,
          value: this.$route.query.value,
          page: this.page
        };
      } else {
        params = {
          page: this.page,
          type: "raw",
          value: ""
        };
      }
      return params;
    },
    load() {
      this.$http
        .get("/blogs", { params: this.getParams() })
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
