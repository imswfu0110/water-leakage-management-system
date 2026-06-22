<template>
  <el-sub-menu v-if="visibleChildren.length" :index="item.path || String(item.id)">
    <template #title><el-icon><component :is="icon" /></el-icon><span>{{ item.menuName }}</span></template>
    <MenuNode v-for="child in visibleChildren" :key="child.id" :item="child" />
  </el-sub-menu>
  <el-menu-item v-else-if="item.menuType !== 'BUTTON'" :index="item.path || '/dashboard'">
    <el-icon><component :is="icon" /></el-icon><template #title>{{ item.menuName }}</template>
  </el-menu-item>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import * as Icons from '@element-plus/icons-vue'
import type { MenuItem } from '@/store/app'
const props = defineProps<{ item: MenuItem }>()
const visibleChildren = computed(() => (props.item.children || []).filter(i => i.menuType !== 'BUTTON'))
const icon = computed(() => (Icons as any)[props.item.icon || (visibleChildren.value.length ? 'FolderOpened' : 'Monitor')] || Icons.Monitor)
</script>
