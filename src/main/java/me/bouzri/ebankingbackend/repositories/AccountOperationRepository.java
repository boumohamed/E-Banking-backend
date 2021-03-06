package me.bouzri.ebankingbackend.repositories;

import me.bouzri.ebankingbackend.entities.AccountOperation;
import me.bouzri.ebankingbackend.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    public List<AccountOperation> findByBankAccountId(String accountId);

    public Page<AccountOperation> findByBankAccountIdOrderByDateOperationDesc(String accountId, Pageable pageable);
}
