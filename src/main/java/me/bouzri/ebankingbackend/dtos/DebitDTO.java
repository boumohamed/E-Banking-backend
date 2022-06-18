package me.bouzri.ebankingbackend.dtos;

import lombok.Data;

@Data
public class DebitDTO {
    private String AccountId;
    private double amount;
    private String Description;
}
