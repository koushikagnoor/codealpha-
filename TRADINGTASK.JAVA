import java.util.*;

// Stock Class
class Stock {
    private String symbol;
    private double price;
    private double volatility;

    public Stock(String symbol, double price, double volatility) {
        this.symbol = symbol;
        this.price = price;
        this.volatility = volatility;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void updatePrice() {
        Random random = new Random();
        double changePercent = (random.nextDouble() * 2 - 1) * volatility;
        price += price * changePercent;
        if (price < 1) price = 1;  // Ensure price doesn't fall below $1
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f", symbol, price);
    }
}

// Market Class
class Market {
    private Map<String, Stock> stocks;

    public Market() {
        stocks = new HashMap<>();
        stocks.put("AAPL", new Stock("AAPL", 150, 0.02));
        stocks.put("GOOG", new Stock("GOOG", 2800, 0.03));
        stocks.put("AMZN", new Stock("AMZN", 3300, 0.025));
        stocks.put("TSLA", new Stock("TSLA", 700, 0.04));
    }

    public void displayMarket() {
        System.out.println("\nCurrent Market Prices:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public void simulateMarketChange() {
        for (Stock stock : stocks.values()) {
            stock.updatePrice();
        }
        System.out.println("\nMarket prices updated!");
    }
}

// Portfolio Class
class Portfolio {
    private Map<Stock, Integer> holdings;
    private double cashBalance;

    public Portfolio(double initialBalance) {
        holdings = new HashMap<>();
        this.cashBalance = initialBalance;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void addCash(double amount) {
        cashBalance += amount;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost <= cashBalance) {
            holdings.put(stock, holdings.getOrDefault(stock, 0) + quantity);
            cashBalance -= cost;
            System.out.println("Bought " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Insufficient funds to buy " + quantity + " shares of " + stock.getSymbol());
        }
    }

    public void sellStock(Stock stock, int quantity) {
        Integer heldQuantity = holdings.get(stock);
        if (heldQuantity == null || heldQuantity < quantity) {
            System.out.println("Not enough shares to sell.");
            return;
        }
        holdings.put(stock, heldQuantity - quantity);
        if (holdings.get(stock) == 0) holdings.remove(stock);  // Remove stock if no shares left
        double proceeds = stock.getPrice() * quantity;
        cashBalance += proceeds;
        System.out.println("Sold " + quantity + " shares of " + stock.getSymbol());
    }

    public void displayPortfolio() {
        System.out.println("\nPortfolio:");
        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            System.out.println(entry.getKey().getSymbol() + ": " + entry.getValue() + " shares @ $" + entry.getKey().getPrice());
        }
        System.out.printf("Cash Balance: $%.2f%n", cashBalance);
        System.out.printf("Portfolio Value: $%.2f%n", calculatePortfolioValue());
    }

    public double calculatePortfolioValue() {
        double value = cashBalance;
        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            value += entry.getKey().getPrice() * entry.getValue();
        }
        return value;
    }
}

// TradingPlatform Class
public class TradingPlatform {
    private Market market;
    private Portfolio portfolio;
    private Scanner scanner;

    public TradingPlatform() {
        market = new Market();
        portfolio = new Portfolio(10000); // Starting balance
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nStock Trading Platform");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Simulate Market Change");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    market.displayMarket();
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 4:
                    portfolio.displayPortfolio();
                    break;
                case 5:
                    market.simulateMarketChange();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine();
        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found in market.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int quantity = scanner.nextInt();
        if (quantity <= 0) {
            System.out.println("Quantity should be greater than zero.");
            return;
        }

        portfolio.buyStock(stock, quantity);
    }

    private void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine();
        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found in market.");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();
        if (quantity <= 0) {
            System.out.println("Quantity should be greater than zero.");
            return;
        }

        portfolio.sellStock(stock, quantity);
    }

    public static void main(String[] args) {
        TradingPlatform platform = new TradingPlatform();
        platform.start();
    }
}
