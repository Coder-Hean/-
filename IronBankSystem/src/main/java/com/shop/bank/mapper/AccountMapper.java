package com.shop.bank.mapper;

import com.shop.bank.pojo.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    /**
     * 根据银行卡号查询账户信息
     */
    Account selectByCardNumber(String cardNumber);

    /**
     * 插入新账户（开户）
     */
    void insertAccount(Account account);

    /**
     * 更新账户余额（用于存款、取款）
     * 注意：amount 可正可负（存款为正，取款为负）
     */
    void updateBalanceByCardNumber(@Param("cardNumber") String cardNumber, @Param("amount") double amount);

    /**
     * 查询账户是否存在（用于验证）
     */
    boolean existsByCardNumber(String cardNumber);
}