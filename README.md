# StudyHub - 线上答疑互助平台

Java程序设计课程项目。一个面向高校学生的线上答疑互助平台，支持课程提问、互助回答、学习资源分享等功能。

---

## 功能模块

| 模块 | 功能说明 |
|------|---------|
| 用户管理 | 学号/邮箱注册登录、JWT认证、个人信息管理、角色权限控制 |
| **答疑互助（核心）** | **发布问题、设置标签/悬赏、提交回答、采纳最佳答案、积分奖励** |
| 资源分享 | 学习资料上传/下载、按学科分类、点赞收藏、积分兑换 |
| 后台管理 | 数据统计看板、用户/资源管理、公告发布 |

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + Element Plus + Axios + Vue Router |
| 后端 | Spring Boot 3 + MyBatis-Plus + Spring Security |
| 数据库 | MySQL 8.0 |
| 工具 | Maven + Git + Swagger（接口文档） |
| 开发模式 | AI辅助生成代码骨架 + 人工审阅业务逻辑 |

## 团队成员（5人）

| 角色 | 人员 | 职责 |
|------|------|------|
| 项目经理 | 待填 | 需求分析、进度把控、Git管理、联调协调 |
| 后端开发A | 待填 | 用户模块、答疑模块、数据库设计 |
| 后端开发B | 待填 | 资源模块、后台管理、数据统计 |
| 前端开发 | 待填 | 全部前端页面、组件封装、接口对接 |
| 测试/文档 | 待填 | 测试用例、Bug跟踪、技术文档 |

## 项目结构

```
study-hub/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/studyhub/
│   │   ├── controller/         # 控制器层（REST API入口）
│   │   ├── service/            # 业务逻辑层
│   │   ├── mapper/             # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── config/             # 配置类（Security/JWT）
│   │   └── StudyHubApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/             # XML映射文件
│   └── pom.xml
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 通用组件
│   │   ├── api/                # 接口请求封装
│   │   ├── router/             # 路由配置
│   │   └── store/              # 状态管理
│   ├── package.json
│   └── vite.config.js
├── README.md
└── .gitignore
```

## 数据库设计

| 表名 | 说明 |
|------|------|
| user | 用户表（id, username, password, email, avatar, role, points） |
| question | 问题表（id, title, content, category_id, user_id, points_reward, status） |
| answer | 回答表（id, content, question_id, user_id, is_accepted） |
| resource | 资源表（id, title, file_url, category, user_id, points_cost） |
| category | 分类表（id, name, description） |

## 接口规范

- 基础路径：`/api/v1/`
- 认证方式：JWT Token（Header: `Authorization: Bearer &lt;token&gt;`）
- 接口文档：启动后端后访问 `http://localhost:8080/swagger-ui.html`
- 响应格式：`{ "code": 200, "data": {}, "message": "ok" }`

## 本地启动

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0

### 后端启动
```bash
cd backend
# 1. 修改 application.yml 中的数据库配置
# 2. 启动项目
mvn spring-boot:run
# 访问 http://localhost:8080/swagger-ui.html
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
# 访问 http://localhost:5173
```

## 开发规范

- **分支策略**：`main`（稳定）→ `dev`（集成）→ `feature/xxx`（功能分支）
- **代码提交**：通过 Pull Request 合并，至少1人 Review
- **每日同步**：每晚22:00线上站会，3分钟汇报进度
- **AI使用**：AI生成代码骨架，人工审阅业务逻辑和安全相关代码

## 开发计划（2周）

| 阶段 | 时间 | 目标 |
|------|------|------|
| 基础搭建 | Day 1-2 | 框架搭建 + 用户模块 |
| 核心功能 | Day 3-4 | 答疑模块（提问/回答/采纳）可演示 |
| 功能扩展 | Day 5-6 | 资源模块 + 后台管理 |
| 联调测试 | Day 7-9 | 全面联调 + Bug修复 |
| 项目交付 | Day 10 | 回归测试 + 演示汇报 |
