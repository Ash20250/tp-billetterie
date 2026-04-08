package dao;

import model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public Client getLoggedUser(String email, String password) {
        Client client = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM client WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel"),
                        rs.getInt("age"),
                        rs.getString("adresse")
                );
                client.setRole(rs.getString("role"));
            }
        } catch (Exception e) {
            System.out.println("Erreur getLoggedUser : " + e.getMessage());
        }
        return client;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM client";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel"),
                        rs.getInt("age"),
                        rs.getString("adresse")
                );
                client.setRole(rs.getString("role"));
                clients.add(client);
            }
        } catch (Exception e) {
            System.out.println("Erreur getAllClients : " + e.getMessage());
        }
        return clients;
    }

    public Client getClientById(int id) {
        Client client = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM client WHERE id_client = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel"),
                        rs.getInt("age"),
                        rs.getString("adresse")
                );
                client.setRole(rs.getString("role"));
            }
        } catch (Exception e) {
            System.out.println("Erreur getClientById : " + e.getMessage());
        }
        return client;
    }

    public void addClient(Client client) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO client (nom, prenom, email, num_tel, age, adresse, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getNumTel());
            stmt.setInt(5, client.getAge());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, "1234");
            stmt.setString(8, "CLIENT");
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur addClient : " + e.getMessage());
        }
    }

    public void updateClient(Client client) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE client SET nom=?, prenom=?, email=?, num_tel=?, age=?, adresse=?, role=? WHERE id_client=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getNumTel());
            stmt.setInt(5, client.getAge());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, client.getRole());
            stmt.setInt(8, client.getId_client());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur updateClient : " + e.getMessage());
        }
    }

    public void deleteClient(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM client WHERE id_client = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur deleteClient : " + e.getMessage());
        }
    }

    public List<Client> searchClientsByName(String keyword) {
        List<Client> clients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM client WHERE nom LIKE ? OR prenom LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel"),
                        rs.getInt("age"),
                        rs.getString("adresse")
                );
                client.setRole(rs.getString("role"));
                clients.add(client);
            }
        } catch (Exception e) {
            System.out.println("Erreur searchClientsByName : " + e.getMessage());
        }
        return clients;
    }

    public List<Client> getClientsBySpectacle(int idSpectacle) {
        List<Client> clients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT c.* FROM client c " +
                    "JOIN billet b ON c.id_client = b.id_client " +
                    "WHERE b.id_spectacle = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idSpectacle);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel"),
                        rs.getInt("age"),
                        rs.getString("adresse")
                );
                client.setRole(rs.getString("role"));
                clients.add(client);
            }
        } catch (Exception e) {
            System.out.println("Erreur getClientsBySpectacle : " + e.getMessage());
        }
        return clients;
    }
}