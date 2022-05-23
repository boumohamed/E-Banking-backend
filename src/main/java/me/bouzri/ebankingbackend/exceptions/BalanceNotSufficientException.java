package me.bouzri.ebankingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String s) {
        super(s);
    }
}
