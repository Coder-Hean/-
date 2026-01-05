package com.shop.bank.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户信息实体类
 */
@Data
public class Customer {
    /**
     * 客户ID（主键）
     */
    private Integer customerId;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;
}