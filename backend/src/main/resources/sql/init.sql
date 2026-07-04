CREATE DATABASE IF NOT EXISTS studyhub
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE studyhub;

-- 用户表
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    student_id VARCHAR(50),
    avatar VARCHAR(500),
    role VARCHAR(20) DEFAULT 'USER',
    points INT DEFAULT 100,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分类表
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 问题表
CREATE TABLE question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content MEDIUMTEXT,
    category_id BIGINT,
    user_id BIGINT NOT NULL,
    points_reward INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'OPEN',
    view_count INT DEFAULT 0,
    answer_count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_user_id (user_id),
    KEY idx_category_id (category_id),
    KEY idx_status (status),
    KEY idx_created_at (created_at),
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 回答表
CREATE TABLE answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content MEDIUMTEXT NOT NULL,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_accepted TINYINT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_question_id (question_id),
    KEY idx_user_id (user_id),
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 资源表（后端B负责）
CREATE TABLE resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    file_url VARCHAR(500),
    category VARCHAR(50),
    user_id BIGINT NOT NULL,
    download_count INT DEFAULT 0,
    points_cost INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始分类
INSERT INTO category (name, description, sort_order) VALUES
('Java编程', 'Java语言、Spring框架、面向对象等', 1),
('数据结构', '链表、树、图、排序算法等', 2),
('数据库', 'MySQL、SQL优化、数据库设计等', 3),
('计算机网络', 'TCP/IP、HTTP、网络安全等', 4),
('操作系统', '进程管理、内存管理、文件系统等', 5),
('高等数学', '微积分、线性代数、概率论等', 6),
('大学英语', '四六级、翻译、写作等', 7),
('其他', '其他课程相关问题', 99);

-- 默认管理员（密码: admin123, BCrypt加密）
INSERT INTO user (username, password, email, role, points, status) VALUES
('admin', '$2b$10$1ZgbZmpxGJehA4xli0Fs2OhHAkLLgLtS5oF1Uty1dY0NF6KPQphPi', 'admin@studyhub.com', 'ADMIN', 9999, 'ACTIVE');
