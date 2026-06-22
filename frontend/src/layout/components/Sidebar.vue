<template>
  <aside class="sidebar">
    <div class="sidebar-heading"><el-icon><Menu /></el-icon><span>功能导航</span></div>
    <el-menu router :default-active="route.path" class="menu">
      <MenuNode v-for="item in appStore.menus" :key="item.id" :item="item" />
    </el-menu>
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { Menu } from '@element-plus/icons-vue'
import { appStore } from '@/store/app'
import MenuNode from './MenuNode.vue'
const route = useRoute()
</script>

<style scoped>
.sidebar { width: 178px; height: 100%; flex-shrink: 0; background: #fff; border-right: 1px solid #e6edf4; overflow: auto; }
.sidebar-heading { height: 52px; padding: 0 20px; display: flex; align-items: center; gap: 8px; color: #2389e6; font-weight: 700; border-bottom: 1px solid #edf2f7; }
.menu { border-right: 0; }
:deep(.el-menu-item), :deep(.el-sub-menu__title) { height: 47px; color: #31506d; }
:deep(.el-menu-item.is-active) { color: #258ff0; background: #e9f5ff; border-right: 3px solid #258ff0; }
</style>
