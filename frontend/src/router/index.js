import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', component: () => import('../views/HomeView.vue') },
  { path: '/login', component: () => import('../views/LoginView.vue') },
  { path: '/register', component: () => import('../views/RegisterView.vue') },
  { path: '/questions', component: () => import('../views/QuestionListView.vue') },
  { path: '/question/:id', component: () => import('../views/QuestionDetailView.vue') },
  {
    path: '/ask',
    component: () => import('../views/AskView.vue'),
    meta: { requireAuth: true }
  },
  {
    path: '/profile',
    component: () => import('../views/ProfileView.vue'),
    meta: { requireAuth: true }
  },
  {
    path: '/admin',
    component: () => import('../views/AdminView.vue'),
    meta: { requireAuth: true, requireAdmin: true }
  },
  { path: '/resources', component: () => import('../views/ResourceListView.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // 需要登录的页面
  if (to.meta?.requireAuth) {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      next('/login')
      return
    }

    // 需要管理员权限 — 实际拦截
    if (to.meta?.requireAdmin) {
      if (!userStore.userInfo) {
        // userInfo未加载（页面刷新后），尝试加载或拒绝
        ElMessage.warning('权限验证中，请稍后重试')
        next('/home')
        return
      }
      if (!userStore.isAdmin) {
        ElMessage.warning('权限不足，需要管理员身份')
        next('/home')
        return
      }
    }
  }

  next()
})

export default router
