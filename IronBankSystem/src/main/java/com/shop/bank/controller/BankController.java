package com.shop.bank.controller;

import com.shop.bank.pojo.Account;
import com.shop.bank.pojo.Transaction;
import com.shop.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/open")
    public String open() {
        return "open";
    }

    @PostMapping("/openAccount")
    public String openAccount(@RequestParam Map<String, String> form, Model model) {
        try {
            // 获取初始余额，如果没有提供则使用默认值200
            double initialBalance = 0;
            try {
                initialBalance = Double.parseDouble(form.get("initialAmount"));
            } catch (Exception e) {
                // 使用默认值0
            }

            String cardNumber = bankService.openAccount(
                    form.get("name"),
                    form.get("idCard"),
                    form.get("phone"),
                    form.get("payPassword"),
                    form.get("address"),
                    initialBalance  // 添加第6个参数
            );

            // 查询开户后的账户信息并传入页面
            Account account = bankService.queryAccount(cardNumber);
            model.addAttribute("account", account);
            model.addAttribute("message", "✅ 开户成功！您的银行卡号：<br><strong>" + cardNumber + "</strong>");
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE KEY") || e.getMessage().contains("重复键")) {
                model.addAttribute("message", "❌ 开户失败：该身份证号已存在，不能重复开户");
            } else {
                model.addAttribute("message", "❌ 开户失败：" + e.getMessage());
            }
        }
        return "result";
    }

    @GetMapping("/account")
    public String accountPage() {
        return "account";
    }

    // --- 以下为 AJAX API 接口 ---
    @PostMapping("/queryAccount")
    public String queryAccount(@RequestParam String cardNumber, Model model) {
        try {
            Account account = bankService.queryAccount(cardNumber);
            if (account != null) {
                model.addAttribute("account", account);
            } else {
                model.addAttribute("errorMessage", "❌ 未找到该银行卡号的账户信息");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "❌ 查询失败：" + e.getMessage());
        }
        return "account";
    }
    // 显示存款表单页面
    @GetMapping("/showDeposit")
    public String showDeposit(@RequestParam String cardNumber, Model model) {
        try {
            Account account = bankService.queryAccount(cardNumber);
            if (account != null) {
                model.addAttribute("account", account);
                model.addAttribute("depositForm", true);
            } else {
                model.addAttribute("errorMessage", "❌ 未找到该银行卡号的账户信息");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "❌ 查询失败：" + e.getMessage());
        }
        return "account";
    }

    // 显示取款表单页面
    @GetMapping("/showWithdraw")
    public String showWithdraw(@RequestParam String cardNumber, Model model) {
        try {
            Account account = bankService.queryAccount(cardNumber);
            if (account != null) {
                model.addAttribute("account", account);
                model.addAttribute("withdrawForm", true);
            } else {
                model.addAttribute("errorMessage", "❌ 未找到该银行卡号的账户信息");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "❌ 查询失败：" + e.getMessage());
        }
        return "account";
    }

    // 处理存款操作
    @PostMapping("/deposit")
    public String deposit(@RequestParam String cardNumber, @RequestParam double amount, Model model) {
        try {
            boolean success = bankService.deposit(cardNumber, amount);
            if (success) {
                Account account = bankService.queryAccount(cardNumber);
                model.addAttribute("account", account);
                model.addAttribute("depositMessage", "✅ 存款成功！存入金额：" + amount + "元");
                model.addAttribute("depositSuccess", true);
            } else {
                model.addAttribute("depositMessage", "❌ 存款失败，请稍后重试");
                model.addAttribute("depositSuccess", false);
            }
        } catch (Exception e) {
            model.addAttribute("depositMessage", "❌ 存款失败：" + e.getMessage());
            model.addAttribute("depositSuccess", false);
        }
        return "account";
    }
    //转账
    @GetMapping("/showTransfer")
    public String showTransfer(@RequestParam String cardNumber, Model model) {
        try {
            Account account = bankService.queryAccount(cardNumber);
            if (account != null) {
                model.addAttribute("account", account);
                model.addAttribute("transferForm", true);
            } else {
                model.addAttribute("errorMessage", "❌ 未找到该银行卡号的账户信息");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "❌ 查询失败：" + e.getMessage());
        }
        return "account";
    }
    // 处理取款操作
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String cardNumber, @RequestParam double amount,
                           @RequestParam String password, Model model) {
        try {
            boolean success = bankService.withdraw(cardNumber, amount, password);
            if (success) {
                Account account = bankService.queryAccount(cardNumber);
                model.addAttribute("account", account);
                model.addAttribute("withdrawMessage", "✅ 取款成功！取出金额：" + amount + "元");
                model.addAttribute("withdrawSuccess", true);
            } else {
                model.addAttribute("withdrawMessage", "❌ 取款失败，请检查密码或余额");
                model.addAttribute("withdrawSuccess", false);
            }
        } catch (Exception e) {
            model.addAttribute("withdrawMessage", "❌ 取款失败：" + e.getMessage());
            model.addAttribute("withdrawSuccess", false);
        }
        return "account";
    }
    // 处理转账操作
    @PostMapping("/transfer")
    public String transfer(@RequestParam String cardNumber, @RequestParam String toCardNumber,
                           @RequestParam double amount, @RequestParam String password, Model model) {
        try {
            boolean success = bankService.transfer(cardNumber, toCardNumber, amount, password);
            if (success) {
                Account account = bankService.queryAccount(cardNumber);
                model.addAttribute("account", account);
                model.addAttribute("transferMessage", "✅ 转账成功！转出金额：" + amount + "元");
                model.addAttribute("transferSuccess", true);
            } else {
                model.addAttribute("transferMessage", "❌ 转账失败，请检查对方卡号、密码或余额");
                model.addAttribute("transferSuccess", false);
            }
        } catch (Exception e) {
            model.addAttribute("transferMessage", "❌ 转账失败：" + e.getMessage());
            model.addAttribute("transferSuccess", false);
        }
        return "account";
    }
    //验证查询交易记录密码
    @GetMapping("/showTransactionPassword")
    public String showTransactionPassword(@RequestParam String cardNumber, Model model) {
        if (cardNumber != null && !cardNumber.trim().isEmpty()) {
            try {
                Account account = bankService.queryAccount(cardNumber);
                if (account != null) {
                    model.addAttribute("account", account);
                    model.addAttribute("transactionPasswordForm", true);
                } else {
                    model.addAttribute("errorMessage", "❌ 未找到该银行卡号的账户信息");
                }
            } catch (Exception e) {
                model.addAttribute("errorMessage", "❌ 查询失败：" + e.getMessage());
            }
        } else {
            model.addAttribute("transactionPasswordForm", true);
        }
        return "account";
    }
    @PostMapping("/verifyTransactionPassword")
    public String verifyTransactionPassword(@RequestParam String cardNumber,
                                            @RequestParam String password, Model model) {
        try {
            boolean success = bankService.verifyPayPassword(cardNumber, password);
            if (success) {
                // 密码验证成功，跳转到交易记录页面
                return "redirect:/transactions?cardNumber=" + cardNumber;
            } else {
                model.addAttribute("transactionErrorMessage", "❌ 密码错误，请重新输入");
                model.addAttribute("transactionPasswordForm", true);
                Account account = bankService.queryAccount(cardNumber);
                if (account != null) {
                    model.addAttribute("account", account);
                }
            }
        } catch (Exception e) {
            model.addAttribute("transactionErrorMessage", "❌ 验证失败：" + e.getMessage());
            model.addAttribute("transactionPasswordForm", true);
        }
        return "account";
    }
    @GetMapping("/transactions")
    public String showTransactions(@RequestParam String cardNumber, Model model) {
        try {
            List<Transaction> transactions = bankService.getTransactions(cardNumber);
            model.addAttribute("transactions", transactions);
            model.addAttribute("cardNumber", cardNumber);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "❌ 获取交易记录失败：" + e.getMessage());
        }
        return "transactions";
    }
    @PostMapping("/api/queryAccount")
    @ResponseBody
    public Account queryAccount(@RequestBody Map<String, String> request) {
        // 前端传的是 "accountNo"，我们当作 cardNumber 使用
        return bankService.queryAccount(request.get("accountNo"));
    }

    @PostMapping("/api/verifyPassword")
    @ResponseBody
    public boolean verifyPassword(@RequestBody Map<String, String> request) {
        return bankService.verifyPayPassword(request.get("accountNo"), request.get("password"));
    }

    @PostMapping("/api/getTransactions")
    @ResponseBody
    public List<Transaction> getTransactions(@RequestBody Map<String, String> request) {
        return bankService.getTransactions(request.get("accountNo"));
    }

    @PostMapping("/api/deposit")
    @ResponseBody
    public boolean deposit(@RequestBody Map<String, String> request) {
        double amount = Double.parseDouble(request.get("amount"));
        return bankService.deposit(request.get("accountNo"), amount);
    }

    @PostMapping("/api/withdraw")
    @ResponseBody
    public boolean withdraw(@RequestBody Map<String, String> request) {
        double amount = Double.parseDouble(request.get("amount"));
        return bankService.withdraw(request.get("accountNo"), amount, request.get("password"));
    }
}