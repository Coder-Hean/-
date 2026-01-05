-- =============================================
-- 步骤1：判断并创建bank_db数据库（SQL Server专用语法）
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'bank_db')
BEGIN
    CREATE DATABASE [bank_db]
    ON PRIMARY (
        NAME = N'bank_db',
        FILENAME = N'D:\Tools\ssms\MSSQL16.MSSQLSERVER\MSSQL\DATA\bank_db.mdf', -- 替换为实际路径
        SIZE = 8192KB,
        MAXSIZE = UNLIMITED,
        FILEGROWTH = 65536KB
    )
    LOG ON (
        NAME = N'bank_db_log',
        FILENAME = N'D:\Tools\ssms\MSSQL16.MSSQLSERVER\MSSQL\DATA\bank_db_log.ldf', -- 替换为实际路径
        SIZE = 8192KB,
        MAXSIZE = 2048GB,
        FILEGROWTH = 65536KB
    )
END
GO

-- 切换到bank_db数据库
USE [bank_db]
GO

-- =============================================
-- 步骤2：判断并创建表（确保表结构完整）
-- =============================================
-- 创建客户表
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Customers')
BEGIN
    CREATE TABLE [dbo].[Customers] (
        customer_id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(50) NOT NULL,
        id_card NVARCHAR(18) NOT NULL UNIQUE,
        phone NVARCHAR(20) NOT NULL,
        address NVARCHAR(200) NOT NULL,
        created_date DATETIME NOT NULL DEFAULT GETDATE()
    )
END
GO

-- 创建账户表
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Accounts')
BEGIN
    CREATE TABLE [dbo].[Accounts] (
        card_number NVARCHAR(19) PRIMARY KEY,
        customer_id INT NOT NULL,
        balance DECIMAL(15, 2) NOT NULL DEFAULT 0,
        status NVARCHAR(20) NOT NULL DEFAULT 'active',
        open_date DATE NOT NULL,
        FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
    )
END
GO

-- 创建账户认证表
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'AccountAuth')
BEGIN
    CREATE TABLE [dbo].[AccountAuth] (
        card_number NVARCHAR(19) PRIMARY KEY,
        pay_password NVARCHAR(6) NOT NULL,
        last_modified DATETIME NOT NULL DEFAULT GETDATE(),
        FOREIGN KEY (card_number) REFERENCES Accounts(card_number)
    )
END
GO

-- 创建交易记录表
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Transactions')
BEGIN
    CREATE TABLE [dbo].[Transactions] (
        transaction_id BIGINT IDENTITY(1,1) PRIMARY KEY,
        card_number NVARCHAR(19) NOT NULL,
        transaction_time DATETIME NOT NULL DEFAULT GETDATE(),
        transaction_type NVARCHAR(20) NOT NULL,
        amount DECIMAL(15, 2) NOT NULL,
        balance_after DECIMAL(15, 2) NOT NULL,
        remarks NVARCHAR(200),
        FOREIGN KEY (card_number) REFERENCES Accounts(card_number)
    )
END
GO

-- =============================================
-- 步骤3：创建索引（避免重复创建）
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_accounts_customer_id')
BEGIN
    CREATE INDEX idx_accounts_customer_id ON Accounts(customer_id)
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_transactions_card_number')
BEGIN
    CREATE INDEX idx_transactions_card_number ON Transactions(card_number)
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_transactions_time')
BEGIN
    CREATE INDEX idx_transactions_time ON Transactions(transaction_time)
END
GO

-- =============================================
-- 步骤4：插入测试数据（确保外键、变量、长度均合法）
-- =============================================
-- 插入测试客户（确保唯一）
IF NOT EXISTS (SELECT * FROM Customers WHERE id_card = '110101199001011234')
BEGIN
    INSERT INTO Customers (name, id_card, phone, address, created_date)
    VALUES ('张三', '110101199001011234', '13800138000', '北京市朝阳区', GETDATE())
END
GO

-- 生成测试卡号+插入数据（同一批处理，避免变量失效）
DECLARE 
    @test_card_number NVARCHAR(19),
    @customer_id INT = 1 -- 对应测试客户的customer_id

-- 生成19位卡号（622208前缀+13位随机串，避免长度溢出）
SET @test_card_number = '622208' + LEFT(REPLACE(NEWID(), '-', ''), 13)

-- 插入测试账户（确保外键关联客户，且卡号唯一）
IF NOT EXISTS (SELECT * FROM Accounts WHERE customer_id = @customer_id)
BEGIN
    INSERT INTO Accounts (card_number, customer_id, balance, status, open_date)
    VALUES (@test_card_number, @customer_id, 1000.00, 'active', GETDATE())
END

-- 插入测试账户认证（依赖Accounts表的卡号）
IF NOT EXISTS (SELECT * FROM AccountAuth WHERE card_number = @test_card_number)
BEGIN
    INSERT INTO AccountAuth (card_number, pay_password, last_modified)
    VALUES (@test_card_number, '123456', GETDATE())
END

-- 插入测试交易记录（依赖Accounts表的卡号）
IF NOT EXISTS (SELECT * FROM Transactions WHERE remarks = '初始存款' AND card_number = @test_card_number)
BEGIN
    INSERT INTO Transactions (card_number, transaction_time, transaction_type, amount, balance_after, remarks)
    VALUES (@test_card_number, GETDATE(), 'DEPOSIT', 1000.00, 1000.00, '初始存款')
END

-- 输出结果
PRINT '数据库创建完成！';
PRINT '测试卡号：' + @test_card_number;
PRINT '支付密码：123456';