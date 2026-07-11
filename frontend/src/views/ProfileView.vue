<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card v-if="user">
        <template #header>
          <div class="header-flex">
            <h3>个人中心</h3>
            <el-button v-if="!isEditing" link type="primary" @click="isEditing = true">编辑</el-button>
          </div>
        </template>
        <!-- 查看模式 -->
        <div v-if="!isEditing">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="积分">{{ user.points }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}</el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ user.createdAt }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 编辑模式 -->
        <div v-else>
          <el-form :model="editForm" label-width="80px">
            <el-form-item label="邮箱">
              <el-input v-model="editForm.email" placeholder="输入邮箱" />
            </el-form-item>
            <el-form-item label="头像URL">
              <el-input v-model="editForm.avatar" placeholder="输入头像链接" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存</el-button>
              <el-button @click="isEditing = false">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
      <el-empty v-else description="加载中..." />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProfile, updateProfile } from '../api/user'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AddHeader.vue'

const userStore = useUserStore()
const user = ref(null)
const isEditing = ref(false)
const editForm = ref({ email: '', avatar: '' })

onMounted(async () => {
  await fetchProfile()
})

const fetchProfile = async () => {
  try {
    const data = await getProfile()
    user.value = data
    userStore.setUserInfo(data)
    editForm.value = { email: data.email || '', avatar: data.avatar || '' }
  } catch (e) {
    console.error('获取用户信息失败:', e)
  }
}

const handleSave = async () => {
  try {
    await updateProfile({
      email: editForm.value.email || undefined,
      avatar: editForm.value.avatar || undefined
    })
    ElMessage.success('保存成功')
    isEditing.value = false
    await fetchProfile()
  } catch (e) {
    ElMessage.error('保存失败')
    console.error(e)
  }
}
</script>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}
.header-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
