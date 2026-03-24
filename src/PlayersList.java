import java.util.*;
import java.io.*;
import java.time.*;

public class PlayersList {

    private List<Player> players = new ArrayList<>();

    public PlayersList() {
        this.players = loadPlayers();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Boolean addPlayer(Player player) {
        // Vérifier si l'email existe déjà
        String email = player.getEmail();
        for (Player p : players) {
            if (p.getEmail().equals(email)) {
                System.out.println("❌ This email address is already in use.");
                return false;
            }
        }
        players.add(player);
        saveAllPlayers();
        return true;
    }

    public void removePlayer(Player p) {
        players.remove(p);
        saveAllPlayers();
    }

    public Player findPlayer(String email, String password) {
        for (Player p : players) {
            if (p.getEmail().equals(email) && p.getPassword().equals(password)) {
                return p;
            }
        }
        return null;
    }

    private List<Player> loadPlayers() {
        List<Player> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("players.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    list.add(new Player(parts[0], parts[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ No players file found, starting empty.");
        }

        return list;
    }

    public void saveAllPlayers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("players.txt"))) {
            for (Player p : players) {
                pw.println(p.getEmail() + ";" + p.getPassword());
            }
        } catch (Exception e) {
            System.out.println("❌ Error saving players.");
        }
    }
}
