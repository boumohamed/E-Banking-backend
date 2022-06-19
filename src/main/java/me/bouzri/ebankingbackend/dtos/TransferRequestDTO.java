package me.bouzri.ebankingbackend.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String AccountSource;
    private String AccountDestination;
    private double amount;
    private String Description;
}
