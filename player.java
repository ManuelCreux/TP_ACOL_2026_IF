public class Player {
    private String email;
    private String password;
    private Portfolio portfolio;

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
        this.portfolio = Portfolio.loadFromFile(email);
    }

    public String getEmail(){ 
        return email; 
    }

    public String getPassword(){ 
        return password;
    }

    public Portfolio getPortfolio(){ 
        return portfolio;
    }

    public String playerPrinter() {
        return String.format(
            "Mail : %s | Password : %s",
            email, password
        );
    }
}
