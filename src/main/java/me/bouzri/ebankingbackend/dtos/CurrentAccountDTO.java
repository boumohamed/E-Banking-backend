package me.bouzri.ebankingbackend.dtos;

import lombok.Data;
import me.bouzri.ebankingbackend.enums.AccountStatus;

import java.util.Date;




@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private String id;
    private double Balance;
    private Date CreateDate;
    private AccountStatus status;
    private CustomerDTO customer;
    private double overdraft;
}
