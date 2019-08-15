<template>
  <el-container>
    <el-row type="flex">
      <el-col :span="12">
        <el-input
          type="textarea"
          :autosize="{ minRows: 100 }"
          placeholder="请输入内容"
          v-model="raw"
        >
        </el-input>
      </el-col>
      <el-col :span="12">
        <div v-html="markedStr"></div>
      </el-col>
    </el-row>
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
      return marked(this.raw);
    }
  },
  data() {
    return {
      raw: ""
    };
  }
};
</script>

<style scoped></style>
