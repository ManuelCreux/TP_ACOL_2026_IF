import java.time.*;

public class Order{
    private final String type;  // BUY or SELL
    private final String symbol;
    private final int quantity;
    private final double priceAtExecution;
    private final LocalDateTime dateTime;

    public Order(String type, String symbol, int quantity, double priceAtExecution, LocalDateTime dateTime){
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.priceAtExecution = priceAtExecution;
        this.dateTime = dateTime;
    }

    public String getType(){
        return type;
    }

    public String getSymbol(){
        return symbol;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getPriceAtExecution(){
        return priceAtExecution;
    }

    public LocalDateTime getDateTime(){
        return dateTime;
    }

}