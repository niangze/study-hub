<template>
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from './store/user'
import { getProfile } from './api/user'

const userStore = useUserStore()

onMounted(async () => {
  // 页面刷新后：token 仍在 localStorage 但 userInfo 已丢失
  // 自动调用 getProfile() 恢复用户信息（含 role 字段用于权限判断）
  if (userStore.isLoggedIn && !userStore.userInfo) {
    try {
      const data = await getProfile()
      userStore.setUserInfo(data)
    } catch (e) {
      // token 已过期或无效，清除登录状态避免权限混乱
      userStore.clearUser()
    }
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 
               'Helvetica Neue', Arial, 'Noto Sans SC', sans-serif;
  background-color: #f5f7fa;
  color: #303133;
  min-height: 100vh;
}

/* 页面切换过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #909399;
}
</style>
