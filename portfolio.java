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
            System.out.println("\n ❌ You don't have enough money for this operation.");
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
            System.out.println("\n ❌ You can't sell a product you don't have.");
            return;
        }

        int quantityHeld = holdings.get(stock.getSymbol());
        if (quantityHeld < quantity){
            System.out.println("\n ❌ You don't have enough quantity to sell.");
            return;
        }

        // On rajoute du cash dans cashAvailable
        this.cashAvailable = cashAvailable + quantity * stock.getCurrentPrice();

        // On retire la quantité de ce stock dans holding
        holdings.replace(stock.getSymbol(), quantityHeld - quantity);

        // On n ajoute la transaction à l'historique
        history.add(new Order("SELL",  stock.getSymbol(), quantity,  stock.getCurrentPrice(), LocalDateTime.now()));
    }

    public void printTotalValue(Market market){
        System.out.println("\n=== Total Wallet Value ===");
        double totalValue = cashAvailable;

        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            Stock s = market.getStock(symbol);
            if (s != null) {
                totalValue += s.getCurrentPrice() * quantity;
            }
        }
        // Affichage du cash
        System.out.printf("Total Wallet Value : %.2f €\n", totalValue);
    }

    public void displayWallet() {
        System.out.println("\n=== Wallet ===");

        // Affichage du cash
        System.out.printf("Cash available : %.2f €\n", cashAvailable);

        // Si aucun stock
        if (holdings.isEmpty()) {
            System.out.println("No shares held.");
            return;
        }

        System.out.println("\n Shares held :");
        System.out.printf("%-10s %-10s\n", "Symbol", "Quantity");

        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            System.out.printf("%-10s %-10d\n", symbol, quantity);
        }
    }

    public void displayHistory() {
        System.out.println("\n=== History ===");

        // Si aucun stock
        if (history.isEmpty()) {
            System.out.println("You did not BUY/SELL anything yet.");
            return;
        }

        System.out.printf("%-6s %-10s %-10s %-10s %-20s\n",
        "Type", "Symbol", "Quantity", "Price at execution", "Date");

        for (Order o : history) {
            System.out.printf("%-6s %-10s %-10d %-10.2f %-20s\n",
                o.getType(),
                o.getSymbol(),
                o.getQuantity(),
                o.getPrice(),
                o.getDate().toString()
            );
        }
    }

    public void saveToFile(String email) {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

        try {
            File dir = new File("portfolios");
            if (!dir.exists()) dir.mkdir();

            FileWriter writer = new FileWriter("portfolios/" + email + ".json");
            gson.toJson(this, writer);
            writer.close();

            System.out.println("Portfolio saved successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error saving portfolio.");
        }
    }

    public static Portfolio loadFromFile(String email) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

        try {
            File file = new File("portfolios/" + email + ".json");
            if (!file.exists()) {
                System.out.println("No portfolio found, creating a new one.");
                return new Portfolio(10000); // cash initial
            }

            FileReader reader = new FileReader(file);
            Portfolio p = gson.fromJson(reader, Portfolio.class);
            reader.close();

            System.out.println("Portfolio loaded successfully.");
            return p;

        } catch (Exception e) {
            System.out.println("❌ Error loading portfolio, creating a new one.");
            return new Portfolio(10000);
        }
    }


}