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
            <el-descriptions-item label="学号">{{ user.studentId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="积分">{{ user.points }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}</el-descriptions-item>
            <el-descriptions-item label="关注">
              <el-button link type="primary" @click="showFollowing = true">{{ followCounts.following }} 关注</el-button>
              |
              <el-button link type="primary">{{ followCounts.followers }} 粉丝</el-button>
            </el-descriptions-item>
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

      <!-- 关注列表弹窗 -->
      <el-dialog v-model="showFollowing" title="我的关注" width="400px">
        <el-empty v-if="followingList.length === 0" description="还没有关注任何人" />
        <div v-for="u in followingList" :key="u.id" class="follow-item">
          <el-avatar :size="40" :src="u.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" />
          <div class="follow-info">
            <div class="follow-name">{{ u.username }}</div>
            <div class="follow-role">{{ u.role === 'ADMIN' ? '管理员' : '普通用户' }}</div>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProfile, updateProfile } from '../api/user'
import { getFollowCounts, getFollowingList } from '../api/follow'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AddHeader.vue'

const userStore = useUserStore()
const user = ref(null)
const isEditing = ref(false)
const showFollowing = ref(false)
const editForm = ref({ email: '', avatar: '' })
const followCounts = ref({ followers: 0, following: 0 })
const followingList = ref([])

onMounted(async () => {
  await fetchProfile()
  await fetchFollowData()
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

const fetchFollowData = async () => {
  try {
    if (user.value) {
      const counts = await getFollowCounts(user.value.id)
      followCounts.value = counts || { followers: 0, following: 0 }
    }
    const list = await getFollowingList()
    followingList.value = list || []
  } catch (e) {
    console.error('获取关注数据失败:', e)
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
.follow-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}
.follow-info {
  flex: 1;
}
.follow-name {
  font-weight: bold;
  color: #303133;
}
.follow-role {
  font-size: 12px;
  color: #999;
}
</style>
