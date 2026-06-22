<template>
  <div class="app-layout">
    <Sidebar />

    <div class="main-container">
      <AppHeader />

      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import Sidebar from './components/Sidebar.vue'
import AppHeader from './components/AppHeader.vue'
</script>

<style scoped>
.app-layout {
  width: 100vw;
  height: 100vh;
  display: flex;
  background: #f3f6fa;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.content {
  flex: 1;
  padding: 20px;
  overflow: auto;
}
</style>