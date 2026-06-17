-- 记账应用数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS bookkeeping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bookkeeping;

-- 用户表（如果不存在）
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    age INT,
    occupation VARCHAR(50),
    gender VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 记账记录表（如果不存在）
CREATE TABLE IF NOT EXISTS bookkeeping_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    type VARCHAR(10) NOT NULL COMMENT '收入/支出',
    category VARCHAR(50) NOT NULL COMMENT '类别',
    amount DECIMAL(10, 2) NOT NULL,
    record_date DATE NOT NULL,
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone_date (phone, record_date),
    INDEX idx_phone_type (phone, type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预算表
CREATE TABLE IF NOT EXISTS budget (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL COMMENT '类别',
    amount DECIMAL(10, 2) NOT NULL COMMENT '预算金额',
    month VARCHAR(7) NOT NULL COMMENT '月份 yyyy-MM',
    current_spent DECIMAL(10, 2) DEFAULT 0.00 COMMENT '当前已花费',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_phone_category_month (phone, category, month),
    INDEX idx_phone_month (phone, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 债务表
CREATE TABLE IF NOT EXISTS debt (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    type VARCHAR(10) NOT NULL COMMENT '借出/借入',
    counterparty VARCHAR(50) NOT NULL COMMENT '对方姓名',
    amount DECIMAL(10, 2) NOT NULL COMMENT '总金额',
    repaid_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '已还金额',
    debt_date DATE NOT NULL COMMENT '借款日期',
    due_date DATE COMMENT '到期日期',
    status VARCHAR(20) DEFAULT 'UNPAID' COMMENT 'UNPAID, PARTIAL, PAID',
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone),
    INDEX idx_phone_type (phone, type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 周期账单表
CREATE TABLE IF NOT EXISTS recurring_bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    type VARCHAR(10) NOT NULL COMMENT '收入/支出',
    category VARCHAR(50) NOT NULL COMMENT '类别',
    amount DECIMAL(10, 2) NOT NULL,
    frequency VARCHAR(20) NOT NULL COMMENT 'DAILY, WEEKLY, MONTHLY, YEARLY',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    day_of_month INT COMMENT '每月第几天',
    day_of_week INT COMMENT '每周第几天',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone),
    INDEX idx_phone_active (phone, is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 账户表
CREATE TABLE IF NOT EXISTS account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    account_name VARCHAR(50) NOT NULL COMMENT '账户名称',
    account_type VARCHAR(20) NOT NULL COMMENT '账户类型',
    balance DECIMAL(10, 2) DEFAULT 0.00 COMMENT '余额',
    icon VARCHAR(50) COMMENT '图标',
    color VARCHAR(20) COMMENT '颜色',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入测试数据（可选）
-- INSERT INTO user (phone, password, name, age, occupation, gender)
-- VALUES ('13800138000', 'password123', '测试用户', 25, '软件工程师', '男');
