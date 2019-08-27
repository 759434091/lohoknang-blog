<template>
  <el-container class="editor-container">
    <el-main class="editor-main">
      <el-row type="flex" gutter="10" class="editor-row">
        <el-col :span="24">
          <el-form ref="form" :model="form" size="mini" :inline="true">
            <el-form-item label="标题">
              <el-input v-model="form.title" placeholder="请输入标题"></el-input
            ></el-form-item>
            <el-form-item label="分类">
              <el-input
                v-model="form.category"
                placeholder="请输入分类"
              ></el-input
            ></el-form-item>
            <el-form-item label="作者">
              <el-input
                v-model="form.author"
                placeholder="请输入作者"
              ></el-input
            ></el-form-item>
            <el-form-item>
              <el-button type="primary">提交</el-button>
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
            <el-input
              type="textarea"
              :autosize="{ minRows: 100 }"
              placeholder="请输入内容"
              v-model="form.content"
            >
            </el-input>
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

marked.setOptions({
  breaks: true
});

const titleKey = "blog-editor-form-title";
const categoryKey = "blog-editor-form-category";
const authorKey = "blog-editor-form-author";
const contentKey = "blog-editor-form-content";

export default {
  name: "MarkedEditor",
  computed: {
    markedStr() {
      return marked(this.form.content);
    }
  },
  watch: {
    "form.title"(newTitle) {
      localStorage.setItem(titleKey, newTitle);
    },
    "form.category"(newCategory) {
      localStorage.setItem(categoryKey, newCategory);
    },
    "form.author"(newAuthor) {
      localStorage.setItem(authorKey, newAuthor);
    },
    "form.content"(newContent) {
      localStorage.setItem(contentKey, newContent);
    }
  },
  data() {
    return {
      form: {
        title: "",
        category: "",
        author: "a9043",
        content: ""
      }
    };
  },
  created() {
    this.getLocalBlog();
  },
  beforeDestroy() {
    localStorage.setItem(titleKey, this.form.title);
    localStorage.setItem(categoryKey, this.form.category);
    localStorage.setItem(authorKey, this.form.author);
    localStorage.setItem(contentKey, this.form.content);
  },
  methods: {
    getLocalBlog() {
      const title = localStorage.getItem(titleKey);
      if (title != null) {
        this.form.title = title;
      }

      const category = localStorage.getItem(categoryKey);
      if (category != null) {
        this.form.category = category;
      }

      const author = localStorage.getItem(authorKey);
      if (author != null) {
        this.form.author = author;
      }

      const content = localStorage.getItem(contentKey);
      if (content != null) {
        this.form.content = content;
      }
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
  padding: 20px;
}
</style>
