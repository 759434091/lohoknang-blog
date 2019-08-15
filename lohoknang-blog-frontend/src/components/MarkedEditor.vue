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
          <div class="editor-preview-marked" v-html="markedStr"></div>
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

export default {
  name: "MarkedEditor",
  computed: {
    markedStr() {
      return marked(this.form.content);
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
