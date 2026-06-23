package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // ✅ CORRIGÉ : Remplacement de 'tp_billetterie' par 'tp-billetterie'
    private static final String URL = "jdbc:mysql://localhost:3306/tp-billetterie?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Paris";
    private static final String USER = "root";
    private static final String PASSWORD = "Biblio2026";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}