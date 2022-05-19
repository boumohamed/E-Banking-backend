package me.bouzri.ebankingbackend.repositories;

import me.bouzri.ebankingbackend.entities.AccountOperation;
import me.bouzri.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
