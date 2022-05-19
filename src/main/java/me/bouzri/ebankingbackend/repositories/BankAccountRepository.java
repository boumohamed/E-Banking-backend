package me.bouzri.ebankingbackend.repositories;

import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
