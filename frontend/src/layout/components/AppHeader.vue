<template>
  <header class="app-header">
    <div class="brand">
      <div class="brand-mark"><img v-if="appStore.systemLogo" :src="appStore.systemLogo" alt="系统 Logo" /><span v-else>≋</span></div>
      <div><div class="brand-name">{{ appStore.systemName }}</div><div class="brand-sub">象山水务第一自来水公司</div></div>
    </div>
    <nav class="top-nav">
      <div class="nav-item active"><el-icon><TrendCharts /></el-icon><span>监控中心</span></div>
      <div class="nav-item"><el-icon><DataLine /></el-icon><span>数据中心</span></div>
      <div class="nav-item"><el-icon><Warning /></el-icon><span>事件中心</span></div>
      <div class="nav-item"><el-icon><Tickets /></el-icon><span>工单中心</span></div>
      <div class="nav-item"><el-icon><Setting /></el-icon><span>云平台管理</span></div>
    </nav>
    <el-dropdown @command="handleCommand">
      <div class="account"><el-avatar :size="32">{{ (appStore.user?.nickname || '管').slice(0,1) }}</el-avatar><span>{{ appStore.user?.nickname || '管理员' }}</span><el-icon><ArrowDown /></el-icon></div>
      <template #dropdown><el-dropdown-menu><el-dropdown-item command="logout">退出登录</el-dropdown-item></el-dropdown-menu></template>
    </el-dropdown>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { TrendCharts, DataLine, Warning, Tickets, Setting, ArrowDown } from '@element-plus/icons-vue'
import { appStore } from '@/store/app'
import { logoutApi } from '@/api/auth'
const router = useRouter()
const handleCommand = async (command: string) => {
  if (command !== 'logout') return
  await ElMessageBox.confirm('确定要退出登录吗？', '提示')
  await logoutApi().catch(() => undefined)
  localStorage.clear(); appStore.ready = false; router.replace('/login')
}
</script>

<style scoped>
.app-header { height: 72px; padding: 0 18px 0 16px; color: #fff; display: flex; align-items: center; background: linear-gradient(90deg,#1889e9,#0874cb); box-shadow: 0 2px 10px rgba(9,82,143,.18); }
.brand { min-width: 500px; display: flex; align-items: center; gap: 12px; }
.brand-mark { width: 54px; height: 54px; border-radius: 50%; background: #fff; color: #0782dd; display: grid; place-items: center; font-size: 42px; font-weight: 900; overflow: hidden; }
.brand-mark img { width: 100%; height: 100%; object-fit: contain; }
.brand-name { font-size: 21px; font-weight: 800; letter-spacing: .5px; white-space: nowrap; }
.brand-sub { font-size: 12px; margin-top: 5px; opacity: .86; }
.top-nav { flex: 1; height: 100%; display: flex; justify-content: center; }
.nav-item { min-width: 86px; padding: 10px 15px 8px; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; border-bottom: 4px solid transparent; font-size: 14px; opacity: .88; }
.nav-item .el-icon { font-size: 21px; }.nav-item.active { background: rgba(0,73,146,.18); border-color: #ffb000; opacity: 1; }
.account { color: #fff; display: flex; align-items: center; gap: 8px; cursor: pointer; white-space: nowrap; }
@media(max-width:1100px){.brand{min-width:380px}.brand-name{font-size:17px}.nav-item{min-width:65px;padding-inline:7px}.nav-item span{font-size:12px}}
</style>
