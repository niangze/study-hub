onMounted(async () => {
  await fetchProfile()
  await fetchFollowData()
})