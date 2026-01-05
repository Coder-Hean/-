package com.shop.bank.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交易记录实体类
 */
@Data
public class Transaction {
    /**
     * 银行卡号（关联 Account.cardNumber）
     */
    private String cardNumber;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 交易类型（如 "DEPOSIT", "WITHDRAW", "TRANSFER"）
     */
    private String transactionType;

    /**
     * 交易金额（正数表示存入，负数表示支出）
     */
    private Double amount;

    /**
     * 交易后账户余额
     */
    private Double balanceAfter;

    /**
     * 备注信息
     */
    private String remarks;
}