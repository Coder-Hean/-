package com.shop.bank.pojo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 银行账户实体类
 */
@Data
public class Account {
    private String cardNumber;
    private Integer customerId;
    private String name;  // 添加客户姓名属性
    private Double balance;
    private String status;
    private LocalDate openDate;
}