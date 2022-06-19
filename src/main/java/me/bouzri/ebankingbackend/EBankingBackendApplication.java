package me.bouzri.ebankingbackend;

import me.bouzri.ebankingbackend.dtos.BankAccountDTO;
import me.bouzri.ebankingbackend.dtos.CurrentAccountDTO;
import me.bouzri.ebankingbackend.dtos.SavingAccountDTO;
import me.bouzri.ebankingbackend.entities.*;
import me.bouzri.ebankingbackend.enums.AccountStatus;
import me.bouzri.ebankingbackend.enums.OperationType;
import me.bouzri.ebankingbackend.mappers.BankAccountMapperImpl;
import me.bouzri.ebankingbackend.repositories.AccountOperationRepository;
import me.bouzri.ebankingbackend.repositories.BankAccountRepository;
import me.bouzri.ebankingbackend.repositories.CustomerRepository;
import me.bouzri.ebankingbackend.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

    @Autowired
    private BankAccountMapperImpl mapper;
    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }

    //@Bean
    CommandLineRunner start1(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository)
    {
        return args -> {
            Stream.of("Mohamed", "Amina").forEach(n -> {
                Customer c = new Customer();
                c.setNom(n);
                c.setEmail(n+"@gmail.com");
                customerRepository.save(c);
            });

            customerRepository.findAll().forEach(c -> {
                CurrentAccount ca = new CurrentAccount();
                ca.setId(UUID.randomUUID().toString());
                ca.setBalance(Math.random() * 5000);
                ca.setCustomer(c);
                ca.setStatus(AccountStatus.CREATED);
                ca.setCreateDate(new Date());
                ca.setOverdraft(10000);
                bankAccountRepository.save(ca);

                SavingAccount sa = new SavingAccount();
                sa.setId(UUID.randomUUID().toString());
                sa.setBalance(Math.random() * 5000);
                sa.setCustomer(c);
                sa.setStatus(AccountStatus.CREATED);
                sa.setCreateDate(new Date());
                sa.setInterestRate(2.5);

                bankAccountRepository.save(sa);
            });

            bankAccountRepository.findAll().forEach(a -> {
                for (int i = 0; i < 2; i++)
                {
                    AccountOperation ao = new AccountOperation();
                    ao.setAmount(Math.random() * 1000);
                    ao.setBankAccount(a);
                    ao.setDateOperation(new Date());
                    ao.setType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperationRepository.save(ao);
                }

            });



        };
    };

    @Bean
    CommandLineRunner start2(BankAccountService bankAccountService)
    {
        return args -> {
            Stream.of("Mohamed", "Amina", "Ali").forEach(n -> {
                Customer c = new Customer();
                c.setNom(n);
                c.setEmail(n+"@gmail.com");
                bankAccountService.saveCustomer(mapper.fromCustomer(c));
            });

            bankAccountService.listCustomers().forEach(c -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 30000, 7000, c.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 100000, 2.7, c.getId());

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> accounts = bankAccountService.AccountList();
            for (BankAccountDTO bankAccount: accounts)
            {
                for (int i = 0; i < 5; i++ )
                {
                    String id;
                    if (bankAccount instanceof SavingAccountDTO)
                        id = ((SavingAccountDTO) bankAccount).getId();
                    else
                        id = ((CurrentAccountDTO) bankAccount).getId();

                    bankAccountService.credit(id, 10000 + Math.random() * 600000, "Credit operation");
                    bankAccountService.debit(id, 1000 + Math.random() * 5000, "Debit operation");
                }
            }



        };
    }

}
