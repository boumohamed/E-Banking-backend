package me.bouzri.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bouzri.ebankingbackend.dtos.BankAccountDTO;
import me.bouzri.ebankingbackend.dtos.CustomerDTO;
import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.entities.Customer;
import me.bouzri.ebankingbackend.exceptions.CustomerNotFoundException;
import me.bouzri.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/customers")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestCotroller {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers()
    {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> findCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword)
    {
        return bankAccountService.searcheCustomers("%"+keyword+"%");
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO request)
    {
        CustomerDTO customer = bankAccountService.saveCustomer(request);
        return customer;
    }
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long id, @RequestBody CustomerDTO request)
    {
        request.setId(id);
        CustomerDTO customer = bankAccountService.updateCustomer(request);
        return customer;
    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id)
    {
        bankAccountService.deleteCustomer(id);
    }

    @GetMapping("/customers/{id}/accounts")
    public List<BankAccountDTO> getCustomerAccounts(@PathVariable(name = "id") Long id) {
        return bankAccountService.customerAccounts(id);
    }
}
