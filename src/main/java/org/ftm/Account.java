package org.ftm;
//Account class
public class Account {

    private String accountNumber;
    private double balance;

    //Account constructor that receives the account number and initial balance as args
    public Account (String accountNumber, double initialBalance){
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Account number geter
    public String getAccountNumber(){
        return accountNumber;
    }

    //Account balance geter
    public double getBalance(){
        return balance;
    }

    // Adds new amount to the current balance
    public void deposit(double amount){
        balance += amount;
    }

    // Takes out an amount from the current balance if it's enough
    public boolean withdraw(double amount){
        if(amount <= balance){
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    //Overrides toString to display variables more visually
    @Override
    public String toString() {
        return "Account: " + accountNumber + ", Balance: " + balance;
    }
}
