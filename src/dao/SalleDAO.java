package dao;

import model.Salle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {

    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM salle";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salle salle = new Salle(
                        rs.getInt("id_salle"),
                        rs.getString("nom"),       // ✅ colonne réelle
                        rs.getInt("capacite")
                );
                salles.add(salle);
            }
        } catch (Exception e) {
            System.out.println("Erreur getAllSalles : " + e.getMessage());
        }
        return salles;
    }

    public Salle getSalleById(int id) {
        Salle salle = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM salle WHERE id_salle = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                salle = new Salle(
                        rs.getInt("id_salle"),
                        rs.getString("nom"),
                        rs.getInt("capacite")
                );
            }
        } catch (Exception e) {
            System.out.println("Erreur getSalleById : " + e.getMessage());
        }
        return salle;
    }
}