import java.util.*;

public class simulation {

    // Données du master
    private static final String MASTER_IDENTIFIER = "master@market.com";
    private static final String MASTER_PASSWORD = "1";

    private static PlayersList players = new PlayersList();
    private static Market market = new Market();
    private static Player currentPlayer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        market.startAutoUpdate();

        System.out.println("Welcome to the stock market simulation  !");
        int choix = 0;
        do {
            System.out.println("\nYou are :");
            System.out.println("1. Master");
            System.out.println("2. Player");
            System.out.println("3. Leave");
            System.out.print("Your choice : ");


            try{
                choix = scanner.nextInt();
                scanner.nextLine();
    
                switch (choix) {
                    case 1 -> if (!authentifierMaster(scanner)) {
                                System.out.println("❌ Access denied");
                                return;
                            }
                            menuMaster(scanner);
                    case 2 -> menuPlayer(scanner);
                    case 3 -> System.out.println("See you soon !");
                    default -> System.out.println("Invalid Choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error, try again");
                scanner.nextLine();
            }

        } while (choix != 3);
    }

    // Authentification du master
    private static boolean authentifierMaster(Scanner scanner) {
        System.out.println("\n--- Master Connexion ---");

        System.out.print("Identifier : ");
        String identifier = scanner.nextLine();

        System.out.print("Password : ");
        String password = scanner.nextLine();

        if (identifier.equals(MASTER_IDENTIFIER) && password.equals(MASTER_PASSWORD)) {
            System.out.println("✅ Connexion successful !");
            return true;
        } else {
            System.out.println("❌ Incorrect credentials");
            return false;
        }
    }

    
    // Menu master
    private static void menuMaster(Scanner scanner) {
        int choix = 0;

        do {
            System.out.println("\n--- Master Actions ---");
            System.out.println("1. Add a stock");
            System.out.println("2. Remove a stock");
            System.out.println("3. Display available market stocks");
            System.out.println("4. Display player accounts");
            System.out.println("5. Delete an account");
            System.out.println("6. Change a price");
            System.out.println("7. Leave");
            System.out.print("Your choice : ");

            try{
                choix = scanner.nextInt();
                scanner.nextLine();
    
                switch (choix) {
                    case 1 -> addStock(scanner, market);
                    case 2 -> removeStock(scanner, market);
                    case 3 -> displayMarketStock(scanner, market);
                    case 4 -> displayPlayerAccounts(scanner, players);
                    case 5 -> deleteAccount(scanner, players);
                    case 6 -> changePrice(scanner, market);
                    case 7 -> System.out.println("See you soon !");
                    default -> System.out.println("Invalid Choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error, try again");
                scanner.nextLine();
            }

        } while (choix != 7);
    }

    

    // Menu Player
    private static void menuPlayer(Scanner scanner) {
        System.out.println("\n--- Player Actions ---");
        System.out.println("1. Log in with an existing email");
        System.out.println("2. Create a new account");
        System.out.println("3. Leave");
        System.out.print("Your choice : ");
        int choixConnexion = scanner.nextInt();
        scanner.nextLine();
    
        switch (choixConnexion) {
            case 1 -> {
                currentPlayer = playerConnexion(scanner, players);
                if (currentPlayer == null) {
                    System.out.println("❌ Connexion failed");
                    return;
                }
                mainMenuPlayer(scanner, currentPlayer);
            }
            case 2 -> {
                currentPlayer = createAccount(scanner, players);
                if (currentPlayer != null) {
                    mainMenuPlayer(scanner, currentPlayer);
                }
            }
            case 3 -> System.out.println("See you soon !");
            default -> System.out.println("Invalid Choice.");
        }
    }
    
    
    public static Player playerConnexion(Scanner scanner, PlayersList players) {
        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String mdp = scanner.nextLine();

        for (Player p : players.getPlayers()) {
            if (p.getEmail().equals(email) && p.getPassword().equals(mdp)) {
                System.out.println("✅ Connexion successful !");
                return p;
            }
        }

        System.out.println("❌ Incorrect credentials");
        return null;
    }
    
    // Nouvelle méthode pour créer un compte
    public static Player createAccount(Scanner scanner, PlayersList players) {
        System.out.print("Email : ");
        String email = scanner.nextLine();

        // Vérifier si l'email existe déjà
        for (Player p : players.getPlayers()) {
            if (p.getEmail().equals(email)) {
                System.out.println("❌ This email address is already in use.");
                return null;
            }
        }

        System.out.print("Password : ");
        String mdp = scanner.nextLine();

        Player newPlayer = new Player(email, mdp);
        players.addPlayer(newPlayer);
        System.out.println("✅ Account created successfully !");
        return newPlayer;
    }
    
    // Nouvelle méthode pour supprimer un compte
    public static void deleteAccount(Scanner scanner, PlayersList players) {
        System.out.print("Email address to delete : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String password = scanner.nextLine();

        Player joueurASupprimer = players.findPlayer(email, password);

        if (joueurASupprimer == null) {
            System.out.println("❌ No account matches these credentials..");
            return;
        }

        players.removePlayer(joueurASupprimer);
        System.out.println("Account deleted successfully.");
    }

    
    // Menu Player
    private static void mainMenuPlayer(Scanner scanner, Player currentPlayer) {
        int choix = 0;
        Portfolio portfolio = currentPlayer.getPortfolio();
    
        do {
            System.out.println("\n--- Main Menu  ---");
            System.out.println("1. Display market stocks");
            System.out.println("2. BUY a stock");
            System.out.println("3. SELL a stock");
            System.out.println("4. Display available cash and holding");
            System.out.println("5. Display history");
            System.out.println("6. Display total value of wallet");
            System.out.println("7. Deconnexion");
            System.out.print("Your choice : ");

            try {
                choix = scanner.nextInt();
                scanner.nextLine();
        
                switch (choix) {
                    case 1 -> displayMarketStock(scanner, market);
                    case 2 -> buyStock(scanner, market, portfolio);
                    case 3 -> sellStock(scanner, market, portfolio);
                    case 4 -> displayCashHoldings(scanner, currentPlayer);
                    case 5 -> displayHistory(scanner, currentPlayer);
                    case 6 -> displayWalletValue(scanner, currentPlayer);
                    case 7 -> {
                        System.out.println("Deconnexion...");
                        currentPlayer.getPortfolio().saveToFile(currentPlayer.getEmail());
                    }
                    default -> System.out.println("Invalid Choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error, try again");
                scanner.nextLine();
            }
    
        } while (choix != 7);
    }

    // Catalogue
    public static void displayMarketStock(Scanner scanner, Market market) {
        System.out.println("\n=== Shares available on the market ===");

        for (Stock s : market.getStocks()) {
            System.out.println(s);
        }
    }

    public static void buyStock(Scanner scanner, Market market, Portfolio portfolio) {

        System.out.println("\n=== Buying a share ===");

        // Demander le symbole
        System.out.print("\nEnter the stock symbol you wish to buy : ");
        String symbol = scanner.nextLine().toUpperCase();

        //Vérifier que l'action existe
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("❌ This action does not exist.");
            return;
        }

        // Demander la quantité
        System.out.print("Quantity to purchase : ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid quantity.");
            scanner.nextLine();
            return;
        }

        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity <= 0) {
            System.out.println("❌ The quantity must be positive.");
            return;
        }

        // Appeler la méthode buy du portefeuille
        portfolio.Buy(stock, quantity);

        // 6. Confirmation
        System.out.println("✅ Purchase made : " + quantity + " x " + symbol);
    }


