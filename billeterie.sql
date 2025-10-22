CREATE TABLE Salle (
  id_salle INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  ville VARCHAR(100) NOT NULL,
  capacite INT NOT NULL
);

CREATE TABLE Spectacle (
  id_spectacle INT AUTO_INCREMENT PRIMARY KEY,
  id_salle INT NOT NULL,
  titre VARCHAR(150) NOT NULL,
  affiche VARCHAR(255) NOT NULL,
  tags VARCHAR(255),
  duree INT NOT NULL,
  description_courte TEXT NOT NULL,
  description_longue TEXT NOT NULL,
  langue VARCHAR(50) NOT NULL,
  age_minimum INT,
  date_evenement DATE NOT NULL,
  FOREIGN KEY (id_salle) REFERENCES Salle(id_salle)
);

CREATE TABLE Photo (
  id_photo INT AUTO_INCREMENT PRIMARY KEY,
  id_spectacle INT NOT NULL,
  photo VARCHAR(255) NOT NULL,
  FOREIGN KEY (id_spectacle) REFERENCES Spectacle(id_spectacle)
);

CREATE TABLE Seance (
  id_seance INT AUTO_INCREMENT PRIMARY KEY,
  id_spectacle INT NOT NULL,
  date_heure DATETIME NOT NULL,
  prix DECIMAL(8,2) NOT NULL,
  FOREIGN KEY (id_spectacle) REFERENCES Spectacle(id_spectacle)
);

CREATE TABLE Client (
  id_client INT AUTO_INCREMENT PRIMARY KEY,
  prenom VARCHAR(100) NOT NULL,
  nom VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  adresse VARCHAR(255) NOT NULL,
  num_tel VARCHAR(20) NOT NULL
);

CREATE TABLE Historique (
  id_client INT NOT NULL,
  id_spectacle INT NOT NULL,
  PRIMARY KEY (id_client, id_spectacle),
  FOREIGN KEY (id_client) REFERENCES Client(id_client)
  FOREIGN KEY (id_spectacle) REFERENCES Spectacle(id_spectacle)
);

CREATE TABLE Billet (
  id_billet INT AUTO_INCREMENT PRIMARY KEY,
  id_spectacle INT NOT NULL,
  id_client INT NOT NULL,
  date_achat DATE NOT NULL,
  place VARCHAR(50),
  statut VARCHAR(50),
  prix_paye DECIMAL(8,2) NOT NULL,
  FOREIGN KEY (id_spectacle) REFERENCES Spectacle(id_spectacle)
  FOREIGN KEY (id_client) REFERENCES Client(id_client)
);
