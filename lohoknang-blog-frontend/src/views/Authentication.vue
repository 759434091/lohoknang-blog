<template>
  <div></div>
</template>

<script>
export default {
  name: "Authentication",
  mounted() {
    const rawAuthentication = prompt("Please enter you authentication", "");
    const authentication = `Basic ${btoa(rawAuthentication)}`;
    this.$http
      .post("/authenticate", null, {
        headers: { Authorization: authentication }
      })
      .then(res => {
        if (res.status !== 202) {
          this.$message.error(res.message);
          return;
        }
        this.$store.commit("setAuth", authentication);
        this.$router.push("/");
      })
      .catch(err => this.$message.error(err.message));
  }
};
</script>

<style scoped></style>
