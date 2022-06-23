package me.bouzri.ebankingbackend.services;

import me.bouzri.ebankingbackend.dtos.*;
import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.entities.CurrentAccount;
import me.bouzri.ebankingbackend.entities.Customer;
import me.bouzri.ebankingbackend.entities.SavingAccount;
import me.bouzri.ebankingbackend.exceptions.BalanceNotSufficientException;
import me.bouzri.ebankingbackend.exceptions.BankAccountNotFoundException;
import me.bouzri.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    CurrentAccountDTO saveCurrentBankAccount(double initSolde, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount(double initSolde, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();


    List<CustomerDTO> searcheCustomers(String keyWord);

    BankAccountDTO getAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccountDTO> AccountList();
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String desc) throws BalanceNotSufficientException, BankAccountNotFoundException;
    void tranfser(String accountIdS, String accountIdD, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<AccountOperationDTO> accountHistory(String id);

    AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException;

    List<BankAccountDTO> customerAccounts(Long customerId);
}
