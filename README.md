# Bookkeeping API

一个基于 Spring Boot 的个人记账后端接口项目，面向移动端或 Web 前端提供用户管理、记账、预算、债务管理和统计分析能力。

## 项目描述

`bookkeeping-api` 是一个轻量级 RESTful API 服务，用于支撑个人财务管理场景。项目围绕“用户 + 账单记录”展开，在此基础上扩展了月度预算、债务跟踪和年度/分类统计等功能，适合作为记账应用、小程序或课程项目的后端基础服务。

当前项目已实现的核心方向：

- 用户注册、登录、资料查询与更新
- 收入/支出记账与月度账单查询
- 预算创建、编辑、删除与月度预算使用情况统计
- 债务记录、还款处理与状态更新
- 收支趋势、分类统计、年度报告
- 账单识别接口预留（当前为占位实现）

## 技术栈

- Java 8
- Spring Boot 2.2.6.RELEASE
- Spring Web
- Spring Data JPA
- MySQL 8
- HikariCP
- Lombok
- Maven

## 功能模块

### 1. 用户模块

接口前缀：`/api/user`

- 用户注册
- 用户登录
- 根据手机号查询用户信息
- 更新用户资料
- 注销用户
- 查询用户记账条数

### 2. 记账模块

接口前缀：`/api/bookkeeping`

- 新增记账记录
- 更新记账记录
- 删除记账记录
- 按月份查询账单
- 查询单条账单详情
- 上传账单文件进行识别（当前返回空识别结果）

### 3. 预算模块

接口前缀：`/api/budget`

- 查询指定月份预算
- 新增预算
- 更新预算
- 删除预算
- 自动汇总分类已支出金额

### 4. 债务模块

接口前缀：`/api/debt`

- 查询债务列表
- 新增债务
- 编辑债务
- 还款
- 删除债务

### 5. 统计模块

接口前缀：`/api/statistics`

- 最近 N 个月收支趋势
- 指定月份分类支出统计
- 年度财务报告

## 项目结构

```text
src
├─ main
│  ├─ java/com/bookkeeping/bookkeepingapi
│  │  ├─ controller    # 接口层
│  │  ├─ service       # 业务层
│  │  ├─ repository    # 数据访问层
│  │  ├─ entity        # 实体类
│  │  └─ dto           # 请求/响应对象
│  └─ resources
│     ├─ application.properties
│     └─ db_init.sql
└─ test
   └─ java
```

## 运行要求

- JDK 8
- Maven 3.6+
- MySQL 8.x

## 快速开始

### 1. 创建数据库

执行初始化脚本：

```sql
source src/main/resources/db_init.sql;
```

或手动创建数据库：

```sql
CREATE DATABASE bookkeeping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 修改数据库配置

编辑 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookkeeping?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=你的数据库密码
```

默认端口：

```properties
server.port=8084
```

### 3. 启动项目

使用 Maven Wrapper：

```bash
./mvnw spring-boot:run
```

Windows：

```bash
mvnw.cmd spring-boot:run
```

或先打包再运行：

```bash
./mvnw clean package
java -jar target/bookkeeping-api-0.0.1-SNAPSHOT.jar
```

## 接口示例

### 用户注册

`POST /api/user/register`

```json
{
  "phone": "13800138000",
  "password": "123456",
  "confirmPassword": "123456",
  "name": "张三",
  "age": 25,
  "occupation": "工程师",
  "gender": "男"
}
```

### 新增记账

`POST /api/bookkeeping`

```json
{
  "phone": "13800138000",
  "type": "支出",
  "category": "餐饮",
  "amount": 35.50,
  "recordDate": "2026-07-01",
  "remark": "午餐"
}
```

### 查询月账单

`GET /api/bookkeeping?phone=13800138000&month=2026-07`

### 查询预算

`GET /api/budget?phone=13800138000&month=2026-07`

### 查询统计趋势

`GET /api/statistics/trend?phone=13800138000&months=6`

## 数据库表

项目中已包含以下主要表结构：

- `user`
- `bookkeeping_record`
- `budget`
- `debt`
- `recurring_bill`
- `account`

其中 `recurring_bill` 与 `account` 已有表定义和仓储接口，但当前控制器层尚未开放对应完整业务接口。

## 当前实现说明

- 项目使用 `spring.jpa.hibernate.ddl-auto=update`，启动时会自动同步部分表结构
- 账单识别接口 `/api/bookkeeping/recognize` 当前为占位实现
- 登录逻辑当前基于手机号和明文密码校验，未接入 JWT 或 Spring Security
- 已允许跨域请求，便于前后端分离联调

## 开发建议

- 将数据库账号密码改为环境变量或多环境配置
- 为登录与敏感接口增加密码加密、Token 鉴权和权限控制
- 补充接口文档工具，例如 Swagger / Springfox 或 springdoc
- 增加单元测试与集成测试
- 完善周期账单和账户模块接口

## 部署说明

仓库内提供了额外部署文档：

- `DEPLOYMENT_GUIDE.md`

## License

如需开源发布，建议补充具体许可证，例如 MIT。
