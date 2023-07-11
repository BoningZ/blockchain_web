<template>
  <div>
    <h2>个人信息</h2>
    <div class="info">
      <p><strong>身份：</strong>{{ user.role }}</p>
      <p><strong>用户ID：</strong>{{ user.userId }}</p>
      <p><strong>姓名：</strong>{{ user.name }}</p>
      <p><strong>用户名：</strong>{{ user.username }}</p>
    </div>

    <el-button class="edit-button" @click="goToEditProfile">修改个人信息</el-button>
  </div>
</template>

<script>
export default {
  name:'UserProf',
  data() {
    return {
      user: {
        role: '管理员',
        userId: '12345',
        name: '张三',
        username: 'user123'
      }
    };
  },
  methods: {
    goToEditProfile() {
      this.$router.push({ name: 'EditProfile' });
    }
  }
};
</script>

<style scoped>
.info p {
  margin-bottom: 10px;
}
.edit-button {
  margin-top: 20px;
}
</style>
