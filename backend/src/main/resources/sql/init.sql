-- StudyHub Database Initialization Script
-- MySQL 8.0+

-- Create database
CREATE DATABASE IF NOT EXISTS studyhub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE studyhub;

-- ==============================
-- 1. User Table
-- ==============================
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '加密密码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `points` INT DEFAULT 100 COMMENT '积分',
    `role` ENUM('USER', 'ADMIN') DEFAULT 'USER' COMMENT '角色',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ==============================
-- 2. Category Table
-- ==============================
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- ==============================
-- 3. Question Table
-- ==============================
CREATE TABLE IF NOT EXISTS `question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '问题ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '提问者ID',
    `points_reward` INT DEFAULT 0 COMMENT '悬赏积分',
    `status` ENUM('OPEN', 'CLOSED') DEFAULT 'OPEN' COMMENT '状态',
    `answer_count` INT DEFAULT 0 COMMENT '回答数',
    `view_count` INT DEFAULT 0 COMMENT '浏览数',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- ==============================
-- 4. Answer Table
-- ==============================
CREATE TABLE IF NOT EXISTS `answer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '回答ID',
    `content` TEXT NOT NULL COMMENT '内容',
    `question_id` BIGINT NOT NULL COMMENT '问题ID',
    `user_id` BIGINT NOT NULL COMMENT '回答者ID',
    `is_accepted` TINYINT DEFAULT 0 COMMENT '是否采纳',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_question_id` (`question_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_accepted` (`is_accepted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回答表';

-- ==============================
-- 5. Resource Table
-- ==============================
CREATE TABLE IF NOT EXISTS `resource` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资源ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `category` VARCHAR(50) NOT NULL COMMENT '分类',
    `user_id` BIGINT NOT NULL COMMENT '上传者ID',
    `file_url` VARCHAR(500) DEFAULT NULL COMMENT '文件URL',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小(字节)',
    `download_count` INT DEFAULT 0 COMMENT '下载次数',
    `points_cost` INT DEFAULT 0 COMMENT '下载所需积分',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

-- ==============================
-- Insert Sample Data
-- ==============================

