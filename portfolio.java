public class Portfolio{
    private double cashAvailable;
    private Map<String, Integer> holdings;
    private List<Order> history;

    public Portfolio(double cashAvailable){
        this.cashAvailable = cashAvailable;
        this.holdings = new HashMap<>();
        this.history = new ArrayList<>();
    }

    public double getCashAvailable(){
        return cashAvailable;
    }

    public Map<String, Integer> getHoldings(){
        return holdings;
    }

    public List<Order> getHistory(){
        return history;
    }

    public void Buy(Stock stock, int quantity){
        // Barrière pour savoir si j'ai de quoi l'acheter
        double cost = stock.getCurrentPrice() * quantity;
        if (cost > cashAvailable){
            System.out.println("\nYou don't have enough money for this operation.");
            return;
        }

        // On retranche cost de cashAvailable
        this.cashAvailable = cashAvailable - cost;

        // Ajoute quantity de stock à holdings
        if (!holdings.containsKey(stock.getSymbol())){
            holdings.put(stock.getSymbol(), quantity);
        }
        else{
            holdings.replace(stock.getSymbol(), quantity + holdings.get(stock.getSymbol()));
        }

        // On ajoute la transaction à l'historique
        history.add(new Order("BUY",  stock.getSymbol(), quantity,  stock.getCurrentPrice(), LocalDateTime.now()));
    }

    public void Sell(Stock stock, int quantity){
        // Barrière pour savoir si j'ai de quoi vendre
        if (!holdings.containsKey(stock.getSymbol())){
            System.out.println("\nYou can't sell a product you don't have.");
            return;
        }

        int quantityHeld = holdings.get(stock.getSymbol());
        if (quantityHeld < quantity){
            System.out.println("\nYou don't have enough quantity to sell.");
            return;
        }

        // On rajoute du cash dans cashAvailable
        this.cashAvailable = cashAvailable + quantity * stock.getCurrentPrice();

        // On retire la quantité de ce stock dans holding
        holdings.replace(stock.getSymbol(), quantityHeld - quantity);

        // On n ajoute la transaction à l'historique
        history.add(new Order("SELL",  stock.getSymbol(), quantity,  stock.getCurrentPrice(), LocalDateTime.now()));
    }

    public double getTotalValue(Market market) {
        double totalValue = cashAvailable;

        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            Stock s = market.getStock(symbol);
            if (s != null) {
                totalValue += s.getCurrentPrice() * quantity;
            }
        }
        return totalValue;
    }
}