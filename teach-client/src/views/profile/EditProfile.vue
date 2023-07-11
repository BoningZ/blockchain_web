<template>
  <div>
    <h2>修改个人信息</h2>
    <el-form :model="editForm" label-width="100px" class="edit-form">
      <el-form-item label="用户名">
        <el-input v-model="editForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="editForm.password" type="password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input v-model="editForm.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveChanges">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name:'EditProfile',
  data() {
    return {
      editForm: {
        username: '',
        password: '',
        confirmPassword: ''
      }
    };
  },
  methods: {
    saveChanges() {
      if (this.editForm.password !== this.editForm.confirmPassword) {
        this.$message.error('两次输入的密码不一致');
        return;
      }

      // 后端保存处理数据

      // 跳转回去
      this.$router.push({ name: 'UserProf' });
    }
  }
};
</script>

<style scoped>
.edit-form {
  width: 300px;
  margin: 0 auto;
}
</style>
