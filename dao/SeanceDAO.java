package dao;

import model.Seance;
import model.Spectacle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    private final SpectacleDAO spectacleDAO = new SpectacleDAO();

    public List<Seance> getAllSeances() {
        List<Seance> seances = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id_seance, id_spectacle, date_seance, heure_seance FROM seance";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Spectacle spectacle = spectacleDAO.getSpectacleById(rs.getInt("id_spectacle"));
                Seance seance = new Seance(
                        rs.getInt("id_seance"),
                        spectacle,
                        rs.getDate("date_seance").toLocalDate(),
                        rs.getTime("heure_seance").toLocalTime()
                );
                seances.add(seance);
            }
        } catch (Exception e) {
            System.out.println("Erreur getAllSeances : " + e.getMessage());
        }
        return seances;
    }

    public List<Seance> getSeancesBySpectacle(int idSpectacle) {
        List<Seance> seances = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id_seance, date_seance, heure_seance FROM seance WHERE id_spectacle = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idSpectacle);
            ResultSet rs = stmt.executeQuery();

            Spectacle spectacle = spectacleDAO.getSpectacleById(idSpectacle);
            while (rs.next()) {
                Seance seance = new Seance(
                        rs.getInt("id_seance"),
                        spectacle,
                        rs.getDate("date_seance").toLocalDate(),
                        rs.getTime("heure_seance").toLocalTime()
                );
                seances.add(seance);
            }
        } catch (Exception e) {
            System.out.println("Erreur getSeancesBySpectacle : " + e.getMessage());
        }
        return seances;
    }
}