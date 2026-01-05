[README.md](https://github.com/user-attachments/files/24428622/README.md)
# 铁金库银行系统

## 项目简介
铁金库银行系统是一个基于Spring Boot的现代化银行管理系统，提供账户管理、存款、取款、转账和交易记录查询等核心银行功能。系统采用分层架构设计，结合现代化的前端界面，为用户提供安全、便捷的银行服务体验。

## 技术栈

### 后端
- **框架**：Spring Boot 3.2.0
- **ORM**：MyBatis 3.0.3
- **数据库**：SQL Server
- **事务管理**：Spring声明式事务
- **构建工具**：Maven
- **Java版本**：JDK 17

### 前端
- **模板引擎**：Thymeleaf
- **UI框架**：Bootstrap 5.x
- **CSS**：原生CSS3
- **JavaScript**：原生JavaScript

## 功能特性

### 1. 账户管理
- ✅ 新用户开户
- ✅ 账户信息查询
- ✅ 支付密码设置

### 2. 资金操作
- ✅ 存款功能
- ✅ 取款功能
- ✅ 转账功能
- ✅ 实时余额更新

### 3. 交易记录
- ✅ 交易记录查询
- ✅ 按时间倒序排列
- ✅ 包含交易类型、金额、余额、备注等信息

### 4. 安全机制
- ✅ 支付密码验证
- ✅ 事务管理
- ✅ 输入验证
- ✅ 错误处理

### 5. 其他特性
- ✅ 19位银行卡号自动生成
- ✅ 响应式设计，适配不同屏幕尺寸
- ✅ 现代化UI界面
- ✅ 友好的操作反馈

## 项目结构

```
IronBankSystem/
├── src/
│   ├── main/
│   │   ├── java/com/shop/bank/
│   │   │   ├── controller/       # 控制器层
│   │   │   │   └── BankController.java
│   │   │   ├── mapper/           # MyBatis映射接口
│   │   │   │   └── AccountMapper.java
│   │   │   ├── pojo/             # 实体类
│   │   │   │   ├── Account.java
│   │   │   │   ├── AccountAuth.java
│   │   │   │   ├── Customer.java
│   │   │   │   └── Transaction.java
│   │   │   ├── service/          # 业务逻辑层
│   │   │   │   ├── BankService.java
│   │   │   │   └── impl/
│   │   │   │       └── BankServiceImpl.java
│   │   │   └── IronBankSystemApplication.java  # 应用入口
│   │   └── resources/
│   │       ├── mapper/           # MyBatis映射XML
│   │       │   └── AccountMapper.xml
│   │       ├── static/           # 静态资源
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   ├── images/
│   │       │   │   └── logo.png
│   │       │   └── js/
│   │       │       └── main.js
│   │       ├── templates/        # Thymeleaf模板
│   │       │   ├── account.html
│   │       │   ├── index.html
│   │       │   ├── open.html
│   │       │   ├── result.html
│   │       │   └── transactions.html
│   │       └── application.properties  # 配置文件
├── target/                       # 编译输出目录
├── .idea/                        # IDEA项目配置
├── database_creation_script.sql  # 数据库创建脚本
├── 功能实现详细介绍.md            # 功能实现文档
├── 数据库执行指南.md              # 数据库配置指南
├── IDEA运行指南.md               # IDEA运行指南
├── pom.xml                       # Maven配置文件
└── README.md                     # 项目说明文档
```

## 安装与运行

### 1. 环境准备

#### 1.1 安装JDK 17
- 从Oracle官网或OpenJDK下载并安装Java 17
- 配置JAVA_HOME环境变量

#### 1.2 安装SQL Server
- 安装SQL Server（推荐使用SQL Server 2019或更高版本）
- 安装SQL Server Management Studio (SSMS)

#### 1.3 安装IntelliJ IDEA
- 安装IntelliJ IDEA（推荐使用Ultimate版本）
- 安装Spring Boot插件

### 2. 数据库配置

#### 2.1 执行数据库脚本
1. 启动SQL Server Management Studio
2. 连接到SQL Server实例
3. 打开`database_creation_script.sql`文件
4. 执行脚本，创建数据库和测试数据
5. 记录生成的测试卡号和密码

#### 2.2 配置数据库连接
在`src/main/resources/application.properties`文件中，配置数据库连接信息：

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=bank_db;encrypt=false;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=123456
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

### 3. 运行项目

#### 3.1 在IntelliJ IDEA中运行
1. 打开IDEA，导入项目根目录
2. 等待依赖下载完成
3. 找到`IronBankSystemApplication.java`主类
4. 右键点击，选择"Run 'IronBankSystemApplication'"
5. 应用启动后，自动打开浏览器访问`http://localhost:8080`

#### 3.2 使用Maven命令运行
```bash
# 构建项目
mvn clean package

# 运行项目
java -jar target/BankSystem-1.0.jar
```

## 使用说明

### 1. 新用户开户
1. 访问首页，点击"新用户开户"
2. 填写个人信息（姓名、身份证号、手机号、地址）
3. 设置6位支付密码
4. 选择初始存款金额
5. 提交表单，获取银行卡号

### 2. 账户查询
1. 访问首页，点击"已有账户"
2. 输入银行卡号
3. 点击"查询"，查看账户信息

### 3. 存款操作
1. 查询账户信息后，点击"存款"按钮
2. 输入存款金额
3. 点击"确认存款"
4. 查看存款结果和余额变化

### 4. 取款操作
1. 查询账户信息后，点击"取款"按钮
2. 输入取款金额和支付密码
3. 点击"确认取款"
4. 查看取款结果和余额变化

### 5. 转账操作
1. 查询账户信息后，点击"转账"按钮
2. 输入转入卡号、转账金额和支付密码
3. 点击"确认转账"
4. 查看转账结果和余额变化

### 6. 交易记录查询
1. 查询账户信息后，点击"交易记录"按钮
2. 输入支付密码进行验证
3. 查看交易记录列表

## 数据库设计

### 1. 客户表（Customers）
| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| customer_id | INT | 客户ID，自增主键 |
| name | NVARCHAR(50) | 客户姓名 |
| id_card | NVARCHAR(18) | 身份证号，唯一约束 |
| phone | NVARCHAR(20) | 手机号码 |
| address | NVARCHAR(200) | 联系地址 |
| created_date | DATETIME | 创建时间 |

### 2. 账户表（Accounts）
| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| card_number | NVARCHAR(19) | 银行卡号，主键 |
| customer_id | INT | 关联客户ID |
| balance | DECIMAL(15,2) | 账户余额 |
| status | NVARCHAR(20) | 账户状态 |
| open_date | DATE | 开户日期 |

### 3. 账户认证表（AccountAuth）
| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| card_number | NVARCHAR(19) | 银行卡号，主键 |
| pay_password | NVARCHAR(6) | 支付密码 |
| last_modified | DATETIME | 最后修改时间 |

### 4. 交易记录表（Transactions）
| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| transaction_id | BIGINT | 交易ID，自增主键 |
| card_number | NVARCHAR(19) | 银行卡号 |
| transaction_time | DATETIME | 交易时间 |
| transaction_type | NVARCHAR(20) | 交易类型 |
| amount | DECIMAL(15,2) | 交易金额 |
| balance_after | DECIMAL(15,2) | 交易后余额 |
| remarks | NVARCHAR(200) | 备注信息 |

## 系统架构

### 分层架构

```
┌─────────────────────────────────────────────────┐
│                   表现层 (UI)                   │
│  Thymeleaf模板 + Bootstrap + JavaScript + CSS   │
└─────────────────────────────────────────────────┘
                          │
┌─────────────────────────────────────────────────┐
│                控制器层 (Controller)            │
│      处理HTTP请求，调用业务逻辑，返回响应        │
└─────────────────────────────────────────────────┘
                          │
┌─────────────────────────────────────────────────┐
│                业务逻辑层 (Service)             │
│    实现核心业务功能，处理事务和业务规则          │
└─────────────────────────────────────────────────┘
                          │
┌─────────────────────────────────────────────────┐
│                数据访问层 (Mapper)              │
│     封装数据库操作，提供数据访问接口             │
└─────────────────────────────────────────────────┘
                          │
┌─────────────────────────────────────────────────┐
│                   数据库层 (DB)                 │
│                   SQL Server                    │
└─────────────────────────────────────────────────┘
```

### 核心流程

#### 转账流程
1. 验证支付密码
2. 验证转出账户存在性和余额充足性
3. 验证转入账户存在性
4. 执行转账操作（事务管理）
   - 减少转出账户余额
   - 增加转入账户余额
5. 记录双方交易记录
6. 返回操作结果

## 安全设计

### 1. 支付密码保护
- 所有资金变动操作都需要验证6位支付密码
- 支付密码存储在独立的表中，与账户信息分离
- 密码验证失败返回统一错误信息

### 2. 事务管理
- 使用Spring声明式事务（@Transactional注解）
- 确保关键操作（如转账）的原子性
- 自动处理事务的提交和回滚

### 3. 输入验证
- 前端表单验证，防止无效输入
- 后端再次验证，确保数据完整性

### 4. 错误处理
- 统一的错误处理机制
- 友好的错误提示，不泄露系统内部信息
- 详细的日志记录

## 性能优化

### 1. 数据库优化
- 关键字段创建索引
- 使用JOIN查询减少数据库访问次数
- 避免全表扫描

### 2. 连接池配置
- 配置HikariCP连接池
- 设置合理的连接池参数
  - 最大连接数：20
  - 最小空闲连接数：5
  - 连接超时时间：30秒

### 3. 前端优化
- 响应式设计，适配不同屏幕尺寸
- 使用CDN加载外部资源
- 优化CSS和JavaScript代码

## 开发指南

### 1. 代码规范
- 遵循Java编码规范
- 使用Lombok简化代码
- 方法和变量命名清晰，遵循驼峰命名法
- 添加必要的注释

### 2. 调试模式
- 在IDEA中使用调试模式运行项目
- 设置断点，查看变量值和执行流程
- 使用日志输出调试信息

### 3. 测试
- 编写单元测试和集成测试
- 测试覆盖率目标：80%
- 使用Spring Boot Test进行测试

### 4. 部署
- 构建可执行jar文件
- 部署到服务器
- 配置反向代理（如Nginx）
- 设置监控和日志收集

## 常见问题与解决方案

### 1. 数据库连接失败
**问题**：应用启动时无法连接到数据库
**解决方案**：
- 确保SQL Server服务正在运行
- 检查数据库连接配置是否正确
- 确保数据库已创建
- 检查SQL Server是否允许远程连接

### 2. 端口被占用
**问题**：8080端口被其他应用占用
**解决方案**：
- 修改`application.properties`中的`server.port`属性
- 关闭占用8080端口的应用

### 3. 依赖下载失败
**问题**：Maven依赖下载失败
**解决方案**：
- 检查网络连接
- 点击IDEA中Maven工具栏的"Reload All Maven Projects"
- 配置国内Maven镜像源

### 4. 支付密码错误
**问题**：支付密码验证失败
**解决方案**：
- 确保输入的密码是6位数字
- 检查密码是否正确
- 联系管理员重置密码

## 项目亮点

1. **现代化技术栈**：采用Spring Boot 3.x和JDK 17，符合最新技术趋势
2. **分层架构设计**：清晰的分层结构，便于维护和扩展
3. **完善的功能实现**：覆盖银行核心业务功能
4. **安全可靠**：完善的安全机制，确保资金安全
5. **友好的用户界面**：现代化的UI设计，良好的用户体验
6. **详细的文档**：提供完整的功能文档、配置指南和运行指南
7. **易于扩展**：模块化设计，便于添加新功能

## 扩展建议

1. 添加用户登录功能，支持多用户管理
2. 实现更复杂的业务规则，如手续费计算
3. 添加账户冻结/解冻功能
4. 实现短信验证码验证
5. 添加数据统计和报表功能
6. 支持更多支付方式
7. 实现移动端适配
8. 添加API接口，支持第三方集成

## 许可证

本项目采用MIT许可证，详见LICENSE文件。


## 更新日志

### v1.0.0 (2026-01-05)
- ✅ 初始版本发布
- ✅ 实现账户开户功能
- ✅ 实现存款、取款、转账功能
- ✅ 实现交易记录查询功能
- ✅ 完善安全机制
- ✅ 编写详细文档

---

**铁金库银行系统** - 安全 · 便捷 · 专业

© 2026 铁金库银行系统
