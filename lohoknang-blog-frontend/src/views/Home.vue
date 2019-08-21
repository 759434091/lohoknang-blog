<template>
  <div class="blog-home-container" ref="blog-home-container" @scroll="scroll">
    <el-container class="blog-home-main-container">
      <!--suppress HtmlUnknownAttribute -->
      <el-main class="blog-home-main">
        <ul class="blog-home-infinite-list">
          <li :key="blog.id" class="infinite-list-item" v-for="blog in blogs">
            <BlogIntro :blog="blog" />
            <el-divider></el-divider>
          </li>
        </ul>
      </el-main>
      <el-aside class="blog-home-aside">
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
              <el-link
                class="blog-home-aside-link"
                icon="el-icon-news"
                :href="`/#/blogs/${it.id}`"
              >
                <span v-text="it.title"></span>
              </el-link>
            </li>
          </ul>
        </div>
      </el-aside>
    </el-container>
    <infinite-loading class="infinite-div" @infinite="load"></infinite-loading>
  </div>
</template>

<script>
import InfiniteLoading from "vue-infinite-loading";
import BlogIntro from "../components/BlogIntro";

export default {
  name: "Home",
  components: { BlogIntro, InfiniteLoading },
  data() {
    return {
      query: this.$route.query,
      scrollTop: 0,
      top: 5,
      page: 0,
      blogs: [],
      categories: [],
      dates: [],
      updateds: []
    };
  },
  watch: {
    "$route.query"(newQuery) {
      if (
        !(
          newQuery.type === this.query.type &&
          newQuery.value === this.query.value
        )
      ) {
        this.page = 0;
        this.blogs = [];
        this.scrollTop = 0;
        this.load();
      }
      this.query = newQuery;
      this.$refs["blog-home-container"].scrollTop = this.scrollTop;
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
        this.$message.error(err);
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
        this.$message.error(err);
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
        this.$message.error(err);
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
    load($state) {
      this.$http
        .get("/blogs", { params: this.getParams() })
        .then(res => {
          if (res.data.length === 0) {
            $state.complete();
            return;
          }
          this.page++;
          this.blogs.push(...res.data);
          $state.loaded();
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    getDateText(date) {
      const arr = date.split("-");
      return `${arr[0]}年 - ${arr[1]}月`;
    },
    scroll($event) {
      this.scrollTop = $event.target.scrollTop;
    }
  }
};
</script>

<style scoped>
.blog-home-container {
  width: 100vw;
  height: calc(100vh - 60px);
  overflow-y: auto;
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
  width: 300px !important;
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

.infinite-div {
  width: 1024px;
}
</style>