    public static void sellStock(Scanner scanner, Market market, Portfolio portfolio) {

        System.out.println("\n=== Selling a share ===");

        // Demander le symbole
        System.out.print("\nEnter the stock symbol you wish to sell : ");
        String symbol = scanner.nextLine().toUpperCase();

        //Vérifier que l'action existe
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("❌ This action does not exist.");
            return;
        }

        // Demander la quantité
        System.out.print("Quantity to sell : ");
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid quantity.");
            scanner.nextLine();
            return;
        }

        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity <= 0) {
            System.out.println("❌ The quantity must be positive.");
            return;
        }

        // Appeler la méthode buy du portefeuille
        portfolio.Sell(stock, quantity);

        // 6. Confirmation
        System.out.println("✅ Sale made : " + quantity + " x " + symbol);
    }


    private static void addStock(Scanner scanner, Market market) {
        System.out.println("\n--- Adding a stock ---");

        // Demander le symbole
        System.out.print("\nEnter the stock symbol you want to add : ");
        String symbol = scanner.nextLine().toUpperCase();

        // Vérifier que le symbole n'existe pas déjà sur le marché
        Stock stock = market.getStock(symbol);
        if (stock != null) {
            System.out.println("❌ This action already exists.");
            return;
        }

        // Demander le nom
        System.out.print("\nEnter the stock name you want to add : ");
        String name = scanner.nextLine();

        // Demander le prix
        System.out.print("\nEnter the stock price you want to add : ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price format.");
            return;
        }

        // Vérifier que le prix est positif
        if (price <= 0) {
            System.out.println("❌ The price must be positive.");
            return;
        }

        // Ajouter au market
        market.addMarketStock(new Stock(symbol, name, price));
        System.out.println("✅ Stock added successfully : " + symbol + " (" + name + ")");
    }

    private static void removeStock(Scanner scanner, Market market) {
        System.out.println("\n--- Removing a stock ---");

        // Demander le symbole
        System.out.print("\nEnter the stock symbol you want to remove : ");
        String symbol = scanner.nextLine().toUpperCase();

        // Vérifier que le symbole existe déjà sur le marché
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("❌ This action doesn't exists.");
            return;
        }

        // Ajouter au market
        market.removeMarketStock(symbol);
        System.out.println("✅ Stock removed successfully : " + symbol);
    }

    public static void displayPlayerAccounts(Scanner scanner, PlayersList players){
        System.out.println("\n--- List of players with their mail and password ---");
        
        for (Player p : players.getPlayers()) {
            System.out.println(p.playerPrinter());
        }
    }

    private static void changePrice(Scanner scanner, Market market) {
        System.out.println("\n--- Price change ---");

        // Demander le symbole
        System.out.print("\nEnter the stock symbol you want to change : ");
        String symbol = scanner.nextLine().toUpperCase();

        // Vérifier que le symbole existe déjà sur le marché
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("❌ This action doesn't exists.");
            return;
        }

        // Demander le nouveau prix
        System.out.print("\nEnter the new price of this stock : ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price format.");
            return;
        }

        // Vérifier que le prix est positif
        if (price <= 0) {
            System.out.println("❌ The price must be positive.");
            return;
        }

        // On récupère le nom
        String name = stock.getName();

        // Modifier le prix
        stock.setCurrentPrice(price);
        System.out.println("✅ Stock price successfully modified: " + symbol);
    }


    public static void displayCashHoldings(Scanner scanner, Player player){
        Portfolio portfolio = player.getPortfolio();
        portfolio.displayWallet();
    }

    public static void displayHistory(Scanner scanner, Player player){
        Portfolio portfolio = player.getPortfolio();
        portfolio.displayHistory();
    }

    public static void displayWalletValue(Scanner scanner, Player player){
        Portfolio portfolio = player.getPortfolio();
        portfolio.printTotalValue();
    }
}