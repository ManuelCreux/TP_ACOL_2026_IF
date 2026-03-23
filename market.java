public class Market {
    private List<Stock> stocks;

    public Market() {
        this.stocks = loadMarket();
    }

    public void updatePrices() {
        for (Stock s : stocks) {
            s.randomWalkUpdate();
        }
    }

    public void startAutoUpdate() {
        Thread t = new Thread(() -> {
            while (true) {
                updatePrices();
                try {
                    Thread.sleep(2000); // toutes les 2 secondes
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }


    public Stock getStock(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equals(symbol)) {
                return stock;
            }
        }
        // Si l'action n'existe pas
        return null;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void addMarketStock(Stock stock){
        stocks.add(stock);
        saveMarket(stocks);
    }

    public void removeMarketStock(String symbol){
        stocks.removeIf(s -> s.getSymbol().equalsIgnoreCase(symbol));
        saveMarket(stocks);
    }


    public static List<Stock> loadMarket() {
        List<Stock> stocks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("market.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    stocks.add(new Stock(parts[0], parts[1], Double.parseDouble(parts[2])));
                }
            }
        } catch (Exception e) {
            System.out.println("No market file found, starting empty.");
        }

        return stocks;
    }


    public static void saveMarket(List<Stock> stocks) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("market.txt"))) {
            for (Stock s : stocks) {
                pw.println(s.getSymbol() + ";" + s.getName() + ";" + s.getCurrentPrice());
            }
        } catch (Exception e) {
            System.out.println("Error saving market.");
        }
    }

}
