# 🎫 Application de Billetterie - Java Desktop

![Java](https://img.shields.io/badge/Language-Java-red)
![JavaFX](https://img.shields.io/badge/Framework-JavaFX-blue)
![MySQL](https://img.shields.io/badge/DB-MySQL-lightgrey)

## 📝 Présentation du Projet
Cette application "Client Lourd" permet la gestion et la réservation de billets pour des spectacles. Elle a été développée en **Java** avec une architecture **MVC** (Modèle-Vue-Contrôleur).

## 🛠️ Étapes de Réalisation

### TP 1 : Analyse et Persistance des données
* **Conception UML :** Création du diagramme de classes avec **PlantUML**.
    * Gestion des entités : `Spectacle`, `Salle`, `Seance`, `Client`, `Billet`.
* **Base de données :** Mise en place d'une base **MySQL**.
    * Scripts fournis : `billeterie.sql` (structure) et `data.sql` (jeu d'essai de ~100 billets).

### TP 2 : Veille et Benchmark Technique
Étude comparative des frameworks GUI Java pour choisir l'outil le plus adapté au projet :
* **Comparatif :** JavaFX vs Swing vs SWT vs Vaadin.
* **Critères :** Performance, modernité du rendu, facilité de maintenance (FXML) et documentation.
* **Choix :** **JavaFX**, pour sa séparation claire entre le design (FXML/Scene Builder) et la logique (Java).

### TP 3 : Développement des Interfaces (IHM)
Réalisation des vues ergonomiques via **Scene Builder** :
* **ListeSpectaclesView :** Catalogue dynamique des événements.
* **SpectacleDetailsView :** Fiche détaillée avec récupération des données en temps réel.
* **AchatBilletView :** Formulaire de réservation et génération de billets.

## 🚀 Spécifications Techniques
- **Langage :** Java 17+
- **Interface :** JavaFX & Scene Builder (fichiers .fxml)
- **Accès aux données :** Pattern **DAO** (Data Access Object) avec `PreparedStatement` pour la sécurité (anti-injection SQL).
- **Gestionnaire de dépendances :** Maven / Gradle.

## 📊 Modélisation
