<template>
  <header class="app-header">
    <div class="left">
      <span class="page-title">{{ pageTitle }}</span>
    </div>

    <div class="right">
      <span class="user-name">{{ nickname }}</span>

      <el-button size="small" @click="logout">
        退出登录
      </el-button>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const userInfo = localStorage.getItem('userInfo')
const nickname = userInfo ? JSON.parse(userInfo).nickname : '管理员'

const pageTitle = computed(() => {
  return route.meta.title || '供水管网漏损控制管理系统'
})

const logout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')

    ElMessage.success('已退出登录')
    router.push('/login')
  } catch {
    // 用户取消退出，不做处理
  }
}
</script>

<style scoped>
.app-header {
  height: 56px;
  padding: 0 20px;
  background: #ffffff;
  border-bottom: 1px solid #e5eaf0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
}

.right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  font-size: 14px;
  color: #606266;
}
</style>