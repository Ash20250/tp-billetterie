package dao;

import model.Salle;
import model.Spectacle;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SpectacleDAO {

    private final SalleDAO salleDAO = new SalleDAO();

    /**
     * ✅ Récupère tous les spectacles avec une capacité bridée à 60
     */
    public List<Spectacle> getAllSpectacles() {
        List<Spectacle> spectacles = new ArrayList<>();
        String sql = "SELECT s.*, " +
                "(60 - (SELECT COUNT(*) FROM billet b WHERE b.id_spectacle = s.id_spectacle AND b.statut != 'Annulé')) as places_libres " +
                "FROM spectacle s";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // L'erreur venait d'ici si getSalleById lance une Exception générale
                Salle salle = salleDAO.getSalleById(rs.getInt("id_salle"));

                Spectacle spectacle = new Spectacle(
                        rs.getInt("id_spectacle"),
                        rs.getString("titre"),
                        (rs.getDate("date_spectacle") != null) ? rs.getDate("date_spectacle").toLocalDate() : null,
                        (rs.getTime("heure_spectacle") != null) ? rs.getTime("heure_spectacle").toLocalTime() : null,
                        salle,
                        rs.getInt("duree"),
                        rs.getString("langue"),
                        rs.getInt("age_minimum")
                );

                spectacle.setPlacesDisponibles(rs.getInt("places_libres"));
                spectacles.add(spectacle);
            }
        } catch (Exception e) { // ✅ Changé de SQLException à Exception pour tout attraper
            System.err.println("Erreur getAllSpectacles : " + e.getMessage());
        }
        return spectacles;
    }

    /**
     * ✅ Récupère un spectacle par ID
     */
    public Spectacle getSpectacleById(int id) {
        Spectacle spectacle = null;
        String sql = "SELECT s.*, " +
                "(60 - (SELECT COUNT(*) FROM billet b WHERE b.id_spectacle = s.id_spectacle AND b.statut != 'Annulé')) as places_libres " +
                "FROM spectacle s " +
                "WHERE s.id_spectacle = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Salle salle = salleDAO.getSalleById(rs.getInt("id_salle"));
                    spectacle = new Spectacle(
                            rs.getInt("id_spectacle"),
                            rs.getString("titre"),
                            (rs.getDate("date_spectacle") != null) ? rs.getDate("date_spectacle").toLocalDate() : null,
                            (rs.getTime("heure_spectacle") != null) ? rs.getTime("heure_spectacle").toLocalTime() : null,
                            salle,
                            rs.getInt("duree"),
                            rs.getString("langue"),
                            rs.getInt("age_minimum")
                    );
                    spectacle.setPlacesDisponibles(rs.getInt("places_libres"));
                }
            }
        } catch (Exception e) { // ✅ Idem ici
            System.err.println("Erreur getSpectacleById : " + e.getMessage());
        }
        return spectacle;
    }

    public boolean addSpectacle(Spectacle spectacle) {
        String sql = "INSERT INTO spectacle (id_salle, titre, date_spectacle, heure_spectacle, duree, langue, age_minimum) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, spectacle.getSalle().getId_salle());
            stmt.setString(2, spectacle.getTitre());
            stmt.setDate(3, Date.valueOf(spectacle.getDate_spectacle()));
            stmt.setTime(4, Time.valueOf(spectacle.getHeure_spectacle()));
            stmt.setInt(5, spectacle.getDuree());
            stmt.setString(6, spectacle.getLangue());
            stmt.setInt(7, spectacle.getAge_minimum());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur addSpectacle : " + e.getMessage());
            return false;
        }
    }

    public void updateSpectacle(Spectacle spectacle) {
        String sql = "UPDATE spectacle SET id_salle=?, titre=?, date_spectacle=?, heure_spectacle=?, duree=?, langue=?, age_minimum=? WHERE id_spectacle=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, spectacle.getSalle().getId_salle());
            stmt.setString(2, spectacle.getTitre());
            stmt.setDate(3, Date.valueOf(spectacle.getDate_spectacle()));
            stmt.setTime(4, Time.valueOf(spectacle.getHeure_spectacle()));
            stmt.setInt(5, spectacle.getDuree());
            stmt.setString(6, spectacle.getLangue());
            stmt.setInt(7, spectacle.getAge_minimum());
            stmt.setInt(8, spectacle.getId_spectacle());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Erreur updateSpectacle : " + e.getMessage());
        }
    }

    public boolean deleteSpectacle(int idSpectacle) {
        String sql = "DELETE FROM spectacle WHERE id_spectacle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSpectacle);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur deleteSpectacle : " + e.getMessage());
            return false;
        }
    }

    public boolean isSalleOccupee(int idSalle, LocalDate date, LocalTime heure, int idSpectacleExclu) {
        String sql = "SELECT COUNT(*) FROM spectacle WHERE id_salle = ? AND date_spectacle = ? AND heure_spectacle = ? AND id_spectacle != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSalle);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setTime(3, Time.valueOf(heure));
            pstmt.setInt(4, idSpectacleExclu);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur isSalleOccupee : " + e.getMessage());
        }
        return false;
    }
}