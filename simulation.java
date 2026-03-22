import java.sql.*;
import java.util.*;

public class simulation {

    // Données du master
    private static final String MASTER_IDENTIFIER = "master@market.com";
    private static final String MASTER_PASSWORD = "1";

    // Connexion globale accessible par toutes les fonctions
    private static Connection conn;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // On récupère les données du marché enregistrés à la dernière connexion
        public static List<Stock> loadMarket() throws IOException {
            List<Stock> stocks = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("market.json"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                stocks.add(new Stock(parts[0], Double.parseDouble(parts[1])));
            }

            br.close();
            return stocks;
        }

        // On récupère la liste des joueurs et leurs données de connexion
        public static List<Player> loadPlayers() {
            List<Player> players = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("players.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        players.add(new Player(parts[0], parts[1]));
                    }
                }
            } catch (Exception e) {
                System.out.println("Aucun fichier de joueurs trouvé, création d'une nouvelle liste.");
            }

            return players;
        }


        Market market = new Market(stocks);
        market.startAutoUpdate();

        System.out.println("Welcome to the stock market simulation  !");
        int profil = 0;
        do {
            System.out.println("\nYou are :");
            System.out.println("1. Master");
            System.out.println("2. Player");
            System.out.println("3. Leave");
            System.out.print("Your choice : ");
            try {
                profil = scanner.nextInt();
                scanner.nextLine();

                // Ouverture de la connexion globale
                conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
                conn.setAutoCommit(false);

                if (profil == 1) {
                    if (!authentifierMaster(scanner)) {
                        System.out.println("❌ Access denied");
                        return;
                    }
                    menuMaster(scanner);

                } else if (profil == 2) {
                    menuPlayer(scanner);
                } else if (profil == 3){
                    System.out.println("See you soon !");
                } else {
                    System.out.println("Invalid choice.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch(Exception e){
                System.out.println("❌ Error, try again");
                scanner.nextLine();
            }finally {
                try { if (conn != null) conn.close(); } catch (Exception ignored) {}
            }
        } while (profil != 4);

        scanner.close();
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
            System.out.println("6. Leave");
            System.out.print("Your choice : ");

            try{
                choix = scanner.nextInt();
                scanner.nextLine();
    
                switch (choix) {
                    case 1 -> addStock(scanner, market);
                    case 2 -> removeStock(scanner);
                    case 3 -> displayMarketStock(scanner);
                    case 4 -> displayPlayerAccounts(scanner);
                    case 5 -> deleteAccount(scanner);
                    case 6 -> System.out.println("See you soon !");
                    default -> System.out.println("Invalid Choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error, try again");
                scanner.nextLine();
            }

        } while (choix != 6);
    }

    

    // Menu Player
    private static void menuPlayeur(Scanner scanner) {
        System.out.println("\n--- Player Actions ---");
        System.out.println("1. Log in with an existing email");
        System.out.println("2. Create a new account");
        System.out.println("3. Leave");
        System.out.print("Your choice : ");
        int choixConnexion = scanner.nextInt();
        scanner.nextLine();
    
        switch (choixConnexion) {
            case 1 -> {
                if (!playerConnexion(scanner)) {
                    System.out.println("❌ Connexion failed");
                    return;
                }
                mainMenuPlayer(scanner);
            }
            case 2 -> {
                if (createAccount(scanner)) {
                    mainMenuPlayer(scanner);
                }
            }
            case 3 -> System.out.println("See you soon !");
            default -> System.out.println("Invalid Choice.");
        }
    }
    
    
    public static Player playerConnexion(Scanner scanner, List<Player> players) {
        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String mdp = scanner.nextLine();

        for (Player p : players) {
            if (p.getEmail().equals(email) && p.getPassword().equals(mdp)) {
                System.out.println("✅ Connexion successful !");
                return p;
            }
        }

        System.out.println("❌ Incorrect credentials");
        return null;
    }
    
    // Nouvelle méthode pour créer un compte
    public static void createAccount(Scanner scanner, List<Player> players) {
        System.out.print("Email : ");
        String email = scanner.nextLine();

        // Vérifier si l'email existe déjà
        for (Player p : players) {
            if (p.getEmail().equals(email)) {
                System.out.println("❌ This email address is already in use.");
                return;
            }
        }

        System.out.print("Password : ");
        String mdp = scanner.nextLine();

        Player newPlayer = new Player(email, mdp);
        players.add(newPlayer);
        savePlayer(newPlayer);

        System.out.println("✅ Account created successfully !");
    }
    
    // Nouvelle méthode pour supprimer un compte
    public static void deleteAccount(Scanner scanner, List<Player> players) {
        System.out.print("Email address to delete : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String mdp = scanner.nextLine();

        Player joueurASupprimer = null;

        for (Player p : players) {
            if (p.getEmail().equals(email) && p.getPassword().equals(mdp)) {
                joueurASupprimer = p;
                break;
            }
        }

        if (joueurASupprimer == null) {
            System.out.println("❌ No account matches these credentials..");
            return;
        }

        players.remove(joueurASupprimer);
        saveAllPlayers(players);

        System.out.println("Account deleted successfully.");
    }

    
    // Menu Player
    private static void mainMenuPlayer(Scanner scanner) {
        int choix = 0;
        Portfolio portfolio = Portfolio.loadFromFile(player.getEmail());
    
        do {
            System.out.println("\n--- Main Menu (" + emailClientConnecte + ") ---");
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
                    case 1 -> displayMarketStock(market);
                    case 2 -> buyStock(scanner, market, portfolio);
                    case 3 -> sellStock(scanner);
                    case 4 -> displayCashHoldings(scanner,idClientConnecte);
                    case 5 -> displayHistory(scanner);
                    case 6 -> displayWalletValue(scanner);
                    case 7 -> {
                        System.out.println("Deconnexion...");
                        emailClientConnecte = null;
                        idClientConnecte = 0;
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
    public static void displayMarketStock(Market market) {
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

}