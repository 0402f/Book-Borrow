# 图书借阅管理系统说明文档

## 项目概述
本项目是一个基于Spring Boot + Vue.js的现代化图书借阅管理系统，采用前后端分离架构设计。系统主要用于管理图书借阅、用户管理等功能，为图书馆提供便捷的管理服务。(部分功能尚未完全实现)
## 项目效果


## 技术栈

### 后端技术栈
- **核心框架**: Spring Boot 3.4.3
- **数据库**: MySQL
- **缓存**: Redis
- **认证授权**: JWT (JSON Web Token)
- **对象存储**: 阿里云OSS
- **ORM框架**: MyBatis
- **分页插件**: PageHelper
- **项目构建**: Maven
- **API文档**: Swagger/OpenAPI
- **日志框架**: Logback (Spring Boot默认)

### 前端技术栈
项目包含两个前端应用：
1. **Web端 (kite-event)**
   - Vue.js
   - Element Plus
   - Vite构建工具
   - Axios请求库
   - Pinia状态管理

2. **移动端 (myproject)**
   - uni-app框架
   - Vue.js
   - 统一状态管理

## 系统架构

### 后端架构
- 采用分层架构设计：
  - Controller层：处理HTTP请求
  - Service层：业务逻辑处理
  - Mapper层：数据访问层
  - Utils：工具类
  - Interceptor：拦截器
  - Exception：全局异常处理

### 数据存储
- MySQL：存储核心业务数据
- Redis：存储用户Token，提升系统性能
- 阿里云OSS：存储图片等媒体文件

## 主要功能模块

### 1. 用户管理
- 用户注册
- 用户登录
- 用户信息管理
- JWT token认证
- 用户权限控制

### 2. 图书借阅管理
- 图书借阅
- 借阅记录查询
- 图书归还
- 借阅统计

### 3. 文件管理
- 文件上传
- 阿里云OSS对象存储集成

### 4. 系统功能
- 全局异常处理
- 统一响应格式
- 分页查询
- 登录拦截
- 跨域处理

## 安全特性
- JWT token认证
- Redis token存储
- 密码加密
- 登录拦截器
- 全局异常处理

## 部署要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+
- Node.js 14+

## 环境配置
系统需要配置以下环境变量：
- ALI_OSS_ENDPOINT
- ALI_OSS_ACCESS_KEY
- ALI_OSS_ACCESS_SECRET
- ALI_OSS_BUCKET_NAME

## 项目结构
```plaintext
Book-Borrow/
├── SLT/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── kite-event/            # Web端前端项目
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── router/
│   │   ├── store/
│   │   └── views/
│   └── package.json
└── myproject/            # 移动端项目
    ├── src/
    │   ├── pages/
    │   ├── static/
    │   └── utils/
    └── package.json