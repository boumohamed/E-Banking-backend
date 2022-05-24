package me.bouzri.ebankingbackend.services;

import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.entities.CurrentAccount;
import me.bouzri.ebankingbackend.entities.Customer;
import me.bouzri.ebankingbackend.entities.SavingAccount;
import me.bouzri.ebankingbackend.exceptions.BalanceNotSufficientException;
import me.bouzri.ebankingbackend.exceptions.BankAccountNotFoundException;
import me.bouzri.ebankingbackend.exceptions.CusttomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initSolde, double overDraft, Long customerId) throws CusttomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initSolde, double interestRate, Long customerId) throws CusttomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccount> AccountList();
    void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String desc) throws BalanceNotSufficientException, BankAccountNotFoundException;
    void tranfser(String accountIdS, String accountIdD, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
