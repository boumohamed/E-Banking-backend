package me.bouzri.ebankingbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.bouzri.ebankingbackend.entities.BankAccount;

import javax.persistence.*;
import java.util.Collection;


@Data

public class CustomerDTO {
    private Long id;
    private String Nom;
    private String Email;
}
