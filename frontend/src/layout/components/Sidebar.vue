<template>
  <aside class="sidebar">
    <div class="logo">
      <div class="logo-title">智慧水务</div>
      <div class="logo-subtitle">漏损控制管理系统</div>
    </div>

    <el-menu
      router
      :default-active="route.path"
      background-color="#0f2a44"
      text-color="#b8c7d6"
      active-text-color="#ffffff"
      class="menu"
    >
      <el-menu-item index="/dashboard">
        <span>首页</span>
      </el-menu-item>

      <el-sub-menu index="/system">
        <template #title>
          <span>系统管理</span>
        </template>

        <el-menu-item index="/system/user">用户管理</el-menu-item>
        <el-menu-item index="/system/role">角色管理</el-menu-item>
        <el-menu-item index="/system/menu">菜单管理</el-menu-item>
        <el-menu-item index="/system/config">系统配置</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<style scoped>
.sidebar {
  width: 220px;
  height: 100vh;
  background: #0f2a44;
  color: #ffffff;
  flex-shrink: 0;
}

.logo {
  height: 72px;
  padding: 16px 18px;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.logo-title {
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 2px;
}

.logo-subtitle {
  margin-top: 6px;
  font-size: 12px;
  color: #9fb3c8;
}

.menu {
  border-right: none;
}
</style>