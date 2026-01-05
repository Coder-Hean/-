// 文件路径: src/main/java/com/shop/bank/service/BankService.java

package com.shop.bank.service;

import com.shop.bank.pojo.Account;
import com.shop.bank.pojo.Transaction;

import java.util.List;

public interface BankService {

    String generateCardNumber();

    String openAccount(String name, String idCard, String phone, String payPassword, String address,double initialBalance);

    Account queryAccount(String cardNumber);

    boolean verifyPayPassword(String cardNumber, String inputPassword);

    List<Transaction> getTransactions(String cardNumber);

    boolean withdraw(String cardNumber, double amount, String password);

    boolean deposit(String cardNumber, double amount);

    boolean transfer(String fromCardNumber, String toCardNumber, double amount, String password);
}