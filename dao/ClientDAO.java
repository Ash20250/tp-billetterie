package dao;

import model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private final String url = "jdbc:mysql://localhost:3306/billeterie";
    private final String user = "root";
    private final String password = "";

    /**
     * ✅ REQUIS par LoginViewController (Ligne 33)
     * Vérifie les identifiants et retourne l'objet Client correspondant.
     */
    public Client getLoggedUser(String email, String mdp) {
        String query = "SELECT * FROM client WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, mdp);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ✅ REQUIS par ClientFormController
     * Met à jour un client existant.
     */
    public boolean updateClient(Client client) {
        String query = "UPDATE client SET nom = ?, prenom = ?, email = ?, num_tel = ?, age = ?, adresse = ?, role = ? WHERE id_client = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getNumTel());
            pstmt.setInt(5, client.getAge());
            pstmt.setString(6, client.getAdresse());
            pstmt.setString(7, client.getRole());
            pstmt.setInt(8, client.getId_client());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ REQUIS par ListeBilletsViewController
     */
    public Client getClientById(int id) {
        String query = "SELECT * FROM client WHERE id_client = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addClient(Client client) {
        String query = "INSERT INTO client (nom, prenom, email, num_tel, age, adresse, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getNumTel());
            pstmt.setInt(5, client.getAge());
            pstmt.setString(6, client.getAdresse());
            pstmt.setString(7, client.getRole());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean deleteClient(int id) {
        String query = "DELETE FROM client WHERE id_client = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Centralisation du mapping pour éviter les erreurs de colonnes
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id_client"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("num_tel"),
                rs.getInt("age"),
                rs.getString("adresse"),
                rs.getString("role")
        );
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}