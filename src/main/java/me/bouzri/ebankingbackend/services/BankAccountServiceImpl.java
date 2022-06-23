package me.bouzri.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bouzri.ebankingbackend.dtos.*;
import me.bouzri.ebankingbackend.entities.*;
import me.bouzri.ebankingbackend.enums.AccountStatus;
import me.bouzri.ebankingbackend.enums.OperationType;
import me.bouzri.ebankingbackend.exceptions.BalanceNotSufficientException;
import me.bouzri.ebankingbackend.exceptions.BankAccountNotFoundException;
import me.bouzri.ebankingbackend.exceptions.CustomerNotFoundException;
import me.bouzri.ebankingbackend.mappers.BankAccountMapperImpl;
import me.bouzri.ebankingbackend.repositories.AccountOperationRepository;
import me.bouzri.ebankingbackend.repositories.BankAccountRepository;
import me.bouzri.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional @AllArgsConstructor
@Slf4j // journalisation
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    //Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer !");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saved = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saved);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("updating a customer !");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saved = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saved);
    }

    @Override
    public void deleteCustomer(Long customerId)
    {
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initSolde, double overDraft, Long customerId) throws CustomerNotFoundException {
        CurrentAccount account = new CurrentAccount();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not Found");



        account.setId(UUID.randomUUID().toString());
        account.setBalance(initSolde);
        account.setCreateDate(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setOverdraft(overDraft);

        CurrentAccount saved = bankAccountRepository.save(account);
        return dtoMapper.fromCurrentAccount(saved);

    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initSolde, double interestRate, Long customerId) throws CustomerNotFoundException {
        SavingAccount account = new SavingAccount();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not Found");



        account.setId(UUID.randomUUID().toString());
        account.setBalance(initSolde);
        account.setCreateDate(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setInterestRate(interestRate);

        SavingAccount saved = bankAccountRepository.save(account);
        return dtoMapper.fromSavingAccount(saved);
    }


    @Override
    public List<CustomerDTO> listCustomers() {

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> CustomerDTOs = customers.stream()
                .map(c ->
                dtoMapper.fromCustomer(c)).collect(Collectors.toList());
        return CustomerDTOs;
    }
    @Override
    public List<CustomerDTO> searcheCustomers(String keyWord) {

        List<Customer> customers = customerRepository.searchCustomers(keyWord);
        List<CustomerDTO> CustomerDTOs = customers.stream()
                .map(c ->
                        dtoMapper.fromCustomer(c)).collect(Collectors.toList());
        return CustomerDTOs;
    }


    @Override
    public BankAccountDTO getAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("Account Not Found !"));
        if (account instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) account;
            return dtoMapper.fromSavingAccount(savingAccount);
        }
        CurrentAccount currentAccount = (CurrentAccount) account;
        return dtoMapper.fromCurrentAccount(currentAccount);

    }

    @Override
    public void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount account = account(accountId);
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
        BankAccount account = account(accountId);
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
    public List<BankAccountDTO> AccountList()
    {
         List<BankAccount> list = bankAccountRepository.findAll();
         List<BankAccountDTO> collectedList = list.stream().map(a -> {
            if (a instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) a;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) a;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }

        }).collect(Collectors.toList());

        return collectedList;
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer Not Found !"));
        CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
        return customerDTO;
    }
    private BankAccount account(String id) throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(id).orElseThrow(() ->
                new BankAccountNotFoundException("Account Not Found !"));
        return account;
    }

    @Override
   public List<AccountOperationDTO> accountHistory(String id)
   {
       List<AccountOperation> operations = accountOperationRepository.findByBankAccountId(id);
       List<AccountOperationDTO> operationDTOs = operations.stream().map(o ->
               dtoMapper.fromAccounOperation(o)).collect(Collectors.toList());
       return operationDTOs;

   }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException {

        BankAccount account = bankAccountRepository.findById(id).orElse(null);
        if (account == null) throw new BankAccountNotFoundException("Account Not Found !");

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByDateOperationDesc(id, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> operationDTOS = accountOperations.getContent().stream().map(o ->
                dtoMapper.fromAccounOperation(o)).collect(Collectors.toList());
        accountHistoryDTO.setOperations(operationDTOS);
        accountHistoryDTO.setId(account.getId());
        accountHistoryDTO.setBalance(account.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrantPage(page);
        accountHistoryDTO.setTotalePages(accountOperations.getTotalPages());
        if (account instanceof SavingAccount)
            accountHistoryDTO.setType("Saving Account");
        else
            accountHistoryDTO.setType("Current Account");

        return accountHistoryDTO;
    }

    @Override
    public List<BankAccountDTO> customerAccounts(Long customerId)
    {
        //Page<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId, PageRequest.of(page, size));



        List<BankAccount> list = bankAccountRepository.findByCustomerId(customerId);
        List<BankAccountDTO> collectedList = list.stream().map(a -> {
            if (a instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) a;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) a;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }

        }).collect(Collectors.toList());

        return collectedList;
    }
}
