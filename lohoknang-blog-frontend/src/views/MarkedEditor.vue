<template>
  <el-container class="editor-container">
    <el-main class="editor-main">
      <el-row type="flex" gutter="10" class="editor-row">
        <el-col :span="24">
          <el-form
            ref="form"
            :rules="rules"
            :model="form"
            size="mini"
            :inline="true"
          >
            <el-form-item label="ID">
              <el-input v-model="form.id" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input
                v-model="form.title"
                placeholder="请输入标题"
              ></el-input>
            </el-form-item>
            <el-form-item label="分类" title="category" prop="category">
              <el-input
                v-model="form.category"
                placeholder="请输入分类"
              ></el-input>
            </el-form-item>
            <el-form-item label="作者" prop="author">
              <el-input
                v-model="form.author"
                placeholder="请输入作者"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submit">提交</el-button>
              <el-button type="info" @click="clearId">清除ID</el-button>
              <el-button type="warning" @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-row
        type="flex"
        gutter="10"
        class="editor-row"
        justify="space-between"
      >
        <el-col :span="12">
          <div class="editor-content">
            <el-form ref="contentForm" :rules="rules" :model="form">
              <el-form-item prop="content">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 100 }"
                  placeholder="请输入内容"
                  v-model="form.content"
                >
                </el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
        <el-col :span="12" class="editor-preview">
          <div
            class="editor-preview-marked"
            v-html="markedStr"
            v-highlight
          ></div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import marked from "marked";
import storage from "../plugins/storage";

marked.setOptions({
  breaks: true
});

export default {
  name: "MarkedEditor",
  computed: {
    markedStr() {
      return marked(this.form.content);
    },
    auth() {
      return this.$store.state.auth;
    }
  },
  watch: {
    "form.title"(newTitle) {
      localStorage.setItem(storage.titleKey, newTitle);
    },
    "form.category"(newCategory) {
      localStorage.setItem(storage.categoryKey, newCategory);
    },
    "form.author"(newAuthor) {
      localStorage.setItem(storage.authorKey, newAuthor);
    },
    "form.content"(newContent) {
      localStorage.setItem(storage.contentKey, newContent);
    }
  },
  data() {
    return {
      form: {
        id: "",
        title: "",
        category: "",
        author: "a9043",
        content: ""
      },
      rules: {
        category: [
          { required: true, message: "请输入分类", trigger: "blur" },
          { min: 4, max: 10, message: "长度在 4 到 10 个字符", trigger: "blur" }
        ],
        author: [
          { required: true, message: "请输入作者", trigger: "blur" },
          { min: 4, max: 10, message: "长度在 4 到 10 个字符", trigger: "blur" }
        ],
        title: [
          { required: true, message: "请输入标题", trigger: "blur" },
          { min: 5, max: 60, message: "长度在 5 到 60 个字符", trigger: "blur" }
        ],
        content: [
          { required: true, message: "请输入内容", trigger: "blur" },
          { min: 140, message: "长度在必须大于140个字符", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    if (this.auth == null) {
      this.$router.push("/");
      return;
    }

    this.getLocalBlog();
  },
  beforeDestroy() {
    localStorage.setItem(storage.titleKey, this.form.title);
    localStorage.setItem(storage.categoryKey, this.form.category);
    localStorage.setItem(storage.authorKey, this.form.author);
    localStorage.setItem(storage.contentKey, this.form.content);
    localStorage.setItem(storage.idKey, this.form.id);
  },
  methods: {
    getLocalBlog() {
      const id = localStorage.getItem(storage.idKey);
      if (id != null) {
        this.form.id = id;
      }
      const title = localStorage.getItem(storage.titleKey);
      if (title != null) {
        this.form.title = title;
      }

      const category = localStorage.getItem(storage.categoryKey);
      if (category != null) {
        this.form.category = category;
      }

      const author = localStorage.getItem(storage.authorKey);
      if (author != null) {
        this.form.author = author;
      }

      const content = localStorage.getItem(storage.contentKey);
      if (content != null) {
        this.form.content = content;
      }
    },
    clearId() {
      this.form.id = "";
    },
    reset() {
      this.form = {
        id: "",
        title: "",
        category: "",
        author: "a9043",
        content: ""
      };
    },
    submit() {
      if (this.form.id == null) {
        this.insert();
        return;
      }

      this.update();
    },
    insert() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return;
        }

        const _this = this;
        this.$refs.contentForm.validate(valid => {
          if (!valid) {
            return;
          }

          const blog = {
            title: _this.form.title,
            category: _this.form.category,
            author: _this.form.author,
            content: _this.form.content
          };

          debugger;
          this.$http
            .post("/blogs", blog, {
              headers: {
                Authorization: _this.auth
              }
            })
            .then(res => {
              this.$message.success(`发布成功 ${res.data}`);
              this.$router.push(`/?time=${new Date().toISOString()}`);
            })
            .catch(err => {
              this.$message.success(`发布失败 ${err}`);
            });
        });
      });
    },
    update() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return;
        }

        const _this = this;
        this.$refs.contentForm.validate(valid => {
          if (!valid) {
            return;
          }

          const blog = {
            id: _this.form.id,
            title: _this.form.title,
            category: _this.form.category,
            author: _this.form.author,
            content: _this.form.content
          };

          _this.$http
            .put(`/blogs/${blog.id}`, blog, {
              headers: {
                Authorization: _this.auth
              }
            })
            .then(res => {
              this.$message.success(`修改成功 ${res.data}`);
              this.$router.push(`/?time=${new Date().toISOString()}`);
            })
            .catch(err => {
              this.$message.success(`修改失败 ${err}`);
            });
        });
      });
    }
  }
};
</script>

<style scoped>
.editor-container {
  width: 100vw;
}

.editor-row {
  margin: 0 !important;
}

.editor-main {
  height: calc(100vh - 60px);
}

.editor-content,
.editor-preview-marked {
  word-wrap: break-word;
  padding: 20px;
}
</style>
