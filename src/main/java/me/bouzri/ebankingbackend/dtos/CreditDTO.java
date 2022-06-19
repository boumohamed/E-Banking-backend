package me.bouzri.ebankingbackend.dtos;

import lombok.Data;

@Data
public class CreditDTO {
    private String AccountId;
    private double amount;
    private String Description;
}
