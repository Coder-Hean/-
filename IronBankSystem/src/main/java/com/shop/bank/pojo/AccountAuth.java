package com.shop.bank.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 账户认证信息（支付密码等）
 */
@Data
public class AccountAuth {
    /**
     * 银行卡号（关联 Account.cardNumber）
     */
    private String cardNumber;

    /**
     * 支付密码（6位数字字符串）
     */
    private String payPassword;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModified;
}