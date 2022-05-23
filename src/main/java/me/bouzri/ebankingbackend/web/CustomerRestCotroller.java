package me.bouzri.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bouzri.ebankingbackend.entities.Customer;
import me.bouzri.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/customers")
@AllArgsConstructor
@Slf4j
public class CustomerRestCotroller {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<Customer> customers()
    {
        return bankAccountService.listCustomers();
    }
}
