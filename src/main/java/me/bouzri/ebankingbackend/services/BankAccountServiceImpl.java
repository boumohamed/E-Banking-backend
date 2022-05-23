package me.bouzri.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bouzri.ebankingbackend.entities.*;
import me.bouzri.ebankingbackend.enums.AccountStatus;
import me.bouzri.ebankingbackend.enums.OperationType;
import me.bouzri.ebankingbackend.exceptions.BalanceNotSufficientException;
import me.bouzri.ebankingbackend.exceptions.BankAccountNotFoundException;
import me.bouzri.ebankingbackend.exceptions.CusttomerNotFoundException;
import me.bouzri.ebankingbackend.repositories.AccountOperationRepository;
import me.bouzri.ebankingbackend.repositories.BankAccountRepository;
import me.bouzri.ebankingbackend.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional @AllArgsConstructor
@Slf4j // journalisation
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    //Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new customer !");
        Customer saveed = customerRepository.save(customer);
        return saveed;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initSolde, double overDraft, Long customerId) throws CusttomerNotFoundException {
        CurrentAccount account = new CurrentAccount();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CusttomerNotFoundException("Customer not Found");



        account.setId(UUID.randomUUID().toString());
        account.setBalance(initSolde);
        account.setCreateDate(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setOverdraft(overDraft);

        CurrentAccount saved = bankAccountRepository.save(account);
        return saved;

    }

    @Override
    public SavingAccount saveSavingBankAccount(double initSolde, double interestRate, Long customerId) throws CusttomerNotFoundException {
        SavingAccount account = new SavingAccount();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CusttomerNotFoundException("Customer not Found");



        account.setId(UUID.randomUUID().toString());
        account.setBalance(initSolde);
        account.setCreateDate(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setInterestRate(interestRate);

        SavingAccount saved = bankAccountRepository.save(account);
        return saved;
    }


    @Override
    public List<Customer> listCustomers() {

        return customerRepository.findAll();
    }

    @Override
    public BankAccount getAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("Account Not Found !"));

        return account;
    }

    @Override
    public void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount account = this.getAccount(accountId);
        if (account.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance Not Sufficient !");
        AccountOperation operation = new AccountOperation();
        operation.setBankAccount(account);
        operation.setDateOperation(new Date());
        operation.setAmount(amount);
        operation.setType(OperationType.DEBIT);
        operation.setDescription(desc);
        accountOperationRepository.save(operation);
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        bankAccountRepository.save(account);
    }

    @Override
    public void credit(String accountId, double amount, String desc) throws BankAccountNotFoundException {
        BankAccount account = this.getAccount(accountId);
        AccountOperation operation = new AccountOperation();
        operation.setBankAccount(account);
        operation.setDateOperation(new Date());
        operation.setAmount(amount);
        operation.setType(OperationType.CREDIT);
        operation.setDescription(desc);
        accountOperationRepository.save(operation);
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        bankAccountRepository.save(account);
    }

    @Override
    public void tranfser(String accountIdS, String accountIdD, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdS, amount, "Transfer to " + accountIdS);
        credit(accountIdD, amount, "transfer from " + accountIdS);
    }
    @Override
    public List<BankAccount> AccountList()
    {
        return bankAccountRepository.findAll();
    }
}
