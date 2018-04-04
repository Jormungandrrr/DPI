package models;

import java.io.Serializable;

/**
 *
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
public class BankInterestRequest implements Serializable{

    private int amount; // the requested loan amount
    private int time; // the requested loan period
    public LoanRequest loanRequest;

    public BankInterestRequest() {
        super();
        this.amount = 0;
        this.time = 0;
    }

    public BankInterestRequest(int amount, int time, LoanRequest lr){
        super();
        this.amount = amount;
        this.time = time;
        this.loanRequest = lr;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    @Override
    public String toString() {
        return " amount=" + amount + " time=" + time;
    }
}
