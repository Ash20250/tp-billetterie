package dao;

import model.Billet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BilletDAO {

    /**
     * ✅ Vérifie si une place est déjà occupée pour un spectacle spécifique
     */
    public boolean isPlaceOccupee(int idSpectacle, String place) {
        String sql = "SELECT COUNT(*) FROM billet WHERE id_spectacle = ? AND place = ? AND statut != 'Annulé'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSpectacle);
            stmt.setString(2, place);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur isPlaceOccupee : " + e.getMessage());
        }
        return false;
    }

    /**
     * ✅ Ajoute un billet avec gestion correcte des exceptions
     */
    public boolean addBillet(Billet billet) {
        if (isPlaceOccupee(billet.getId_spectacle(), billet.getPlace())) {
            System.err.println("Tentative d'achat d'une place déjà occupée.");
            return false;
        }

        String sql = "INSERT INTO billet (id_client, id_spectacle, date_achat, place, statut, prix_paye) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, billet.getId_client());
            stmt.setInt(2, billet.getId_spectacle());
            stmt.setTimestamp(3, Timestamp.valueOf(billet.getDate_achat()));
            stmt.setString(4, billet.getPlace());
            stmt.setString(5, billet.getStatut());
            stmt.setDouble(6, billet.getPrix_paye());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        billet.setId_billet(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur addBillet : " + e.getMessage());
        }
        return false;
    }

    /**
     * ✅ NOUVEAU : Supprime un billet de la base (Annulation)
     * Utilisé par MesReservationsController
     */
    public boolean deleteBillet(int idBillet) {
        String sql = "DELETE FROM billet WHERE id_billet = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBillet);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur deleteBillet : " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Récupère TOUS les billets (Vue Admin)
     */
    public List<Billet> getAllBillets() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.*, s.titre FROM billet b " +
                "JOIN spectacle s ON b.id_spectacle = s.id_spectacle";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                billets.add(mapResultSetToBillet(rs));
            }
        } catch (Exception e) {
            System.err.println("Erreur getAllBillets : " + e.getMessage());
        }
        return billets;
    }

    /**
     * ✅ Récupère les réservations d'un client (Vue "Mes Billets")
     */
    public List<Billet> getBilletsByClient(int idClient) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.*, s.titre FROM billet b " +
                "JOIN spectacle s ON b.id_spectacle = s.id_spectacle " +
                "WHERE b.id_client = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    billets.add(mapResultSetToBillet(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur getBilletsByClient : " + e.getMessage());
        }
        return billets;
    }

    /**
     * ✅ Récupère les participants d'un spectacle
     */
    public List<Billet> getBilletsBySpectacle(int idSpectacle) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.*, c.nom, c.prenom FROM billet b " +
                "JOIN client c ON b.id_client = c.id_client " +
                "WHERE b.id_spectacle = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSpectacle);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Billet b = mapResultSetToBillet(rs);
                    b.setTitreSpectacle(rs.getString("prenom") + " " + rs.getString("nom"));
                    billets.add(b);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur getBilletsBySpectacle : " + e.getMessage());
        }
        return billets;
    }

    /**
     * 🛠 Méthode utilitaire de mapping
     */
    private Billet mapResultSetToBillet(ResultSet rs) throws SQLException {
        Billet billet = new Billet(
                rs.getInt("id_client"),
                rs.getInt("id_spectacle"),
                rs.getTimestamp("date_achat").toLocalDateTime(),
                rs.getString("place"),
                rs.getString("statut"),
                rs.getDouble("prix_paye")
        );
        billet.setId_billet(rs.getInt("id_billet"));

        try {
            String titre = rs.getString("titre");
            if (titre != null) {
                billet.setTitreSpectacle(titre);
            }
        } catch (SQLException e) {
            // Pas de colonne titre
        }
        return billet;
    }
}