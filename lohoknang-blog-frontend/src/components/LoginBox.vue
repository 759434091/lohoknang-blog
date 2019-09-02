<template>
  <el-dialog
    title="后台登录"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form ref="form" :model="form">
      <el-form-item label="用户名" label-position="top">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" label-position="top">
        <el-input
          v-model="form.password"
          placeholder="请输入密码"
          type="password"
        ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" @click="login">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "LoginBox",
  props: {
    visible: Boolean
  },
  data() {
    return {
      form: {
        username: "",
        password: ""
      }
    };
  },
  methods: {
    cancel() {
      this.$emit("close");
      this.$refs["form"].resetFields();
    },
    login() {
      const usernamePassword = `${this.form.username}:${this.form.password}`;
      const auth = Buffer.from(usernamePassword).toString("base64");
      this.$http
        .post("/authenticate", null, {
          headers: {
            Authorization: `Basic ${auth}`
          }
        })
        .then(() => {
          this.$store.commit("setAuth", `Basic ${auth}`);
          this.$router.push("/");
        })
        .catch(err => {
          this.$message.error(err);
        });
    }
  }
};
</script>

<style scoped></style>
