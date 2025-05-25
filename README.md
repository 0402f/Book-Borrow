# 图书借阅管理系统说明文档

## 项目概述
本项目是一个基于Spring Boot + Vue.js的现代化图书借阅管理系统，采用前后端分离架构设计。系统主要用于管理图书借阅、用户管理等功能，为图书馆提供便捷的管理服务。(部分功能尚未完全实现)
## 项目效果
管理员：
![e44248120382e11cab7ef5535eb3cffb](https://github.com/user-attachments/assets/acad4d04-965b-417f-97a5-3e2bca45c7d0)
![0ccae1157f809e1768d55ecce0b41d69](https://github.com/user-attachments/assets/f290562e-9a70-4f63-82ec-3f569a0b8029)
![42bef815aefd7b6da49e721529197169](https://github.com/user-attachments/assets/8c2f606d-c476-4407-bf29-3241f768376e)
![1983040ba5fcb33fc338dfd204df04be](https://github.com/user-attachments/assets/474605c3-4d0c-4fb1-8607-c8b2c15d513d)
![5c709a078f67c9d0d14411f51f2f7fe1](https://github.com/user-attachments/assets/83e664f8-7126-4e1a-b9cc-724765e63025)
用户端：
![34abe10e5842b0ac35b718090c084e5a](https://github.com/user-attachments/assets/230751b6-997d-41aa-a2f9-b240ad6e87c5)
![6da46ba2b45bf0ddf33c856fa00a9916](https://github.com/user-attachments/assets/aa8f0860-e4cd-4905-ad1d-3bca696d289c)
![572b547a3ad86ad5991ced0ae97f2100](https://github.com/user-attachments/assets/dc5435d4-400c-4b07-ba56-5354abc2086f)
![d2ab1eac74769160d31d8935dee16015](https://github.com/user-attachments/assets/6aaa4fad-d6ed-4207-8fa8-b84bbb0062e6)
![3a4d0d382870705f0430dda1169a204e](https://github.com/user-attachments/assets/f9418e3c-5060-4223-835f-20488c243166)



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
