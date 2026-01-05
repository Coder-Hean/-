package com.shop.bank.service.impl;

import com.shop.bank.mapper.AccountMapper;
import com.shop.bank.pojo.Account;
import com.shop.bank.pojo.Transaction;
import com.shop.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String generateCardNumber() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder("622208");
        for (int i = 0; i < 13; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public String openAccount(String name, String idCard, String phone, String payPassword, String address,double initialBalance) {
        // 1. 检查身份证号是否已存在
        Integer existingCustomerId = null;
        try {
            existingCustomerId = jdbcTemplate.queryForObject(
                    "SELECT customer_id FROM Customers WHERE id_card = ?",
                    Integer.class, idCard
            );
        } catch (Exception e) {
            // 身份证号不存在，继续开户流程
        }

        Integer customerId;
        if (existingCustomerId != null) {
            // 身份证号已存在，使用现有客户ID
            customerId = existingCustomerId;
        } else {
            // 2. 插入新客户
            jdbcTemplate.update(
                    "INSERT INTO Customers (name, id_card, phone, address, created_date) VALUES (?, ?, ?, ?, ?)",
                    name, idCard, phone, address, LocalDateTime.now()
            );

            customerId = jdbcTemplate.queryForObject(
                    "SELECT customer_id FROM Customers WHERE id_card = ?", Integer.class, idCard
            );
        }

        // 3. 生成卡号
        String cardNumber = generateCardNumber();

        // 4. 插入账户（使用 MyBatis）
        Account account = new Account();
        account.setCardNumber(cardNumber);
        account.setCustomerId(customerId);
        account.setBalance(initialBalance);
        account.setStatus("active");
        account.setOpenDate(LocalDateTime.now().toLocalDate());
        accountMapper.insertAccount(account);

        // 5. 插入支付密码
        jdbcTemplate.update(
                "INSERT INTO AccountAuth (card_number, pay_password, last_modified) VALUES (?, ?, ?)",
                cardNumber, payPassword, LocalDateTime.now()
        );

        return cardNumber;
    }

    @Override
    public Account queryAccount(String cardNumber) {
        return accountMapper.selectByCardNumber(cardNumber);
    }

    @Override
    public boolean verifyPayPassword(String cardNumber, String inputPassword) {
        try {
            String stored = jdbcTemplate.queryForObject(
                    "SELECT pay_password FROM AccountAuth WHERE card_number = ?",
                    String.class, cardNumber
            );
            return stored != null && stored.equals(inputPassword);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Transaction> getTransactions(String cardNumber) {
        return jdbcTemplate.query(
                "SELECT card_number, transaction_time, transaction_type, amount, balance_after, remarks " +
                        "FROM Transactions WHERE card_number = ? ORDER BY transaction_time DESC",
                (rs, rowNum) -> {
                    Transaction t = new Transaction();
                    t.setCardNumber(rs.getString("card_number"));
                    t.setTransactionTime(rs.getObject("transaction_time", LocalDateTime.class));
                    t.setTransactionType(rs.getString("transaction_type"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setBalanceAfter(rs.getDouble("balance_after"));
                    t.setRemarks(rs.getString("remarks"));
                    return t;
                },
                cardNumber
        );
    }
    @Override
    @Transactional
    public boolean transfer(String fromCardNumber, String toCardNumber, double amount, String password) {
        // 1. 验证支付密码
        if (!verifyPayPassword(fromCardNumber, password)) return false;

        // 2. 检查转出账户是否存在且余额充足
        Account fromAccount = queryAccount(fromCardNumber);
        if (fromAccount == null || fromAccount.getBalance() < amount) return false;

        // 3. 检查转入账户是否存在
        Account toAccount = queryAccount(toCardNumber);
        if (toAccount == null) return false;

        // 4. 执行转账（原子操作）
        // 减少转出账户余额
        accountMapper.updateBalanceByCardNumber(fromCardNumber, -amount);
        // 增加转入账户余额
        accountMapper.updateBalanceByCardNumber(toCardNumber, amount);

        // 5. 记录交易记录
        recordTransaction(fromCardNumber, "TRANSFER", -amount, fromAccount.getBalance() - amount,
                "转账至" + toCardNumber);
        recordTransaction(toCardNumber, "TRANSFER", amount, toAccount.getBalance() + amount,
                "收到来自" + fromCardNumber + "的转账");

        return true;
    }
    @Override
    @Transactional
    public boolean withdraw(String cardNumber, double amount, String password) {
        if (!verifyPayPassword(cardNumber, password)) return false;
        Account acc = queryAccount(cardNumber);
        if (acc == null || acc.getBalance() < amount) return false;

        // 更新余额（MyBatis）
        accountMapper.updateBalanceByCardNumber(cardNumber, -amount);

        // 记录交易
        recordTransaction(cardNumber, "WITHDRAW", amount, acc.getBalance() - amount, "取款");
        return true;
    }

    @Override
    @Transactional
    public boolean deposit(String cardNumber, double amount) {
        Account acc = queryAccount(cardNumber);
        if (acc == null) return false;

        // 更新余额（MyBatis）
        accountMapper.updateBalanceByCardNumber(cardNumber, amount);

        // 记录交易
        recordTransaction(cardNumber, "DEPOSIT", amount, acc.getBalance() + amount, "存款");
        return true;
    }

    private void recordTransaction(String cardNumber, String type, double amount, double balanceAfter, String remarks) {
        jdbcTemplate.update(
                "INSERT INTO Transactions (card_number, transaction_time, transaction_type, amount, balance_after, remarks) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                cardNumber, LocalDateTime.now(), type, amount, balanceAfter, remarks
        );
    }
}