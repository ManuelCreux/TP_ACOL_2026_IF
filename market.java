public class Market {
    private List<Stock> stocks;

    public Market(List<Stock> stocks) {
        this.stocks = stocks;
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
    }