-- Categories
INSERT INTO `category` (`id`, `name`, `description`, `sort_order`) VALUES
(1, 'Java', 'Java编程相关问题，包括基础语法、面向对象、集合框架、多线程等', 1),
(2, '数据库', 'MySQL、Redis、SQL优化等数据库相关问题', 2),
(3, '前端', 'Vue、React、HTML、CSS、JavaScript等前端技术', 3),
(4, '算法', '数据结构与算法、LeetCode题解、面试算法', 4),
(5, 'Spring', 'Spring Boot、Spring Cloud、微服务架构', 5),
(6, '运维', 'Linux、Docker、K8s、CI/CD等DevOps技术', 6),
(7, '其他', '其他课程相关、学习方法、职业规划等', 7)
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- Sample Users (password: 123456, bcrypt encoded)
INSERT INTO `user` (`id`, `username`, `password`, `email`, `avatar`, `points`, `role`) VALUES
(1, 'demo', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'demo@xmu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 100, 'USER'),
(2, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'admin@xmu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 999, 'ADMIN'),
(3, 'zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'zhangsan@xmu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 150, 'USER'),
(4, 'lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'lisi@xmu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 80, 'USER'),
(5, 'wangwu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'wangwu@xmu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 200, 'USER')
ON DUPLICATE KEY UPDATE username=VALUES(username);

-- Sample Questions
INSERT INTO `question` (`id`, `title`, `content`, `category_id`, `user_id`, `points_reward`, `status`, `answer_count`, `view_count`, `created_at`) VALUES
(1, 'Java中接口和抽象类的区别是什么？', '面试经常被问到，想搞清楚两者的本质区别和适用场景。什么时候应该用接口，什么时候应该用抽象类？Java 8之后有default方法，两者界限是不是更模糊了？', 1, 3, 20, 'CLOSED', 2, 156, '2025-07-01 10:30:00'),
(2, 'MySQL索引失效的常见原因有哪些？', '项目里遇到了慢查询问题，怀疑是索引失效导致的。请问有哪些常见场景会导致索引失效？比如用了函数、类型转换、like %xx%这些。有没有系统的排查方法？', 2, 4, 15, 'OPEN', 1, 89, '2025-07-02 14:00:00'),
(3, 'Vue3的Composition API和Options API怎么选？', '新项目用Vue3，在纠结用哪种写法。团队里有人习惯Options API，但Composition API看起来更灵活。实际项目中应该怎么选？能不能混用？', 3, 3, 10, 'CLOSED', 3, 234, '2025-07-02 16:30:00'),
(4, 'Spring Boot如何实现JWT认证？', '正在做课程项目，需要实现登录认证功能。看了很多教程但还是不太明白JWT的工作原理。有没有完整的实现方案，包括Token生成、验证、刷新？', 5, 5, 25, 'OPEN', 0, 67, '2025-07-03 09:15:00'),
(5, '快速排序的时间复杂度怎么分析？', '数据结构课要考排序算法，不太理解时间复杂度的推导。为什么快速排序平均是O(nlogn)，最坏是O(n^2)？怎么证明？', 4, 4, 10, 'OPEN', 1, 45, '2025-07-03 11:20:00'),
(6, 'Redis和MySQL如何保证数据一致性？', '项目中同时用了Redis做缓存和MySQL做持久化，经常出现数据不一致的情况。比如先更新DB再删缓存，还是先发消息再更新？最佳实践是什么？', 2, 5, 30, 'OPEN', 0, 78, '2025-07-04 08:00:00'),
(7, 'Docker部署Spring Boot应用的最佳实践', '准备把课程项目部署到服务器上，想用Docker容器化。请问Spring Boot应用Docker化有哪些注意事项？镜像怎么优化大小？', 6, 3, 15, 'OPEN', 0, 34, '2025-07-04 13:45:00'),
(8, '如何准备Java后端开发实习面试？', '大二学生，想找Java后端开发的实习。现在学了Java基础、Spring Boot、MySQL，还需要学什么？LeetCode要刷多少题？简历怎么写？', 1, 4, 20, 'OPEN', 2, 189, '2025-07-05 10:00:00')
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- Sample Answers
INSERT INTO `answer` (`id`, `content`, `question_id`, `user_id`, `is_accepted`, `created_at`) VALUES
(1, '核心区别在于：接口只能定义方法签名不能有实现，抽象类可以有部分实现。一个类可以实现多个接口但只能继承一个抽象类。\n\n建议优先用接口定义行为契约，用抽象类提取公共代码。Java 8之后接口可以有default方法，但接口仍然不能持有状态（实例变量），所以还是遵循"面向接口编程"原则。', 1, 5, 1, '2025-07-01 12:00:00'),
(2, '补充一点：接口中的default方法主要是为了向后兼容，比如Java 8在Collection接口中加了很多default方法。实际项目中如果一个类需要多个不同来源的行为，用接口组合；如果需要复用代码模板，用抽象类。', 1, 1, 0, '2025-07-01 14:30:00'),
(3, '常见原因：1.对索引列使用函数 2.隐式类型转换 3.OR条件中部分列无索引 4.LIKE以%开头 5.数据量太小全表扫描更快 6.!=或<>操作符\n\n排查方法：用EXPLAIN分析执行计划，关注type列（ALL表示全表扫描）、key列（实际使用的索引）、rows列（扫描行数）。', 2, 5, 0, '2025-07-02 16:00:00'),
(4, 'Composition API更适合逻辑复用和TypeScript，Options API更适合小项目或初学者。新项目推荐Composition API，配合script setup语法糖代码更简洁。\n\n可以混用，但不建议。建议团队统一规范，新项目统一用Composition API。', 3, 1, 1, '2025-07-02 18:00:00'),
(5, '快速排序平均O(n log n)是因为每次partition大致将数组分成两半，递归深度是log n，每层操作是O(n)。最坏O(n^2)发生在每次选的基准都是最大或最小值时（比如数组已有序）。\n\n可以用随机化基准或三数取中法来避免最坏情况。实际应用中很少出现最坏情况。', 5, 5, 1, '2025-07-03 13:00:00'),
(6, '准备实习的话建议：1.基础要扎实：Java集合源码、JUC并发包、JVM内存模型 2.Spring生态：Spring Boot自动配置原理、AOP实现 3.数据库：索引原理、事务隔离级别、SQL优化 4.LeetCode至少刷100道，重点链表、树、动态规划 5.准备一个能演示的项目，有GitHub链接', 8, 5, 1, '2025-07-05 12:00:00'),
(7, '另外建议关注一些技术博客和公众号，了解行业动态。面试时不仅要会做题，还要能讲清楚思路。实习面试一般不会太难，重点考察基础和潜力。', 8, 3, 0, '2025-07-05 15:30:00')
ON DUPLICATE KEY UPDATE content=VALUES(content);

-- Sample Resources
INSERT INTO `resource` (`id`, `title`, `description`, `category`, `user_id`, `file_url`, `file_size`, `download_count`, `points_cost`, `created_at`) VALUES
(1, 'Java核心技术笔记.pdf', '包含面向对象、集合框架、IO流、多线程、JVM等核心知识点，共120页', 'Java', 3, '/files/java-core-notes.pdf', 5242880, 56, 5, '2025-07-01 08:00:00'),
(2, 'MySQL优化手册.md', '索引优化、查询优化、慢查询分析、事务调优，实战案例丰富', '数据库', 5, '/files/mysql-optimization.md', 2097152, 32, 3, '2025-07-02 10:00:00'),
(3, 'Vue3快速入门.pdf', 'Composition API、Pinia状态管理、Vue Router 4、TypeScript整合', '前端', 3, '/files/vue3-quickstart.pdf', 8388608, 89, 5, '2025-07-02 14:00:00'),
(4, '算法面试题汇总.pdf', 'LeetCode高频题分类整理：数组、链表、树、动态规划、回溯', '算法', 4, '/files/algorithm-interview.pdf', 10485760, 120, 10, '2025-06-28 09:00:00'),
(5, 'Spring Boot实战项目源码.zip', '完整的Spring Boot + Vue3前后端分离项目，含JWT认证、CRUD、分页', 'Spring', 5, '/files/springboot-demo.zip', 15728640, 45, 8, '2025-07-03 11:00:00'),
(6, 'Linux常用命令速查.pdf', '文件操作、进程管理、网络工具、Shell脚本，适合初学者', '运维', 4, '/files/linux-commands.pdf', 3145728, 67, 2, '2025-07-04 16:00:00')
ON DUPLICATE KEY UPDATE title=VALUES(title);
