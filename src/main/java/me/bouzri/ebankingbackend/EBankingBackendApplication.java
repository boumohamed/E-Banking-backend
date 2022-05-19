package me.bouzri.ebankingbackend;

import me.bouzri.ebankingbackend.entities.AccountOperation;
import me.bouzri.ebankingbackend.entities.CurrentAccount;
import me.bouzri.ebankingbackend.entities.Customer;
import me.bouzri.ebankingbackend.entities.SavingAccount;
import me.bouzri.ebankingbackend.enums.AccountStatus;
import me.bouzri.ebankingbackend.enums.OperationType;
import me.bouzri.ebankingbackend.repositories.AccountOperationRepository;
import me.bouzri.ebankingbackend.repositories.BankAccountRepository;
import me.bouzri.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
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
                sa.setInvestRate(2.5);

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
    }

}
