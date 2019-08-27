<template>
  <div class="contact-container">
    <el-container class="contact-main-container">
      <el-header>
        <h1 class="contact-brand">联系我</h1>
      </el-header>
      <el-main>
        <el-form :disabled="true" ref="form" :model="form" :rules="rules" label-position="top">
          <el-form-item label="称呼" prop="name">
            <el-input
              v-model="form.name"
              placeholder="称呼"
              maxlength="10"
            ></el-input>
          </el-form-item>
          <el-form-item label="联系方式" prop="contact">
            <el-input
              v-model="form.contact"
              placeholder="联系方式"
              maxlength="50"
            ></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :autosize="{ minRows: 10 }"
              placeholder="内容"
              maxlength="1000"
              show-word-limit
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submit">
              提交
            </el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Contact",
  data() {
    return {
      form: {
        name: "",
        contact: "",
        content: ""
      },
      rules: {
        name: [
          { required: true, message: "请输入你的称呼", trigger: "blur" },
          { min: 3, max: 10, message: "长度在 3 到 10 个字符", trigger: "blur" }
        ],
        contact: [
          { required: true, message: "请输入联系方式", trigger: "blur" },
          { min: 5, max: 50, message: "长度在 5 到 50 个字符", trigger: "blur" }
        ],
        content: [
          { required: true, message: "请输入联系内容", trigger: "blur" },
          {
            min: 10,
            max: 1000,
            message: "长度在 10 到 1000 个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return;
        }
        this.$http.post();
      });
    }
  }
};
</script>

<style scoped>
.contact-container {
  width: 100vw;
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.contact-main-container {
  display: flex;
  width: 768px;
  margin: 20px auto;
}

.contact-brand {
  font-weight: normal;
  font-size: 20px;
  color: #606266;
}
</style>
