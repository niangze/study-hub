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
}

export default router
