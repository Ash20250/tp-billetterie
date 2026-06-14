# 🎫 TP Billetterie - Application de Gestion de Spectacles

BilletTech est une application de bureau développée en **Java** permettant de gérer la billetterie d'une salle de spectacles. Ce projet a été réalisé dans le cadre de ma deuxième année de **BTS SIO (Solutions Logicielles et Applications Métiers)** afin de valider les compétences liées au développement d'applications lourdes et à la gestion du patrimoine informatique.

---

## 🔑 Identifiants Jury

Pour vous connecter et tester l'application avec les différents niveaux d'accréditation, vous pouvez utiliser les comptes suivants :

| Rôle | Adresse Email | Mot de passe | Fonctionnalités accessibles |
| :--- | :--- | :--- | :--- |
| **Administrateur** | `admin@test.com` | `admin` | Gestion complète (CRUD) des spectacles, utilisateurs et statistiques. |
| **Utilisateur / Client** | `jean.dupont@test.com` | `pass123` | Consultation des spectacles, achat de billets et historique des réservations. |

---

## 🚀 Fonctionnalités principales

* **Gestion des Spectacles :** Ajout, modification, suppression et consultation des spectacles (titre, artiste, date, capacité, prix).
* **Gestion des Réservations :** Achat de billets, calcul automatique des tarifs et mise à jour en temps réel des places disponibles.
* **Authentification Sécurisée :** Espace d'administration restreint pour la gestion des données de la billetterie avec **mots de passe hachés en SHA-256**.
* **Génération de Billets :** Visualisation et impression des informations liées à la réservation.

---

## 🛠️ Technologies & Outils utilisés

* **Langage :** Java (JDK 21)
* **Interface Graphique :** JavaFX (avec fichiers de vue FXML)
* **Architecture :** Pattern **MVC** (Modèle-Vue-Contrôleur) + Couche **DAO** (Data Access Object)
* **Persistance des données :** Base de données relationnelle via **JDBC** (MySQL)
* **Sécurité :** Cryptographie applicative (Hachage des mots de passe avec l'algorithme **SHA-256**)
* **IDE :** IntelliJ IDEA

---

## 📁 Architecture du Projet

Le projet respecte une architecture logicielle stricte pour séparer les responsabilités (Design Pattern MVC et DAO) :

* 📂 **`app`** : Point d'entrée de l'application (Classe `Main` lançaant l'interface JavaFX).
* 📂 **`view`** : Fichiers `.fxml` décrivant les interfaces graphiques et le design.
* 📂 **`controller`** : Gestionnaires d'événements faisant le lien entre les vues et les modèles.
* 📂 **`model`** : Classes métiers (Spectacle, Client, Billet) représentant les données.
* 📂 **`dao`** : Couche d'accès aux données (Data Access Object) contenant les requêtes SQL pour interagir avec la base de données.
* 📂 **`lib`** : Contient les bibliothèques (`.jar`) JavaFX nécessaires à l'exécution de l'application (Gestion manuelle des dépendances).

---

## ⚙️ Installation et Exécution

Pour lancer le projet en local sur votre machine :

1. **Cloner le dépôt :**
   ```bash
   git clone [https://github.com/Ash20250/tp-billetterie.git](https://github.com/Ash20250/tp-billetterie.git)
