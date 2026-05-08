<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>供水管网漏损控制管理系统</h2>

      <el-form>
        <el-form-item>
          <el-input placeholder="请输入账号" />
        </el-form-item>

        <el-form-item>
          <el-input placeholder="请输入密码" type="password" />
        </el-form-item>

        <el-form-item>
          <el-input placeholder="请输入验证码" />
        </el-form-item>

        <el-button type="primary" class="login-button">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.login-page {
  width: 100%;
  height: 100vh;
  background: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 380px;
}

.login-card h2 {
  text-align: center;
  margin-bottom: 24px;
}

.login-button {
  width: 100%;
}
</style>