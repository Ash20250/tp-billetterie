package dao;

import model.Billet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BilletDAO {

    public void addBillet(Billet billet) {
        String sql = "INSERT INTO billet (id_client, id_spectacle, date_achat, place, statut, prix_paye) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, billet.getId_client());
            stmt.setInt(2, billet.getId_spectacle());
            stmt.setTimestamp(3, Timestamp.valueOf(billet.getDate_achat()));
            stmt.setString(4, billet.getPlace());
            stmt.setString(5, billet.getStatut());
            stmt.setDouble(6, billet.getPrix_paye());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                billet.setId_billet(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL addBillet : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur addBillet (connexion) : " + e.getMessage());
        }
    }

    public List<Billet> getAllBillets() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM billet";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Billet billet = new Billet(
                        rs.getInt("id_client"),
                        rs.getInt("id_spectacle"),
                        rs.getTimestamp("date_achat").toLocalDateTime(),
                        rs.getString("place"),
                        rs.getString("statut"),
                        rs.getDouble("prix_paye")
                );
                billet.setId_billet(rs.getInt("id_billet"));
                billets.add(billet);
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL getAllBillets : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur getAllBillets (connexion) : " + e.getMessage());
        }
        return billets;
    }

    public List<Billet> getBilletsByClient(int idClient) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM billet WHERE id_client = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Billet billet = new Billet(
                        rs.getInt("id_client"),
                        rs.getInt("id_spectacle"),
                        rs.getTimestamp("date_achat").toLocalDateTime(),
                        rs.getString("place"),
                        rs.getString("statut"),
                        rs.getDouble("prix_paye")
                );
                billet.setId_billet(rs.getInt("id_billet"));
                billets.add(billet);
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL getBilletsByClient : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur getBilletsByClient (connexion) : " + e.getMessage());
        }
        return billets;
    }
}