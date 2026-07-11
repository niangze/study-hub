import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import HomeView from '../views/HomeView.vue'
import QuestionListView from '../views/QuestionListView.vue'
import QuestionDetailView from '../views/QuestionDetailView.vue'
import AskView from '../views/AskView.vue'
import ProfileView from '../views/ProfileView.vue'
import AdminView from '../views/AdminView.vue'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/login', name: 'Login', component: LoginView, meta: { public: true } },
  { path: '/register', name: 'Register', component: RegisterView, meta: { public: true } },
  { path: '/home', name: 'Home', component: HomeView },
  { path: '/questions', name: 'QuestionList', component: QuestionListView },
  { path: '/question/:id', name: 'QuestionDetail', component: QuestionDetailView },
  { path: '/ask', name: 'Ask', component: AskView, meta: { requireAuth: true } },
  { path: '/profile', name: 'Profile', component: ProfileView, meta: { requireAuth: true } },
  { path: '/admin', name: 'Admin', component: AdminView, meta: { requireAuth: true, requireAdmin: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // 公开页面直接放行
  if (to.meta?.public) {
    // 已登录用户访问登录/注册页，重定向到首页
    if (userStore.isLoggedIn && (to.path === '/login' || to.path === '/register')) {
      next('/home')
      return
    }
    next()
    return
  }

  // 需要登录的页面
  if (to.meta?.requireAuth) {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      next('/login')
      return
    }
    // 需要管理员权限
    if (to.meta?.requireAdmin) {
      // 注意：需要等用户信息加载后才能判断
      // 这里先放行，页面内部再做权限控制
    }
  }

  next()
})

export default router
