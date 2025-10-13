CREATE DATABASE IF NOT EXISTS tp_billetterie;
USE tp_billetterie;

CREATE TABLE Venue (
    venue_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    city VARCHAR(50),
    capacity INT
);

CREATE TABLE Event (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    date DATE,
    venue_id INT,
    category VARCHAR(50),
    price_eur DECIMAL(6,2)
);

CREATE TABLE Client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    city VARCHAR(50)
);

CREATE TABLE Ticket (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    client_id INT,
    purchase_date DATE,
    seat VARCHAR(20),
    status VARCHAR(20),
    price_paid DECIMAL(6,2)
);
