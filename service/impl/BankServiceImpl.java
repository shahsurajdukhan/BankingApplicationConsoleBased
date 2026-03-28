package BankingApplicationConsoleBased.service.impl;

import BankingApplicationConsoleBased.domain.Account;
import BankingApplicationConsoleBased.domain.Customer;
import BankingApplicationConsoleBased.domain.Transaction;
import BankingApplicationConsoleBased.domain.Type;
import BankingApplicationConsoleBased.exception.AccountNotFoundException;
import BankingApplicationConsoleBased.exception.InsufficientBalanceException;
import BankingApplicationConsoleBased.exception.ValidationException;
import BankingApplicationConsoleBased.repository.AccountRepository;
import BankingApplicationConsoleBased.repository.CustomerRepository;
import BankingApplicationConsoleBased.repository.TransactionRepository;
import BankingApplicationConsoleBased.service.BankService;
import BankingApplicationConsoleBased.util.Validation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    static int acc = 0;


    private final Validation<String> validateName = name -> {
        if (name == null || name.isBlank()) throw new ValidationException("Name is Required");
    };
    private final Validation<String> validateEmail = email -> {
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains("."))
            throw new ValidationException("Valid Email is Required");
    };
    private final Validation<String> validateType = type -> {
        if (type == null || type.isBlank() || (!type.equalsIgnoreCase("SAVINGS") && !type.equalsIgnoreCase("CURRENT")))
            throw new ValidationException("Account Type must be SAVINGS or CURRENT");
    };
    private final Validation<Double> validateAmount = amount -> {
        if (amount == null || amount < 0) throw new ValidationException("Please Enter a valid Amount");
    };
    private final Validation<String> validateAccountNumber = accountNumber -> {
        if (accountNumber == null || accountNumber.length() != 10)
            throw new ValidationException("Please Enter a valid Account Number");
    };

    @Override
    public String openAccount(String name, String email, Double initialDeposit, String accountType) {
        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);
        String customerId = UUID.randomUUID().toString();
        String accountNumber = String.format("AC%08d", ++acc);

        Account account = new Account(accountNumber, customerId, initialDeposit, accountType);
        Customer customer = new Customer(customerId, name, email);

        accountRepository.save(account);
        customerRepository.save(customer);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream().sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String s) {
        validateAmount.validate(amount);
        validateAccountNumber.validate(accountNumber);
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found :" + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.DEPOSIT, account.getAccountNumber(), amount, LocalDateTime.now(), s);
        transactionRepository.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String s) {
        validateAmount.validate(amount);
        validateAccountNumber.validate(accountNumber);
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found :" + accountNumber));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.WITHDRAW, account.getAccountNumber(), amount, LocalDateTime.now(), s);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String s) {
        validateAmount.validate(amount);
        validateAccountNumber.validate(fromAcc);
        validateAccountNumber.validate(toAcc);
        if (fromAcc.equals(toAcc)) {
            throw new ValidationException("Cannot Transfer To Your Own Account");
        }
        Account from = accountRepository.findByNumber(fromAcc).orElseThrow(() -> new AccountNotFoundException("Account Not Found :" + fromAcc));

        Account to = accountRepository.findByNumber(toAcc).orElseThrow(() -> new AccountNotFoundException("Account Not Found :" + toAcc));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        from.setBalance(from.getBalance() - amount);

        Transaction fromTransaction = new Transaction(UUID.randomUUID().toString(), Type.TRANSFER_OUT, from.getAccountNumber(), amount, LocalDateTime.now(), s);
        transactionRepository.add(fromTransaction);

        to.setBalance(to.getBalance() + amount);

        Transaction toTransaction = new Transaction(UUID.randomUUID().toString(), Type.TRANSFER_IN, to.getAccountNumber(), amount, LocalDateTime.now(), s);
        transactionRepository.add(toTransaction);
    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {
        validateAccountNumber.validate(accountNumber);
        return transactionRepository.findByAccount(accountNumber).stream().sorted(Comparator.comparing(Transaction::getTimeStamp)).collect(Collectors.toList());
    }

    @Override
    public Account searchAccount(String accountNumber) {
        validateAccountNumber.validate(accountNumber);
        return accountRepository.findByNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found :" + accountNumber));
    }

    @Override
    public Customer searchCustomer(String customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new AccountNotFoundException("Customer Not Found :" + customerId));
    }
}

