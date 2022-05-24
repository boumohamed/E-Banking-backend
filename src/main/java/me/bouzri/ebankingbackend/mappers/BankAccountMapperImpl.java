package me.bouzri.ebankingbackend.mappers;

import me.bouzri.ebankingbackend.dtos.CustomerDTO;
import me.bouzri.ebankingbackend.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
// MapStruct : un mapper
@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer)
    {
        CustomerDTO customerDTO = new CustomerDTO();
        // customerDTO.setNom(customer.getNom());
        // customerDTO.setId(customer.getId());
        // customerDTO.setEmail(customer.getEmail());
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO)
    {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
