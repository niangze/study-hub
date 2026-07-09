const { createApp, ref, reactive, onMounted } = Vue;
const { ElMessage, ElMessageBox } = ElementPlus;
const icons = ElementPlusIconsVue;

createApp({
  setup() {
    const isLoggedIn = ref(false);
    const showLogin = ref(true);
    const loading = ref(false);
    const submitting = ref(false);
    const currentPage = ref('home');
    const currentPageNum = ref(1);
    const pageSize = ref(10);
    const questionTotal = ref(0);
    const searchKeyword = ref('');
    const filterStatus = ref('');
    const resourceKeyword = ref('');
    const answerContent = ref('');
    const editProfileVisible = ref(false);

    const currentUser = reactive({ id: 1, username: 'demo', email: 'demo@xmu.edu.cn', avatar: '', points: 100, role: 'USER' });
    const loginForm = reactive({ username: '', password: '' });
    const regForm = reactive({ username: '', password: '', email: '' });
    const editForm = reactive({ username: '', email: '', avatar: '' });

    const hotQuestions = ref([]);
    const latestQuestions = ref([]);
    const questionList = ref([]);
    const currentQuestion = ref(null);
    const answers = ref([]);
    const categories = ref([]);
    const resources = ref([]);
    const allQuestions = ref([]);
    const myQuestions = ref([]);
    const myAnswers = ref([]);
    const stats = reactive({ userCount: 128, questionCount: 56, answerCount: 234, resourceCount: 89 });
    const askForm = reactive({ title: '', content: '', categoryId: null, pointsReward: 0 });

    const mockCategories = [
      { id: 1, name: 'Java', description: 'Java编程相关问题' },
      { id: 2, name: '数据库', description: 'MySQL、Redis等数据库问题' },
      { id: 3, name: '前端', description: 'Vue、React、HTML/CSS/JS' },
      { id: 4, name: '算法', description: '数据结构与算法' },
      { id: 5, name: 'Spring', description: 'Spring Boot、Spring Cloud' },
      { id: 6, name: '运维', description: 'Linux、Docker、K8s' },
      { id: 7, name: '其他', description: '其他课程相关问题' }
    ];

    const mockQuestions = [
      { id: 1, title: 'Java中接口和抽象类的区别是什么？', content: '面试经常被问到，想搞清楚两者的本质区别和适用场景。什么时候应该用接口，什么时候应该用抽象类？Java 8之后有default方法，两者界限是不是更模糊了？', categoryId: 1, categoryName: 'Java', userId: 3, username: 'zhangsan', userAvatar: '', pointsReward: 20, status: 'CLOSED', answerCount: 2, viewCount: 156, createdAt: '2025-07-01 10:30:00' },
      { id: 2, title: 'MySQL索引失效的常见原因有哪些？', content: '项目里遇到了慢查询问题，怀疑是索引失效导致的。请问有哪些常见场景会导致索引失效？', categoryId: 2, categoryName: '数据库', userId: 4, username: 'lisi', userAvatar: '', pointsReward: 15, status: 'OPEN', answerCount: 1, viewCount: 89, createdAt: '2025-07-02 14:00:00' },
      { id: 3, title: 'Vue3的Composition API和Options API怎么选？', content: '新项目用Vue3，在纠结用哪种写法。团队里有人习惯Options API，但Composition API看起来更灵活。', categoryId: 3, categoryName: '前端', userId: 3, username: 'zhangsan', userAvatar: '', pointsReward: 10, status: 'CLOSED', answerCount: 3, viewCount: 234, createdAt: '2025-07-02 16:30:00' },
      { id: 4, title: 'Spring Boot如何实现JWT认证？', content: '正在做课程项目，需要实现登录认证功能。看了很多教程但还是不太明白JWT的工作原理。', categoryId: 5, categoryName: 'Spring', userId: 5, username: 'wangwu', userAvatar: '', pointsReward: 25, status: 'OPEN', answerCount: 0, viewCount: 67, createdAt: '2025-07-03 09:15:00' },
      { id: 5, title: '快速排序的时间复杂度怎么分析？', content: '数据结构课要考排序算法，不太理解时间复杂度的推导。为什么快速排序平均是O(nlogn)，最坏是O(n^2)？', categoryId: 4, categoryName: '算法', userId: 4, username: 'lisi', userAvatar: '', pointsReward: 10, status: 'OPEN', answerCount: 1, viewCount: 45, createdAt: '2025-07-03 11:20:00' },
      { id: 6, title: 'Redis和MySQL如何保证数据一致性？', content: '项目中同时用了Redis做缓存和MySQL做持久化，经常出现数据不一致的情况。', categoryId: 2, categoryName: '数据库', userId: 5, username: 'wangwu', userAvatar: '', pointsReward: 30, status: 'OPEN', answerCount: 0, viewCount: 78, createdAt: '2025-07-04 08:00:00' },
      { id: 7, title: 'Docker部署Spring Boot应用的最佳实践', content: '准备把课程项目部署到服务器上，想用Docker容器化。请问有哪些注意事项？', categoryId: 6, categoryName: '运维', userId: 3, username: 'zhangsan', userAvatar: '', pointsReward: 15, status: 'OPEN', answerCount: 0, viewCount: 34, createdAt: '2025-07-04 13:45:00' },
      { id: 8, title: '如何准备Java后端开发实习面试？', content: '大二学生，想找Java后端开发的实习。现在学了Java基础、Spring Boot、MySQL，还需要学什么？', categoryId: 1, categoryName: 'Java', userId: 4, username: 'lisi', userAvatar: '', pointsReward: 20, status: 'OPEN', answerCount: 2, viewCount: 189, createdAt: '2025-07-05 10:00:00' }
    ];

    const mockAnswers = {
      1: [
        { id: 1, content: '核心区别在于：接口只能定义方法签名不能有实现，抽象类可以有部分实现。一个类可以实现多个接口但只能继承一个抽象类。\n\n建议优先用接口定义行为契约，用抽象类提取公共代码。Java 8之后接口可以有default方法，但接口仍然不能持有状态（实例变量），所以还是遵循"面向接口编程"原则。', questionId: 1, userId: 5, username: 'wangwu', userAvatar: '', isAccepted: true, createdAt: '2025-07-01 12:00:00' },
        { id: 2, content: '补充一点：接口中的default方法主要是为了向后兼容，比如Java 8在Collection接口中加了很多default方法。实际项目中如果一个类需要多个不同来源的行为，用接口组合；如果需要复用代码模板，用抽象类。', questionId: 1, userId: 1, username: 'demo', userAvatar: '', isAccepted: false, createdAt: '2025-07-01 14:30:00' }
      ],
      2: [{ id: 3, content: '常见原因：1.对索引列使用函数 2.隐式类型转换 3.OR条件中部分列无索引 4.LIKE以%开头 5.数据量太小全表扫描更快 6.!=或<>操作符\n\n排查方法：用EXPLAIN分析执行计划，关注type列（ALL表示全表扫描）、key列（实际使用的索引）、rows列（扫描行数）。', questionId: 2, userId: 5, username: 'wangwu', userAvatar: '', isAccepted: false, createdAt: '2025-07-02 16:00:00' }],
      3: [{ id: 4, content: 'Composition API更适合逻辑复用和TypeScript，Options API更适合小项目或初学者。新项目推荐Composition API，配合script setup语法糖代码更简洁。\n\n可以混用，但不建议。建议团队统一规范，新项目统一用Composition API。', questionId: 3, userId: 1, username: 'demo', userAvatar: '', isAccepted: true, createdAt: '2025-07-02 18:00:00' }],
      5: [{ id: 5, content: '快速排序平均O(n log n)是因为每次partition大致将数组分成两半，递归深度是log n，每层操作是O(n)。最坏O(n^2)发生在每次选的基准都是最大或最小值时（比如数组已有序）。\n\n可以用随机化基准或三数取中法来避免最坏情况。实际应用中很少出现最坏情况。', questionId: 5, userId: 5, username: 'wangwu', userAvatar: '', isAccepted: true, createdAt: '2025-07-03 13:00:00' }],
      8: [
        { id: 6, content: '准备实习的话建议：1.基础要扎实：Java集合源码、JUC并发包、JVM内存模型 2.Spring生态：Spring Boot自动配置原理、AOP实现 3.数据库：索引原理、事务隔离级别、SQL优化 4.LeetCode至少刷100道，重点链表、树、动态规划 5.准备一个能演示的项目，有GitHub链接', questionId: 8, userId: 5, username: 'wangwu', userAvatar: '', isAccepted: true, createdAt: '2025-07-05 12:00:00' },
        { id: 7, content: '另外建议关注一些技术博客和公众号，了解行业动态。面试时不仅要会做题，还要能讲清楚思路。实习面试一般不会太难，重点考察基础和潜力。', questionId: 8, userId: 3, username: 'zhangsan', userAvatar: '', isAccepted: false, createdAt: '2025-07-05 15:30:00' }
      ]
    };

    const mockResources = [
      { id: 1, title: 'Java核心技术笔记.pdf', description: '包含面向对象、集合框架、IO流、多线程、JVM等核心知识点，共120页', category: 'Java', userId: 3, username: 'zhangsan', downloadCount: 56, pointsCost: 5, createdAt: '2025-07-01' },
      { id: 2, title: 'MySQL优化手册.md', description: '索引优化、查询优化、慢查询分析、事务调优，实战案例丰富', category: '数据库', userId: 5, username: 'wangwu', downloadCount: 32, pointsCost: 3, createdAt: '2025-07-02' },
      { id: 3, title: 'Vue3快速入门.pdf', description: 'Composition API、Pinia状态管理、Vue Router 4、TypeScript整合', category: '前端', userId: 3, username: 'zhangsan', downloadCount: 89, pointsCost: 5, createdAt: '2025-07-02' },
      { id: 4, title: '算法面试题汇总.pdf', description: 'LeetCode高频题分类整理：数组、链表、树、动态规划、回溯', category: '算法', userId: 4, username: 'lisi', downloadCount: 120, pointsCost: 10, createdAt: '2025-06-28' },
      { id: 5, title: 'Spring Boot实战项目源码.zip', description: '完整的Spring Boot + Vue3前后端分离项目，含JWT认证、CRUD、分页', category: 'Spring', userId: 5, username: 'wangwu', downloadCount: 45, pointsCost: 8, createdAt: '2025-07-03' },
      { id: 6, title: 'Linux常用命令速查.pdf', description: '文件操作、进程管理、网络工具、Shell脚本，适合初学者', category: '运维', userId: 4, username: 'lisi', downloadCount: 67, pointsCost: 2, createdAt: '2025-07-04' }
    ];

    const navigate = (page) => { currentPage.value = page; window.scrollTo(0,0); };

    const handleUserCommand = (cmd) => {
      if (cmd === 'logout') { localStorage.removeItem('token'); isLoggedIn.value = false; showLogin.value = true; ElMessage.success('已退出登录'); }
      else if (cmd === 'profile') navigate('profile');
      else if (cmd === 'ask') navigate('ask');
    };

    const doLogin = async () => {
      if (!loginForm.username || !loginForm.password) { ElMessage.warning('请填写用户名和密码'); return; }
      loading.value = true;
      try { await new Promise(r => setTimeout(r, 500)); localStorage.setItem('token', 'demo-token-' + Date.now()); currentUser.username = loginForm.username || 'demo'; isLoggedIn.value = true; currentPage.value = 'home'; loadAllData(); ElMessage.success('登录成功'); } finally { loading.value = false; }
    };

    const doRegister = async () => {
      if (!regForm.username || !regForm.password) { ElMessage.warning('请填写用户名和密码'); return; }
      if (regForm.username.length < 3) { ElMessage.warning('用户名至少3个字符'); return; }
      if (regForm.password.length < 6) { ElMessage.warning('密码至少6个字符'); return; }
      loading.value = true;
      try { await new Promise(r => setTimeout(r, 500)); localStorage.setItem('token', 'demo-token-' + Date.now()); currentUser.username = regForm.username; isLoggedIn.value = true; currentPage.value = 'home'; loadAllData(); ElMessage.success('注册成功'); } finally { loading.value = false; }
    };

    const loadAllData = () => {
      categories.value = mockCategories;
      hotQuestions.value = [...mockQuestions].sort((a,b) => (b.viewCount+b.answerCount*10) - (a.viewCount+a.answerCount*10)).slice(0,5);
      latestQuestions.value = [...mockQuestions].sort((a,b) => new Date(b.createdAt) - new Date(a.createdAt)).slice(0,5);
      questionList.value = mockQuestions;
      questionTotal.value = mockQuestions.length;
      allQuestions.value = mockQuestions;
      resources.value = mockResources;
      myQuestions.value = mockQuestions.filter(q => q.userId === currentUser.id);
      myAnswers.value = [];
    };

    const searchQuestions = () => {
      let filtered = mockQuestions;
      if (searchKeyword.value) filtered = filtered.filter(q => q.title.includes(searchKeyword.value) || q.content.includes(searchKeyword.value));
      if (filterStatus.value) filtered = filtered.filter(q => q.status === filterStatus.value);
      questionList.value = filtered;
    };

    const viewQuestion = (id) => {
      const q = mockQuestions.find(x => x.id === id);
      if (q) { currentQuestion.value = q; answers.value = mockAnswers[id] || []; currentPage.value = 'detail'; window.scrollTo(0,0); }
    };

    const submitAnswer = async () => {
      if (!answerContent.value.trim()) { ElMessage.warning('请输入回答内容'); return; }
      submitting.value = true;
      try { await new Promise(r => setTimeout(r, 300)); answers.value.push({ id: Date.now(), content: answerContent.value, questionId: currentQuestion.value.id, userId: currentUser.id, username: currentUser.username, userAvatar: currentUser.avatar, isAccepted: false, createdAt: new Date().toISOString() }); currentQuestion.value.answerCount++; answerContent.value = ''; ElMessage.success('回答提交成功'); } finally { submitting.value = false; }
    };

    const acceptAnswer = async (id) => {
      try { await ElMessageBox.confirm('确定采纳这个回答吗？', '确认'); const a = answers.value.find(x => x.id === id); if (a) { a.isAccepted = true; currentQuestion.value.status = 'CLOSED'; ElMessage.success('已采纳'); } } catch(e) {}
    };

    const doAsk = async () => {
      if (!askForm.title.trim()) { ElMessage.warning('请输入问题标题'); return; }
      if (!askForm.content.trim()) { ElMessage.warning('请输入问题描述'); return; }
      if (!askForm.categoryId) { ElMessage.warning('请选择分类'); return; }
      submitting.value = true;
      try { await new Promise(r => setTimeout(r, 300)); const cat = mockCategories.find(c => c.id === askForm.categoryId); const newQ = { id: Date.now(), title: askForm.title, content: askForm.content, categoryId: askForm.categoryId, categoryName: cat?.name || '其他', userId: currentUser.id, username: currentUser.username, userAvatar: currentUser.avatar, pointsReward: askForm.pointsReward, status: 'OPEN', answerCount: 0, viewCount: 0, createdAt: new Date().toISOString() }; mockQuestions.unshift(newQ); questionList.value = [...mockQuestions]; allQuestions.value = [...mockQuestions]; latestQuestions.value = [...mockQuestions].sort((a,b) => new Date(b.createdAt) - new Date(a.createdAt)).slice(0,5); askForm.title = ''; askForm.content = ''; askForm.categoryId = null; askForm.pointsReward = 0; navigate('questions'); ElMessage.success('问题发布成功'); } finally { submitting.value = false; }
    };

    const deleteQuestion = async (id) => {
      try { await ElMessageBox.confirm('确定删除这个问题吗？', '确认删除', { type: 'warning' }); const idx = mockQuestions.findIndex(q => q.id === id); if (idx > -1) { mockQuestions.splice(idx, 1); loadAllData(); ElMessage.success('删除成功'); } } catch(e) {}
    };

    const loadResources = () => { resources.value = resourceKeyword.value ? mockResources.filter(r => r.title.includes(resourceKeyword.value)) : [...mockResources]; };

    const filterByCategory = (catId) => { filterStatus.value = ''; searchKeyword.value = ''; questionList.value = mockQuestions.filter(q => q.categoryId === catId); currentPage.value = 'questions'; };

    const saveProfile = () => { currentUser.email = editForm.email; currentUser.avatar = editForm.avatar; editProfileVisible.value = false; ElMessage.success('资料已更新'); };

    const formatDate = (d) => { if (!d) return ''; const date = new Date(d); return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`; };

    onMounted(() => { if (localStorage.getItem('token')) { isLoggedIn.value = true; loadAllData(); } });

    return {
      isLoggedIn, showLogin, loading, submitting, currentPage, currentPageNum, pageSize, questionTotal,
      searchKeyword, filterStatus, resourceKeyword, answerContent, editProfileVisible,
      currentUser, loginForm, regForm, editForm, askForm,
      hotQuestions, latestQuestions, questionList, currentQuestion, answers,
      categories, resources, stats, allQuestions, myQuestions, myAnswers,
      navigate, handleUserCommand, doLogin, doRegister, searchQuestions,
      viewQuestion, submitAnswer, acceptAnswer, doAsk, deleteQuestion, loadResources,
      filterByCategory, saveProfile, formatDate,
      User: icons.User, Lock: icons.Lock, Message: icons.Message,
      HomeFilled: icons.HomeFilled, ChatDotRound: icons.ChatDotRound,
      Document: icons.Document, Setting: icons.Setting, Plus: icons.Plus,
      Search: icons.Search, ArrowRight: icons.ArrowRight, ArrowLeft: icons.ArrowLeft,
      ArrowDown: icons.ArrowDown, Download: icons.Download, DocumentChecked: icons.DocumentChecked,
      DocumentDelete: icons.DocumentDelete, ChatLineRound: icons.ChatLineRound,
      FolderOpened: icons.FolderOpened, CircleCheck: icons.CircleCheck, UserFilled: icons.UserFilled
    };
  }
}).use(ElementPlus).mount('#app');
