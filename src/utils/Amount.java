/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author LD
 */
public class Amount {
    private String type;
    private String exetime;
    private String amountcr;

    public Amount() {
    }

    public Amount(String type, String exetime, String amountcr) {
        this.type = type;
        this.exetime = exetime;
        this.amountcr = amountcr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExetime() {
        return exetime;
    }

    public void setExetime(String exetime) {
        this.exetime = exetime;
    }

    public String getAmountcr() {
        return amountcr;
    }

    public void setAmountcr(String amountcr) {
        this.amountcr = amountcr;
    }

    @Override
    public String toString() {
        return type + "," + exetime + "," + amountcr;
    }
    
    
}
