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

    public Stock getStock(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equals(symbol)) {
                return s;
            }
        }
        // Si l'action n'existe pas
        return null;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}
