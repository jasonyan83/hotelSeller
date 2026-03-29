<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside :width="sidebarWidth" class="admin-sidebar">
      <div class="sidebar-logo">
        <img src="@/assets/vue.svg" alt="logo" class="logo-img" />
        <span v-if="!appStore.sidebarCollapsed" class="logo-text">酒店分销管理</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="appStore.sidebarCollapsed"
        router
        background-color="#001529"
        text-color="rgba(255,255,255,0.65)"
        active-text-color="#ffffff"
        :collapse-transition="false"
      >
        <template v-for="route in menuRoutes" :key="route.path">
          <el-menu-item :index="'/' + route.path">
            <el-icon><component :is="route.meta.icon" /></el-icon>
            <template #title>{{ route.meta.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="admin-header">
        <div class="header-left">
          <el-icon class="toggle-btn" @click="appStore.toggleSidebar">
            <Fold v-if="!appStore.sidebarCollapsed" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentRoute.meta?.title">
              {{ currentRoute.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" style="background: #409eff">管</el-avatar>
              <span class="user-name">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人设置</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { Fold, Expand } from '@element-plus/icons-vue'

const appStore = useAppStore()
const route = useRoute()

const sidebarWidth = computed(() =>
  appStore.sidebarCollapsed ? '64px' : '220px'
)

const activeMenu = computed(() => route.path)
const currentRoute = computed(() => route)

// 只展示有 icon 且非 hidden 的路由
const menuRoutes = computed(() => {
  const layout = route.matched[0]
  if (!layout || !layout.children) return []
  return layout.children.filter(r => r.meta?.icon && !r.meta?.hidden)
})
</script>

<style lang="scss" scoped>
.admin-layout {
  height: 100vh;
  overflow: hidden;
}

.admin-sidebar {
  background-color: #001529;
  transition: width 0.3s;
  overflow-x: hidden;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 0;
  }

  .sidebar-logo {
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);

    .logo-img {
      width: 28px;
      height: 28px;
    }

    .logo-text {
      color: #fff;
      font-size: 16px;
      font-weight: 600;
      white-space: nowrap;
    }
  }

  :deep(.el-menu) {
    border-right: none;

    .el-menu-item {
      height: 48px;
      line-height: 48px;

      &.is-active {
        background-color: #1890ff !important;
        border-radius: 0;
      }

      &:hover {
        background-color: rgba(255, 255, 255, 0.08) !important;
      }
    }
  }
}

.admin-header {
  height: 56px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  z-index: 10;

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .toggle-btn {
      font-size: 20px;
      cursor: pointer;
      color: #606266;

      &:hover {
        color: #409eff;
      }
    }
  }

  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      .user-name {
        font-size: 14px;
        color: #303133;
      }
    }
  }
}

.admin-main {
  background: #f5f7fa;
  overflow-y: auto;
  padding: 24px;
}
</style>
