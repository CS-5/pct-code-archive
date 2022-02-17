/*
Carson Seese - 11-9-2019 - StockExchange.java
CIT344 Assignment 3: Stock Exchange

This class and it's subclasses are responsible for 3 types of threads: 3x trader threads, an updater thread, and a
logger thread. The logger thread logs all transaction messages to the console as well as to a file. The program's
execution can be modified using the constants defined starting at line 16. Since buying/selling is completely random,
the traders almost always loose money. To make this slightly more satisfying, setting traderCashOut = true will force
the traders to cash out all stocks on the last iteration.
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class StockExchange {

    private static final int traders = 3;
    private static final double traderMoney = 10000;
    private static final int traderActions = 100; // Max number of shares to buy/sell in a transaction
    private static final int traderTransactions = 10; // Max number of transactions before traders stop
    private static final boolean traderCashOut = false; // Should the trader cash out all shares on last transaction?
    private static final String logPath = "transactions.log";

    public static void main(String[] args) {
        Stock stock = new Stock("TSLA");
        List logMessages = Collections.synchronizedList(new ArrayList<>());
        // traderState tracks the trader and total money after all 10 trades have been made.
        Map<Integer, Double> traderState = Collections.synchronizedMap(new HashMap<>());

        // Initialize logger thread and start
        Thread logger = null;
        try {
            logger = new Thread(new Logger(logMessages, traderState, logPath));
        } catch (IOException e) {
            System.out.println("Unable to create logfile. Exiting.");
            System.exit(-1);
        }
        logger.start();

        // Start updater
        new Thread(new Updater(stock, logMessages, traderState)).start();

        // Start traders
        for(int i = 0; i < traders; i++) {
            new Thread(new Trader(
                    i+1,
                    stock,
                    logMessages,
                    traderState,
                    traderMoney,
                    traderActions,
                    traderTransactions,
                    traderCashOut)
            ).start();
        }

        // Wait until logger thread finishes, then report how much money was made (or lost)
        try {
            logger.join();
            System.out.println(""); // Make spacer
            synchronized (traderState) {
               for(Map.Entry<Integer, Double> e : traderState.entrySet()) {
                   System.out.println(String.format("[Trader %d] Earned $%.1f", e.getKey(), e.getValue() - traderMoney));
               }
            }
        } catch (InterruptedException e) {
            System.out.println("Unable to join logger thread.");
        }
    }
}

class Trader implements Runnable {

    private int id;
    private Stock stock;
    private List logMessages;
    private Map<Integer, Double> state;
    private double money;
    private int traderActions;
    private int traderTransactions;
    private boolean traderCashOut;
    private int shares;

    public Trader(int id, Stock stock,
                  List logMessages,
                  Map<Integer, Double> traderState,
                  double money, int traderActions,
                  int traderTransactions,
                  boolean traderCashOut
    ) {
        this.id = id;
        this.stock = stock;
        this.logMessages = logMessages;
        this.state = traderState;
        this.money = money;
        this.traderActions = traderActions;
        this.traderTransactions = traderTransactions;
        this.traderCashOut = traderCashOut;
        this.shares = 0;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (i < traderTransactions) {
                // Cash out if requirements are met
                if (i == 9 && traderCashOut && shares != 0) {
                    sell(shares);
                    i++;
                    continue;
                }

                // Randomly choose to buy or sell. Trader can only buy if they have enough money
                int ta = new Random().nextInt(traderActions -1 ) + 1;
                if (new Random().nextBoolean() && money > stock.getPrice() * ta) {
                    // Buy random number of shares and increment transactions by 1
                    if (buy(ta))
                        i++;
                } else if (shares != 0 && shares > ta) {
                    // Sell random number of shares and increment transactions by 1
                    if(sell(ta))
                        i++;
                }

                Thread.sleep(new Random().nextInt(2000)+1000);
            }

            // Log when finished
            synchronized (logMessages) {
                logMessages.add(String.format(
                        "[Trader %d] Finished %d transactions. Money: $%.1f, Shares: %d",
                        id,
                        i,
                        money,
                        shares
                ));
                logMessages.notifyAll();
            }

            // Add trader id and remaining money to traderState
            synchronized (state) {
                state.put(id, money);
            }
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Buy attempts to buy stock.
     * @throws InterruptedException
     */
    public boolean buy(int number) throws InterruptedException {
        double price = stock.purchaseStock(number);
        if (price == -1)
            return false;

        // Update money and shares
        money = money - price;
        shares = shares + number;

        // Log outcome
        synchronized (logMessages) {
            logMessages.add(String.format("[Trader %d] Bought %d share(s) for $%.1f", id, number, price));
            logMessages.notifyAll();
        }
        return true;
    }

    /**
     * Sell attempts to sell stock.
     */
    public boolean sell(int number) {
        double price = stock.sellStock(number);
        if (price == -1)
            return false;

        // Update money and shares
        money = money + price;
        shares = shares - number;

        // Log outcome
        synchronized (logMessages) {
            logMessages.add(String.format("[Trader %d] Sold %d share(s) for $%.1f", id, number, price));
            logMessages.notifyAll();
        }
        return true;
    }
}

class Updater implements Runnable {

    private Stock stock;
    private List logMessages;
    private Map<Integer, Double> state;

    public Updater(Stock stock, List logMessages, Map<Integer, Double> traderState) {
        this.stock = stock;
        this.logMessages = logMessages;
        this.state = traderState;
    }

    @Override
    public void run() {
        try {
            // If there are still shares to sell and the traders haven't finished, update the price
            while(stock.getSharesAvailable() > 0 && state.size() < 3) {
                double price = stock.updatePrice();

                // Log outcome
                synchronized (logMessages) {
                    logMessages.add(String.format("[Updater] Price Updated. $%.1f/share", price));
                    logMessages.notifyAll();
                }

                Thread.sleep(new Random().nextInt(1500));
            }
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}

class Logger implements Runnable {

    private List logMessages;
    private Map<Integer, Double> state;
    private PrintWriter logWriter;

    public Logger(List logMessages, Map<Integer, Double> traderState, String path) throws IOException {
        this.logMessages = logMessages;
        this.state = traderState;
        this.logWriter = new PrintWriter(new FileWriter(path, true));
    }

    @Override
    public void run() {
        // While the traders are still going...
        while (state.size() < 3) {
            synchronized (logMessages) {
                // If there are not 5 messages in the logger and there are still active traders, wait.
                while(logMessages.size() != 5 && state.size() < 3) {
                    try {
                        logMessages.wait();
                    } catch (InterruptedException e) {}
                }

                // When there are 5 messages, dump the log
                if (logMessages.size() == 5) {
                    logDump();
                }
            }
        }

        // After the traders have finished, dump any remaining log messages
        if (logMessages.size() > 0) {
            synchronized (logMessages) {
                logDump();
            }
        }

        logWriter.close();
    }

    /**
     * Dump log messages to the console and a log file
     */
    private void logDump() {
        for(Object msg : logMessages) {
            String message = String.format("%s - %s", new java.util.Date(), (String) msg);
            System.out.println(message);
            logWriter.println(message);
        }
        logMessages.clear();
        logMessages.notifyAll();
    }
}
