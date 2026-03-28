package BankingApplicationConsoleBased.app;

import BankingApplicationConsoleBased.domain.Account;
import BankingApplicationConsoleBased.domain.Customer;
import BankingApplicationConsoleBased.service.BankService;
import BankingApplicationConsoleBased.service.impl.BankServiceImpl;

import java.util.Scanner;

public class
Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        boolean running = true;
        System.out.println("----------WELCOME TO MY BANK----------");
        while (running) {
            System.out.println("""
                    1. Open Account
                    2. Deposit
                    3. Withdraw
                    4. Transfer
                    5. Account Statement
                    6. List Accounts
                    7. Search Account by Account Number
                    8. Exit
                    """);
            System.out.print("\t\tEnter Your Choice :");
            short ch = scanner.nextShort();
            scanner.nextLine();

            switch (ch) {
                case 1:
                    openAccount(scanner, bankService);
                    break;
                case 2:
                    deposit(scanner, bankService);
                    break;
                case 3:
                    withdraw(scanner, bankService);
                    break;
                case 4:
                    transfer(scanner, bankService);
                    break;
                case 5:
                    statement(scanner, bankService);
                    break;
                case 6:
                    listAccounts(scanner, bankService);
                    break;
                case 7:
                    searchAccounts(scanner, bankService);
                    break;
                case 8:
                    running = false;
                    break;

            }
        }
    }

    private static void openAccount(Scanner scanner, BankService bankService) {
        System.out.print("\nCustomer Name :");
        String name = scanner.nextLine().trim();
        System.out.print("\nCustomer Email :");
        String email = scanner.nextLine().trim();
        System.out.print("\nAccount Type (SAVINGS/CURRENT) :");
        String type = scanner.nextLine().trim();
        System.out.print("\nInitial Deposit :");
        double initialDeposit = scanner.nextDouble();
        System.out.println("Account opened Successfully : " + bankService.openAccount(name, email, Double.isNaN(initialDeposit) ?0:initialDeposit, type.toUpperCase()));
    }

    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.print("\nAccount Number :");
        String accountNumber = scanner.nextLine().trim();
        System.out.print("\nAmount :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("\nAny Note for this deposit :");
        String note = scanner.nextLine().trim();
        bankService.deposit(accountNumber, amount, note.isEmpty() ? "Deposit" : note);
        System.out.println("Amount Deposit Successfully : " + amount);
    }

    private static void withdraw(Scanner scanner, BankService bankService) {
        System.out.print("\nAccount Number :");
        String accountNumber = scanner.nextLine().trim();
        System.out.print("\nAmount :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("\nAny Note for this withdraw :");
        String note = scanner.nextLine().trim();
        bankService.withdraw(accountNumber, amount, note.isEmpty() ? "Withdraw" : note);
        System.out.println("Amount Withdrawal Successfully : " + amount);

    }

    private static void transfer(Scanner scanner, BankService bankService) {
        System.out.print("\nFrom Account Number :");
        String from = scanner.nextLine().trim();
        System.out.print("\nTo Account Number :");
        String to = scanner.nextLine().trim();
        System.out.print("\nAmount :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("\nAny Note for this transaction :");
        String note = scanner.nextLine().trim();
        bankService.transfer(from, to, amount, note.isEmpty() ? "Transfer" : note);
    }

    private static void statement(Scanner scanner, BankService bankService) {
        System.out.print("\nAccount Number :");
        String accountNumber = scanner.nextLine().trim();
        bankService.getStatement(accountNumber).forEach((t) -> {
            System.out.println(t.getTimeStamp() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote());
        });
    }

    private static void listAccounts(Scanner scanner, BankService bankService) {
        bankService.listAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });
    }

    private static void searchAccounts(Scanner scanner, BankService bankService) {
        System.out.print("\nAccount Number :");
        String accountNumber = scanner.nextLine().trim();
        Account acc = bankService.searchAccount(accountNumber);
        Customer customer = bankService.searchCustomer(acc.getCustomerId());

        System.out.println("\nCustomer Name :" + customer.getName());
        System.out.println("Account Number :" + acc.getAccountNumber());
        System.out.println("Customer Email :" + customer.getEmail());
        System.out.println("Account Type :" + acc.getAccountType());
        System.out.println("Total Balance :" + acc.getBalance() + "\n");
    }


}
