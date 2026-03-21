public class Stock{
    private final String symbol;
    private final String name;
    private double currentPrice;

    public Stock(String symbol, String name, double initialPrice){
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = initialPrice;
    }

    /*Gestion de l'acutalisation des prix*/
    public void randomWalkUpdate() {
        double epsilon = (Math.random() * 0.06) - 0.03; // [-0.03, 0.03]
        currentPrice = Math.max(0.01, currentPrice * (1 + epsilon));
    }

    public double getCurrentPrice(){
         return currentPrice; 
    }

    public String getSymbol(){ 
        return symbol;
    }

    public String getName(){
        return name;
    }


}
