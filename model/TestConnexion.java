package model;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnexion {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Connexion OK !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}