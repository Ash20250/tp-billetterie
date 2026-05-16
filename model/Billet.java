package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Billet {
    private int id_billet;
    private int id_client;
    private int id_spectacle;
    private LocalDateTime date_achat;
    private String place;
    private String statut;
    private double prix_paye;

    // Ajout pour l'affichage Pro dans la TableView
    private String titreSpectacle;

    // Constructeur Standard (utilisé pour l'ajout en BDD)
    public Billet(int id_client, int id_spectacle, LocalDateTime date_achat, String place, String statut, double prix_paye) {
        this.id_client = id_client;
        this.id_spectacle = id_spectacle;
        this.date_achat = date_achat;
        this.place = place;
        this.statut = statut;
        this.prix_paye = prix_paye;
    }

    // Constructeur via Objets (Séance / Client)
    public Billet(int id_billet, Seance seance, Client client, double prix_paye, LocalDate date_achat) {
        this.id_billet = id_billet;
        this.id_client = client.getId_client();
        this.id_spectacle = seance.getSpectacle().getId_spectacle();
        this.titreSpectacle = seance.getSpectacle().getTitre(); // Récupération auto du titre
        this.date_achat = date_achat.atStartOfDay();
        this.place = "Non attribuée";
        this.statut = "Réservé";
        this.prix_paye = prix_paye;
    }

    // --- Getters ---
    public int getId_billet() { return id_billet; }
    public int getId_client() { return id_client; }
    public int getId_spectacle() { return id_spectacle; }
    public LocalDateTime getDate_achat() { return date_achat; }
    public String getPlace() { return place; }
    public String getStatut() { return statut; }
    public double getPrix_paye() { return prix_paye; }
    public String getTitreSpectacle() { return titreSpectacle; }

    // Alias pour compatibilité avec tes anciens composants
    public double getPrix() { return prix_paye; }
    public LocalDateTime getDateAchat() { return date_achat; }

    // --- Setters ---
    public void setId_billet(int id_billet) { this.id_billet = id_billet; }
    public void setId_client(int id_client) { this.id_client = id_client; }
    public void setId_spectacle(int id_spectacle) { this.id_spectacle = id_spectacle; }
    public void setDate_achat(LocalDateTime date_achat) { this.date_achat = date_achat; }
    public void setPlace(String place) { this.place = place; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setPrix_paye(double prix_paye) { this.prix_paye = prix_paye; }
    public void setTitreSpectacle(String titreSpectacle) { this.titreSpectacle = titreSpectacle; }

    // Alias Setters
    public void setPrix(double prix) { this.prix_paye = prix; }
    public void setDateAchat(LocalDateTime dateAchat) { this.date_achat = dateAchat; }

    @Override
    public String toString() {
        return String.format("Billet #%d | %s | Place: %s | Statut: %s | %.2f €",
                id_billet,
                (titreSpectacle != null ? titreSpectacle : "Spectacle ID: " + id_spectacle),
                place,
                statut,
                prix_paye);
    }
}