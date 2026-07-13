package com.studyhub.config;

import com.studyhub.entity.*;
import com.studyhub.mapper.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

/**
 * H2 environment data initializer.
 * Auto-creates tables and inserts sample data when running with --spring.profiles.active=h2
 * Requires NO MySQL installation. All data is stored in memory.
 */
@Configuration
@Profile("h2")
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            JdbcTemplate jdbc,
            UserMapper userMapper,
            CategoryMapper categoryMapper,
            QuestionMapper questionMapper,
            AnswerMapper answerMapper,
            ResourceMapper resourceMapper,
            LikeMapper likeMapper,
            CommentMapper commentMapper,
            FollowMapper followMapper) {
        return args -> {
            // 先删除旧表（防止之前不完整的表结构残留）
            jdbc.execute("DROP TABLE IF EXISTS likes");
            jdbc.execute("DROP TABLE IF EXISTS comment");
            jdbc.execute("DROP TABLE IF EXISTS follow");
            jdbc.execute("DROP TABLE IF EXISTS resource");
            jdbc.execute("DROP TABLE IF EXISTS answer");
            jdbc.execute("DROP TABLE IF EXISTS question");
            jdbc.execute("DROP TABLE IF EXISTS category");
            jdbc.execute("DROP TABLE IF EXISTS user");

            // ==============================
            // 1. Create tables (H2 compatible)
            // ==============================

            jdbc.execute("CREATE TABLE IF NOT EXISTS user (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(100)," +
                "avatar VARCHAR(255)," +
                "points INT DEFAULT 100," +
                "role VARCHAR(20) DEFAULT 'USER'," +
                "student_id VARCHAR(50)," +
                "status VARCHAR(20) DEFAULT 'ACTIVE'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "deleted TINYINT DEFAULT 0)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS category (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(50) NOT NULL UNIQUE," +
                "description VARCHAR(255)," +
                "sort_order INT DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS question (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(200) NOT NULL," +
                "content TEXT NOT NULL," +
                "category_id BIGINT NOT NULL," +
                "user_id BIGINT NOT NULL," +
                "points_reward INT DEFAULT 0," +
                "status VARCHAR(20) DEFAULT 'OPEN'," +
                "answer_count INT DEFAULT 0," +
                "view_count INT DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "deleted TINYINT DEFAULT 0)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS answer (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "content TEXT NOT NULL," +
                "question_id BIGINT NOT NULL," +
                "user_id BIGINT NOT NULL," +
                "is_accepted BOOLEAN DEFAULT FALSE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "deleted TINYINT DEFAULT 0)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS resource (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(200) NOT NULL," +
                "description VARCHAR(500)," +
                "category VARCHAR(50) NOT NULL," +
                "user_id BIGINT NOT NULL," +
                "file_url VARCHAR(500)," +
                "download_count INT DEFAULT 0," +
                "points_cost INT DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "deleted TINYINT DEFAULT 0)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS likes (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "user_id BIGINT NOT NULL," +
                "target_id BIGINT NOT NULL," +
                "target_type VARCHAR(20) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS comment (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "answer_id BIGINT NOT NULL," +
                "user_id BIGINT NOT NULL," +
                "content TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "deleted TINYINT DEFAULT 0)");

            jdbc.execute("CREATE TABLE IF NOT EXISTS follow (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "follower_id BIGINT NOT NULL," +
                "following_id BIGINT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // ==============================
            // 2. Insert sample data
            // ==============================
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String pwd = encoder.encode("123456");

            // --- Categories ---
            String[][] cats = {
                {"Java", "Java编程相关问题，包括基础语法、面向对象、集合框架、多线程等"},
                {"数据库", "MySQL、Redis、SQL优化等数据库相关问题"},
                {"前端", "Vue、React、HTML、CSS、JavaScript等前端技术"},
                {"算法", "数据结构与算法、LeetCode题解、面试算法"},
                {"Spring", "Spring Boot、Spring Cloud、微服务架构"},
                {"运维", "Linux、Docker、K8s、CI/CD等DevOps技术"},
                {"其他", "其他课程相关、学习方法、职业规划等"}
            };
            for (int i = 0; i < cats.length; i++) {
                Category c = new Category();
                c.setId((long)(i + 1));
                c.setName(cats[i][0]);
                c.setDescription(cats[i][1]);
                c.setSortOrder(i + 1);
                categoryMapper.insert(c);
            }

            // --- Users (password: 123456) ---
            String[][] users = {
                {"demo", "demo@xmu.edu.cn", "100", "USER", "20240001"},
                {"admin", "admin@xmu.edu.cn", "999", "ADMIN", "20240002"},
                {"zhangsan", "zhangsan@xmu.edu.cn", "150", "USER", "20240101"},
                {"lisi", "lisi@xmu.edu.cn", "80", "USER", "20240102"},
                {"wangwu", "wangwu@xmu.edu.cn", "200", "USER", "20240103"}
            };
            for (int i = 0; i < users.length; i++) {
                User u = new User();
                u.setId((long)(i + 1));
                u.setUsername(users[i][0]);
                u.setPassword(pwd);
                u.setEmail(users[i][1]);
                u.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
                u.setPoints(Integer.parseInt(users[i][2]));
                u.setRole(users[i][3]);
                u.setStudentId(users[i][4]);
                userMapper.insert(u);
            }

            // --- Questions ---
            String[][] questions = {
                {"Java中接口和抽象类的区别是什么？", "面试经常被问到，想搞清楚两者的本质区别和适用场景。什么时候应该用接口，什么时候应该用抽象类？Java 8之后有default方法，两者界限是不是更模糊了？", "1", "3", "20", "CLOSED", "2", "156"},
                {"MySQL索引失效的常见原因有哪些？", "项目里遇到了慢查询问题，怀疑是索引失效导致的。请问有哪些常见场景会导致索引失效？比如用了函数、类型转换、like %xx%这些。有没有系统的排查方法？", "2", "4", "15", "OPEN", "1", "89"},
                {"Vue3的Composition API和Options API怎么选？", "新项目用Vue3，在纠结用哪种写法。团队里有人习惯Options API，但Composition API看起来更灵活。实际项目中应该怎么选？能不能混用？", "3", "3", "10", "CLOSED", "3", "234"},
                {"Spring Boot如何实现JWT认证？", "正在做课程项目，需要实现登录认证功能。看了很多教程但还是不太明白JWT的工作原理。有没有完整的实现方案，包括Token生成、验证、刷新？", "5", "5", "25", "OPEN", "0", "67"},
                {"快速排序的时间复杂度怎么分析？", "数据结构课要考排序算法，不太理解时间复杂度的推导。为什么快速排序平均是O(nlogn)，最坏是O(n^2)？怎么证明？", "4", "4", "10", "OPEN", "1", "45"},
                {"Redis和MySQL如何保证数据一致性？", "项目中同时用了Redis做缓存和MySQL做持久化，经常出现数据不一致的情况。比如先更新DB再删缓存，还是先发消息再更新？最佳实践是什么？", "2", "5", "30", "OPEN", "0", "78"},
                {"Docker部署Spring Boot应用的最佳实践", "准备把课程项目部署到服务器上，想用Docker容器化。请问Spring Boot应用Docker化有哪些注意事项？镜像怎么优化大小？", "6", "3", "15", "OPEN", "0", "34"},
                {"如何准备Java后端开发实习面试？", "大二学生，想找Java后端开发的实习。现在学了Java基础、Spring Boot、MySQL，还需要学什么？LeetCode要刷多少题？简历怎么写？", "1", "4", "20", "OPEN", "2", "189"}
            };
            for (int i = 0; i < questions.length; i++) {
                Question q = new Question();
                q.setId((long)(i + 1));
                q.setTitle(questions[i][0]);
                q.setContent(questions[i][1]);
                q.setCategoryId(Long.parseLong(questions[i][2]));
                q.setUserId(Long.parseLong(questions[i][3]));
                q.setPointsReward(Integer.parseInt(questions[i][4]));
                q.setStatus(questions[i][5]);
                q.setAnswerCount(Integer.parseInt(questions[i][6]));
                q.setViewCount(Integer.parseInt(questions[i][7]));
                q.setCreatedAt(LocalDateTime.of(2025, 7, 1 + i / 3, 10 + i, 30));
                questionMapper.insert(q);
            }

            // --- Answers ---
            String[][] answers = {
                {"核心区别在于：接口只能定义方法签名不能有实现，抽象类可以有部分实现。一个类可以实现多个接口但只能继承一个抽象类。\n\n建议优先用接口定义行为契约，用抽象类提取公共代码。", "1", "5", "true"},
                {"补充一点：接口中的default方法主要是为了向后兼容。实际项目中如果一个类需要多个不同来源的行为，用接口组合；如果需要复用代码模板，用抽象类。", "1", "1", "false"},
                {"常见原因：1.对索引列使用函数 2.隐式类型转换 3.OR条件中部分列无索引 4.LIKE以%开头 5.数据量太小全表扫描更快 6.!=或<>操作符。\n\n排查方法：用EXPLAIN分析执行计划。", "2", "5", "false"},
                {"Composition API更适合逻辑复用和TypeScript，Options API更适合小项目或初学者。新项目推荐Composition API，配合script setup语法糖代码更简洁。", "3", "1", "true"},
                {"快速排序平均O(n log n)是因为每次partition大致将数组分成两半，递归深度是log n，每层操作是O(n)。最坏O(n^2)发生在每次选的基准都是最大或最小值时。", "5", "5", "true"},
                {"准备实习的话建议：1.基础要扎实 2.Spring生态 3.数据库 4.LeetCode至少刷100道 5.准备一个能演示的项目", "8", "5", "true"},
                {"另外建议关注一些技术博客和公众号，了解行业动态。面试时不仅要会做题，还要能讲清楚思路。", "8", "3", "false"}
            };
            for (int i = 0; i < answers.length; i++) {
                Answer a = new Answer();
                a.setId((long)(i + 1));
                a.setContent(answers[i][0]);
                a.setQuestionId(Long.parseLong(answers[i][1]));
                a.setUserId(Long.parseLong(answers[i][2]));
                a.setIsAccepted(Boolean.parseBoolean(answers[i][3]));
                a.setCreatedAt(LocalDateTime.of(2025, 7, 1 + i / 2, 12 + i, 0));
                answerMapper.insert(a);
            }

            // --- Resources ---
            String[][] resources = {
                {"Java核心技术笔记.pdf", "包含面向对象、集合框架、IO流、多线程、JVM等核心知识点，共120页", "Java", "3", "/files/java-core-notes.pdf", "56", "5"},
                {"MySQL优化手册.md", "索引优化、查询优化、慢查询分析、事务调优，实战案例丰富", "数据库", "5", "/files/mysql-optimization.md", "32", "3"},
                {"Vue3快速入门.pdf", "Composition API、Pinia状态管理、Vue Router 4、TypeScript整合", "前端", "3", "/files/vue3-quickstart.pdf", "89", "5"},
                {"算法面试题汇总.pdf", "LeetCode高频题分类整理：数组、链表、树、动态规划、回溯", "算法", "4", "/files/algorithm-interview.pdf", "120", "10"},
                {"Spring Boot实战项目源码.zip", "完整的Spring Boot + Vue3前后端分离项目，含JWT认证、CRUD、分页", "Spring", "5", "/files/springboot-demo.zip", "45", "8"},
                {"Linux常用命令速查.pdf", "文件操作、进程管理、网络工具、Shell脚本，适合初学者", "运维", "4", "/files/linux-commands.pdf", "67", "2"}
            };
            for (int i = 0; i < resources.length; i++) {
                Resource r = new Resource();
                r.setId((long)(i + 1));
                r.setTitle(resources[i][0]);
                r.setDescription(resources[i][1]);
                r.setCategory(resources[i][2]);
                r.setUserId(Long.parseLong(resources[i][3]));
                r.setFileUrl(resources[i][4]);
                r.setDownloadCount(Integer.parseInt(resources[i][5]));
                r.setPointsCost(Integer.parseInt(resources[i][6]));
                r.setCreatedAt(LocalDateTime.of(2025, 7, 1 + i / 2, 8 + i, 0));
                resourceMapper.insert(r);
            }

            // --- Likes (sample) ---
            Object[][] likes = {
                {1, 1, "QUESTION"}, {2, 1, "QUESTION"}, {3, 1, "QUESTION"},
                {1, 2, "QUESTION"}, {2, 3, "QUESTION"}, {3, 3, "QUESTION"},
                {1, 4, "QUESTION"}, {4, 5, "QUESTION"}, {5, 8, "QUESTION"},
                {1, 1, "ANSWER"}, {2, 1, "ANSWER"}, {3, 4, "ANSWER"}, {4, 5, "ANSWER"}, {5, 6, "ANSWER"}
            };
            for (Object[] like : likes) {
                Like l = new Like();
                l.setUserId(((Number)like[0]).longValue());
                l.setTargetId(((Number)like[1]).longValue());
                l.setTargetType((String)like[2]);
                l.setCreatedAt(LocalDateTime.now());
                likeMapper.insert(l);
            }

            // --- Comments (sample) ---
            String[][] comments = {
                {"1", "1", "说得很清楚，感谢！"},
                {"1", "2", "补充得很到位，学习了。"},
                {"1", "3", "收藏了，面试前再看看。"},
                {"4", "3", "请问有具体的例子吗？"},
                {"4", "5", "讲得很透彻！"},
                {"3", "6", "很有用的建议，谢谢！"}
            };
            for (int i = 0; i < comments.length; i++) {
                Comment c = new Comment();
                c.setId((long)(i + 1));
                c.setAnswerId(Long.parseLong(comments[i][0]));
                c.setUserId(Long.parseLong(comments[i][1]));
                c.setContent(comments[i][2]);
                c.setCreatedAt(LocalDateTime.now());
                c.setUpdatedAt(LocalDateTime.now());
                c.setDeleted(0);
                commentMapper.insert(c);
            }

            // --- Follows (sample) ---
            int[][] follows = {
                {1, 2}, {1, 3}, {1, 5},
                {3, 1}, {3, 5},
                {4, 1}, {4, 3}, {4, 5},
                {5, 1}, {5, 3}
            };
            for (int[] f : follows) {
                Follow follow = new Follow();
                follow.setFollowerId((long)f[0]);
                follow.setFollowingId((long)f[1]);
                follow.setCreatedAt(LocalDateTime.now());
                followMapper.insert(follow);
            }
        };
    }
}
