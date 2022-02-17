/*
Carson Seese - 11-8-2019 - Stock.java
CIT344 Assignment 3: Stock Exchange

This class outlines the Stock object. Necessary methods have been synchronized to prevent resource usage issues between
threads.
 */
import java.util.Random;

public class Stock {

    private double price;
    private int sharesAvailable;
    private String symbol;

    public Stock(String symbol) {
        price = getInitialPrice();
        sharesAvailable = 500;
        this.symbol = symbol;
    }

    public synchronized double purchaseStock(int shares) throws InterruptedException {
        // If there are no shares available to purchase, have the threads wait
        while(sharesAvailable < shares) {
            wait(500);
        }

        if(sharesAvailable >= shares){
            sharesAvailable = sharesAvailable - shares;
            notifyAll();
            return shares * price;
        }

        notifyAll();
        return -1;
    }

    public synchronized double sellStock(int shares) {
        if(shares > 0){
            sharesAvailable = sharesAvailable + shares;
            notifyAll();
            return shares * price;
        }
        notifyAll();
        return -1;
    }

    public double getInitialPrice() {
        Random r = new Random();
        price = r.nextInt(100);
        return price;
    }

    public synchronized double updatePrice() {
        price = price +1;
        notifyAll();
        return price;
    }

    public double getPrice() {
        return price;
    }

    public int getSharesAvailable() {
        return sharesAvailable;
    }
}