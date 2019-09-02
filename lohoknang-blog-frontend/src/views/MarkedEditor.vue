<template>
  <el-container class="editor-container">
    <el-main class="editor-main">
      <el-row type="flex" gutter="10" class="editor-row">
        <el-col :span="24">
          <el-form ref="form" :model="form" size="mini" :inline="true">
            <el-form-item label="ID">
              <el-input v-model="form.id" :disabled="true"></el-input
            ></el-form-item>
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
        id: null,
        title: "",
        category: "",
        author: "a9043",
        content: ""
      }
    };
  },
  created() {
    if (this.auth == null) {
      this.$router.push("/");
      return;
    }

    const queryId = this.$route.query.id;
    const id = localStorage.getItem(storage.idKey);

    if (id != null && id !== queryId) {
      this.$router.push(`/editor?id=${id}`);
      return;
    }

    this.getLocalBlog();
    if (queryId != null && queryId !== "") {
      this.form.id = queryId;
    }
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
