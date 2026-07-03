CREATE DATABASE IF NOT EXISTS studyhub 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

USE studyhub;

-- 用户表
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    avatar VARCHAR(255),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    points INT DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分类表
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200)
);

-- 问题表
CREATE TABLE question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    category_id BIGINT,
    user_id BIGINT NOT NULL,
    points_reward INT DEFAULT 0,
    status ENUM('OPEN', 'CLOSED') DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 回答表
CREATE TABLE answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_accepted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES question(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 资源表
CREATE TABLE resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    file_url VARCHAR(500),
    category VARCHAR(50),
    user_id BIGINT NOT NULL,
    download_count INT DEFAULT 0,
    points_cost INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 初始化分类数据
INSERT INTO category (name, description) VALUES
('Java', 'Java编程相关问题'),
('数据库', 'MySQL、SQL等数据库问题'),
('前端', 'Vue、HTML、CSS等前端问题'),
('算法', '数据结构与算法问题'),
('其他', '其他课程相关问题');
