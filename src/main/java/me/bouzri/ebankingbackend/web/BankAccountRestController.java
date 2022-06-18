package me.bouzri.ebankingbackend.web;

import lombok.AllArgsConstructor;
import me.bouzri.ebankingbackend.dtos.*;
import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.exceptions.BalanceNotSufficientException;
import me.bouzri.ebankingbackend.exceptions.BankAccountNotFoundException;
import me.bouzri.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getAccount(@PathVariable(name = "id") String accounId) throws BankAccountNotFoundException {
        return bankAccountService.getAccount(accounId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> accounts()
    {
        return bankAccountService.AccountList();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> accountHistory(@PathVariable(name = "id") String id)
    {
        return bankAccountService.accountHistory(id);
    }

    @GetMapping("/accounts/{id}/pageoperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable(name = "id") String id,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(id, page, size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO Debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return  debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO Credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return  creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void Transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.tranfser(transferRequestDTO.getAccountSource(),
                                        transferRequestDTO.getAccountDestination(),
                                        transferRequestDTO.getAmount(),
                                        transferRequestDTO.getDescription());

    }

}
