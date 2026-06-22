<template>
  <div class="dashboard-page">
    <div class="welcome-card">
      <div>
        <h2>供水管网漏损控制管理系统</h2>
        <p>欢迎回来，{{ nickname }}。当前系统用于支撑管网漏损监测、工单处置、数据分析与系统管理。</p>
      </div>
    </div>

    <div class="stat-grid">
      <el-card class="stat-card">
        <div class="stat-title">管网监测点</div>
        <div class="stat-value">0</div>
        <div class="stat-desc">待接入实时监测数据</div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-title">漏损预警</div>
        <div class="stat-value">0</div>
        <div class="stat-desc">待开发预警规则</div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-title">待处理工单</div>
        <div class="stat-value">0</div>
        <div class="stat-desc">待接入工单模块</div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-title">本月漏损率</div>
        <div class="stat-value">--</div>
        <div class="stat-desc">待接入统计数据</div>
      </el-card>
    </div>

    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <span>系统建设进度</span>
        </div>
      </template>

      <el-timeline>
        <el-timeline-item timestamp="已完成" type="success">
          后端基础规范、数据库基础表、登录认证接口
        </el-timeline-item>

        <el-timeline-item timestamp="当前步骤" type="primary">
          系统 Layout 与首页开发
        </el-timeline-item>

        <el-timeline-item timestamp="下一步">
          菜单管理、用户管理、角色权限模块
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup lang="ts">
const userInfo = localStorage.getItem('userInfo')
const nickname = userInfo ? JSON.parse(userInfo).nickname : '管理员'
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.welcome-card {
  padding: 24px;
  background: linear-gradient(135deg, #1f6fb2, #0f2a44);
  border-radius: 12px;
  color: #ffffff;
}

.welcome-card h2 {
  margin: 0 0 10px;
  font-size: 24px;
}

.welcome-card p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  border-radius: 10px;
}

.stat-title {
  font-size: 14px;
  color: #606266;
}

.stat-value {
  margin-top: 12px;
  font-size: 30px;
  font-weight: 700;
  color: #1f2d3d;
}

.stat-desc {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}

.section-card {
  border-radius: 10px;
}

.card-header {
  font-weight: 600;
}

@media (max-width: 1200px) {
  .stat-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>