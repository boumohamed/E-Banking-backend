package me.bouzri.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CA")

@Data @AllArgsConstructor @NoArgsConstructor
public class CurrentAccount extends BankAccount{
    private double overdraft;
}
