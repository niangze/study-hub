const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const token = await login(form)
    userStore.setToken(token)
    // 登录成功后必须获取用户信息（含role字段用于权限判断）
    const userInfo = await getProfile()
    userStore.setUserInfo(userInfo)
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (e) {
    // 登录失败（含获取profile失败）时清除所有状态
    userStore.clearUser()
    ElMessage.error(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}
