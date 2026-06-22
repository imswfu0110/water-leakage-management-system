<template>
  <div class="app-layout">
    <AppHeader />
    <div class="workspace">
      <Sidebar />
      <div class="main-container">
        <div class="quick-bar"><b>常用页面：</b><span>小区最新数据查询</span><span>区域管理</span><span>测点基础资料</span><span>分区日数据</span><span>NB小表资料管理</span></div>
        <div class="tabs"><div v-for="tab in tabs" :key="tab.path" :class="['tab', {active: tab.path === route.path}]" @click="router.push(tab.path)">{{ tab.title }}<el-icon v-if="tab.path !== '/dashboard'" @click.stop="closeTab(tab.path)"><Close /></el-icon></div></div>
        <main class="content"><router-view /></main>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Close } from '@element-plus/icons-vue'
import Sidebar from './components/Sidebar.vue'
import AppHeader from './components/AppHeader.vue'
const route=useRoute(), router=useRouter()
const tabs=reactive([{path:'/dashboard',title:'首页'}])
watch(()=>route.path,()=>{ if(route.path==='/login')return; if(!tabs.some(t=>t.path===route.path))tabs.push({path:route.path,title:String(route.meta.title||'页面')}) },{immediate:true})
const closeTab=(path:string)=>{ const i=tabs.findIndex(t=>t.path===path); tabs.splice(i,1); if(route.path===path)router.push(tabs[Math.max(0,i-1)].path) }
</script>

<style scoped>
.app-layout { width:100vw; height:100vh; display:flex; flex-direction:column; background:#edf2f7; overflow:hidden; }
.workspace { flex:1; display:flex; min-height:0; }.main-container{flex:1;display:flex;flex-direction:column;min-width:0;}
.quick-bar{height:54px;margin:12px 16px 0;padding:0 25px;border-radius:13px;background:#fff;display:flex;align-items:center;gap:24px;white-space:nowrap;overflow:hidden;color:#2691ef}.quick-bar b{color:#526476}.quick-bar span{cursor:pointer}
.tabs{height:42px;padding:8px 16px 0;display:flex;gap:6px}.tab{height:34px;padding:0 15px;border-radius:7px 7px 0 0;background:#dfe8f0;color:#587087;display:flex;align-items:center;gap:8px;cursor:pointer;font-size:13px}.tab.active{background:#fff;color:#2389e6;font-weight:600}.tab .el-icon{font-size:12px}
.content{flex:1;padding:0 16px 16px;overflow:auto;}
</style>
