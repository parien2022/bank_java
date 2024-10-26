package org.ftm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class FinancialTransactionManager {

    //Path for the accounts file
    private String ACCOUNTS_INPUT_FILE = "src/main/resources/accounts/accounts.json";
    //Path for the output log file
    private String TRANSACTIONS_OUTPUT_FILE = "src/main/resources/output_transactions/transactions.json";

    //Map that contains the key account number and the value as an Account type object
    private Map<String, Account> accounts = new HashMap<>();

    //List of transaction output to write into json file
    private List<Map<String, Object>> transactions = new ArrayList<>();

    //Instance of the gson builder to handle json data
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //main FTM method
    public void main() throws FileNotFoundException {

        String[] options = {"Create Account", "Deposit", "Withdraw", "Transfer", "Display Accounts", "Save and Exit"};
        loadAccounts();

        //Instance of the scanner for user inout along the program
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        // Program options loop
        while (!choice.equals("6")){
            System.out.println();
            for (int i = 0; i < options.length; i++){
                System.out.println(i + 1 + ". " + options[i]);
            }
            System.out.println("Select an option: ");
            choice = scanner.nextLine();
            System.out.println(choice);

            switch(choice){
                case "1":
                    createAccount(scanner);
                    break;
                case "2":
                    deposit(scanner);
                    break;
                case "3":
                    withdraw(scanner);
                    break;
                case "4":
                    transfer(scanner);
                    break;
                case "5":
                    displayAccounts();
                    break;
                case "6":
                    saveAccounts();
                    saveTransactions();
                    System.out.println("Saving account and exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Try Again.");
            }
        }
    }

    //Creates an account and adds it to the accounts Map with an initial balance
    private void createAccount(Scanner scanner){
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter initial balance: ");
        double initialBalance = Double.parseDouble(scanner.nextLine());
        accounts.put(accountNumber, new Account(accountNumber, initialBalance));
    }

    // Deposits a specific amount in the entered account
    private void deposit(Scanner scanner){
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);

        if(account != null){
            System.out.println("Enter amount to deposit: ");
            double depositAmount = Double.parseDouble(scanner.nextLine());
            account.deposit(depositAmount);
            logTransaction("Deposit", accountNumber, depositAmount);
        } else {
            System.out.println("Account not found");
        }

    }

    // Withdraws a specific amount from the entered account
    private void withdraw(Scanner scanner){
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);

        if(account != null){
            System.out.println("Enter amount to withdraw: ");
            double withdrawAmount = Double.parseDouble(scanner.nextLine());

            if(account.withdraw(withdrawAmount)){
                logTransaction("Withdrawal", accountNumber, withdrawAmount);
            } else {
                System.out.println("Insufficient funds");
            }
        } else {
            System.out.println("Account not found");
        }
    }

    //Transfers an amount from the source account to the destination account
    //Uses withdraw and deposit to accomplish the task
    private void transfer(Scanner scanner){
        System.out.println("Enter source account number: ");
        String sourceAccountNumber = scanner.nextLine();
        System.out.println("Enter destination account number: ");
        String destinationAccountNumber = scanner.nextLine();

        Account sourceAccount = accounts.get(sourceAccountNumber);
        Account destinationAccount = accounts.get(destinationAccountNumber);

        if (sourceAccount != null && destinationAccount != null){
            System.out.println("Enter amount to transfer: ");
            double transferAmount = Double.parseDouble(scanner.nextLine());

            if(sourceAccount.withdraw(transferAmount)){
                destinationAccount.deposit(transferAmount);
                logTransaction("Transfer", String.format("From %s to %s",sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber()), transferAmount);
            } else {
                System.out.println("Insufficient funds on source account");
            }
        } else {
            System.out.println("Source or destiny account not found");
        }
    }

    //Displays all available accounts
    private void displayAccounts(){
        if (accounts.isEmpty()) {
            System.out.println("No accounts available");
        } else {
            for (Account account: accounts.values()){
                System.out.println(account);
            }
        }
    }

    //Save data modified along the while loop (such as transactions made) and saves it into the accounts file using gson library.
    private void saveAccounts(){
        try (Writer writer = new FileWriter(ACCOUNTS_INPUT_FILE)){
            gson.toJson(accounts, writer);
        } catch (IOException e){
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    //Logs transaction into the output json file
    private void logTransaction(String type, String details, double amount){
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("type", type);
        transaction.put("details", details);
        transaction.put("amount", amount);
        transactions.add(transaction);
    }

    //Saves transactions data list into output file using gson library
    private void saveTransactions(){
        try (Writer writer = new FileWriter(TRANSACTIONS_OUTPUT_FILE)){
            gson.toJson(transactions, writer);
        } catch (IOException e){
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    //Loads all accounts found in the accounts input file using gson library
    private void loadAccounts() throws FileNotFoundException {
        try{
            Reader reader = new FileReader(ACCOUNTS_INPUT_FILE);
            Type type = new TypeToken<Map<String, Account>>() {}.getType();
            accounts = gson.fromJson(reader, type);
            if(accounts == null){
                accounts = new HashMap<>();
            }
        } catch (FileNotFoundException e){
            System.out.println("Not account file found.");
        }
    }

}
