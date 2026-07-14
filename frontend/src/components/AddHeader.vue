<template>
  <el-header class="app-header">
    <div class="logo" @click="$router.push('/home')">
      <el-icon size="24"><ChatDotRound /></el-icon>
      <span>StudyHub</span>
    </div>
    <div class="nav">
      <el-button link @click="$router.push('/home')">首页</el-button>
      <el-button link @click="$router.push('/questions')">问答</el-button>
      <el-button link @click="$router.push('/resources')">资源</el-button>
      <el-button link @click="$router.push('/profile')">个人中心</el-button>
      <el-button v-if="userStore.isAdmin" link type="warning" @click="$router.push('/admin')">后台管理</el-button>
      <el-button v-if="userStore.isLoggedIn" type="danger" link @click="handleLogout">退出</el-button>
      <el-button v-else link @click="$router.push('/login')">登录</el-button>
    </div>
  </el-header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.clearUser()
  router.push('/login')
}
</script>

<style scoped>
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  height: 60px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}
.nav {
  display: flex;
  gap: 10px;
}
</style>
