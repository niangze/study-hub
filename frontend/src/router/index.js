import { createRouter, createWebHistory } from 'vue-router'
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
  { path: '/login', name: 'Login', component: LoginView },
  { path: '/register', name: 'Register', component: RegisterView },
  { path: '/home', name: 'Home', component: HomeView },
  { path: '/questions', name: 'QuestionList', component: QuestionListView },
  { path: '/question/:id', name: 'QuestionDetail', component: QuestionDetailView },
  { path: '/ask', name: 'Ask', component: AskView },
  { path: '/profile', name: 'Profile', component: ProfileView },
  { path: '/admin', name: 'Admin', component: AdminView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
