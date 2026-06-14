package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {
    private int id_seance;
    private Spectacle spectacle;
    private LocalDate date_seance;
    private LocalTime heure_seance;

    public Seance(int id_seance, Spectacle spectacle, LocalDate date_seance, LocalTime heure_seance) {
        this.id_seance = id_seance;
        this.spectacle = spectacle;
        this.date_seance = date_seance;
        this.heure_seance = heure_seance;
    }

    public int getId_seance() {
        return id_seance;
    }

    public Spectacle getSpectacle() {
        return spectacle;
    }

    public LocalDate getDate_seance() {
        return date_seance;
    }

    public LocalTime getHeure_seance() {
        return heure_seance;
    }

    @Override
    public String toString() {
        return spectacle.getTitre() + " - " + date_seance + " à " + heure_seance;
    }
}