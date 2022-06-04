package me.bouzri.ebankingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.bouzri.ebankingbackend.entities.BankAccount;
import me.bouzri.ebankingbackend.enums.OperationType;

import javax.persistence.*;
import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date dateOperation;
    private double amount;
    private OperationType type;
    private String description;
}
