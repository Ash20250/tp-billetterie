package model;

/**
 * Modèle Client - Gère les données, la compatibilité DAO et l'affichage FXML.
 */
public class Client {
    private int id_client;
    private String nom;
    private String prenom;
    private String email;
    private String numTel;
    private int age;
    private String adresse;
    private String role;
    private String password; // ✅ AJOUT : L'attribut pour stocker le mot de passe

    // 1. CONSTRUCTEUR COMPLET MAJ (9 paramètres - pour le Login, l'Admin et le DAO complet)
    public Client(int id_client, String nom, String prenom, String email, String numTel, int age, String adresse, String role, String password) {
        this.id_client = id_client;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.age = age;
        this.adresse = adresse;
        this.role = (role == null || role.isEmpty()) ? "CLIENT" : role;
        this.password = password; // ✅ AJOUT
    }

    // 2. CONSTRUCTEUR ANCIEN COMPLET (8 paramètres - pour ne pas casser tes autres contrôleurs)
    public Client(int id_client, String nom, String prenom, String email, String numTel, int age, String adresse, String role) {
        this(id_client, nom, prenom, email, numTel, age, adresse, role, "");
    }

    // 3. CONSTRUCTEUR DAO (7 paramètres)
    public Client(int id_client, String nom, String prenom, String email, String numTel, int age, String adresse) {
        this(id_client, nom, prenom, email, numTel, age, adresse, "CLIENT", "");
    }

    // 4. CONSTRUCTEUR SIMPLIFIÉ (4 paramètres)
    public Client(int id_client, String nom, String prenom, String email) {
        this(id_client, nom, prenom, email, "", 0, "", "CLIENT", "");
    }

    // --- GETTERS ---
    public int getId_client() { return id_client; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getNumTel() { return numTel; }

    // Permet à la TableView de lire "num_tel" si ton FXML utilise l'underscore
    public String getNum_tel() { return numTel; }

    public int getAge() { return age; }
    public String getAdresse() { return adresse; }
    public String getRole() { return role; }

    // ✅ AJOUT : Requis par ClientDAO.java ligne 94
    public String getPassword() { return password; }

    // --- SETTERS ---
    public void setId_client(int id_client) { this.id_client = id_client; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setNumTel(String numTel) { this.numTel = numTel; }
    public void setAge(int age) { this.age = age; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setRole(String role) { this.role = role; }

    // ✅ AJOUT
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + role + ")";
    }
}