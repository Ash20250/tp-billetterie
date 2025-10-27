Projet Billetterie

À propos du projet :

Ce projet est une application de billetterie pour des spectacles.
L’idée est de pouvoir :

- Voir la liste des spectacles qui vont avoir lieu

- Consulter les détails d’un spectacle (date, salle, prix…)

- Acheter un billet pour une séance précise

Le tout est fait en Java avec JavaFX pour les interfaces graphiques, et une base de données MySQL pour stocker les informations sur les spectacles, les clients et les billets.

TP 1 : Modélisation et base de données
Modélisation

Pour commencer, j’ai créé un diagramme UML avec PlantUML pour représenter toutes les informations du projet :

salle -> les lieux où se déroulent les spectacles

spectacle -> le spectacle lui-même

photo -> les images associées aux spectacles

séance -> les différentes séances pour un spectacle

client -> les personnes qui achètent des billets

billet -> les billets achetés

Le diagramme montre toutes les relations entre ces entités et les clés primaires/étrangères.

Base de données

Ensuite, j’ai créé la base de données MySQL :

La structure de la base est dans billeterie.sql

Les données de test sont dans data.sql (~100 billets, 10 spectacles, 50 clients)

TP 2 : Benchmark des frameworks Java

Avant de commencer à coder l’interface graphique, j’ai pris le temps de choisir le framework Java le plus adapté pour mon projet. Pour ça, j’ai comparé plusieurs options :

JavaFX -> moderne, complet et parfait pour créer des applications desktop.

Swing -> simple et rapide, pratique si je voulais juste prototyper ou tester quelque chose.

SWT -> très performant, mais un peu compliqué à installer et à prendre en main.

Vaadin -> orienté web, donc moins adapté pour une application desktop classique.

Pour faire mon choix, j’ai regardé plusieurs critères : la facilité d’installation, la documentation, la rapidité d’apprentissage, l’intégration avec Java, le rendu visuel, l’organisation du code, la communauté et les performances.

Conclusion :

J’ai décidé d’utiliser JavaFX comme framework principal pour mon projet, car il est moderne et pratique pour créer mon application.

TP 3 : Interfaces graphiques

Pour ce TP, j’ai créé les interfaces graphiques de l’application de billetterie avec JavaFX. L’objectif était de construire des fenêtres fonctionnelles pour gérer les spectacles et les billets.
Fenêtres réalisées

Liste des spectacles (ListeSpectaclesView)
Affiche tous les spectacles disponibles.
Boutons : Voir détails et Acheter un billet pour chaque spectacle.

Détails d’un spectacle (SpectacleDetailsView)
Affiche toutes les informations d’un spectacle : titre, salle, date, prix, durée, description, etc.

Achat d’un billet (AchatBilletView)
Permet de choisir une séance et de saisir les informations du client.
Affiche un message de confirmation une fois le billet acheté.

Fonctionnement :

Les boutons ouvrent les fenêtres correspondantes, ce qui permet de naviguer entre les différentes vues.
Les informations affichées viennent de la base de données.
