package me.bouzri.ebankingbackend.repositories;

import me.bouzri.ebankingbackend.entities.AccountOperation;
import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    public List<BankAccount> findByCustomerId(Long accountId);
}
