<template>
  <div class="login-page">
    <div class="login-box">
      <h2>供水管网漏损控制管理系统</h2>

      <el-form>
        <el-form-item>
          <el-input v-model="form.username" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-button type="primary" class="login-button" @click="handleLogin">
          登录
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi } from '@/api/auth'

const router = useRouter()

const form = reactive({
  username: 'admin',
  password: '123456'
})

const handleLogin = async () => {
  try {
    const res = await loginApi(form)

    localStorage.setItem('token', res.token)
    localStorage.setItem('userInfo', JSON.stringify(res))

    ElMessage.success('登录成功')

    router.push('/dashboard')
  } catch (error: any) {
    ElMessage.error(error.message || '登录失败')
  }
}
</script>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  background: #eef3f8;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 380px;
  padding: 32px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 24px;
  font-size: 22px;
  color: #1f2d3d;
}

.login-button {
  width: 100%;
}
</style>