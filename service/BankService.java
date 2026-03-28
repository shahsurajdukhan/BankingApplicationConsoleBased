package BankingApplicationConsoleBased.service;

import BankingApplicationConsoleBased.domain.Account;
import BankingApplicationConsoleBased.domain.Customer;
import BankingApplicationConsoleBased.domain.Transaction;

import java.util.List;
import java.util.Map;


public interface BankService {
    String openAccount(String name , String email,Double initialDeposit ,String accountType);

    List<Account> listAccounts();

    void deposit(String accountNumber, Double amount, String s);

    void withdraw(String accountNumber, Double amount, String s);

    void transfer(String from, String to, Double amount, String s);

    List<Transaction> getStatement(String accountNumber);

    Account searchAccount(String accountNumber);

    Customer searchCustomer(String customerId);
}
