package me.bouzri.ebankingbackend.exceptions;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String s) {
        super(s);
    }
}
