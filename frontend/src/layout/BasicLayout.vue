<template>
  <el-container class="layout">
    <el-aside width="220px" class="layout-aside">
      <div class="logo">
        <div class="logo-title">供水管网漏损控制管理系统</div>
      </div>

      <el-menu
        router
        default-active="/dashboard"
        class="layout-menu"
        background-color="#001529"
        text-color="#ffffff"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <span>系统首页</span>
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
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div>供水管网漏损控制管理系统</div>
        <div class="user-info">admin</div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
</script>

<style scoped>
.layout {
  width: 100%;
  height: 100vh;
}

.layout-aside {
  background: #001529;
  color: #fff;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 10px;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

.logo-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.logo-title {
  font-size: 14px;
  line-height: 1.4;
}

.layout-menu {
  border-right: none;
}

.layout-header {
  height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #ebeef5;
  box-sizing: border-box;
  font-weight: 500;
}

.user-info {
  color: #606266;
}

.layout-main {
  background: #f5f7fa;
  padding: 20px;
}
</style>