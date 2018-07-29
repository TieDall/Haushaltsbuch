# Haushaltsbuch
Ziel des Projektes „Haushaltsbuch“ ist die Entwicklung einer Open Source Webanwendung. Die Anwendung soll es ermöglichen die Finanzen einer Einzelperson oder einer Lebensgemeinschaft übersichtlich und transparent aufzubereiten. Dazu können Zahlungen erfasst werden und Auswertungen gemacht werden.
## Getting Started
### Aufsetzen MySQL-Datenbank
```
DROP DATABASE IF EXISTS Haushaltsbuch;
CREATE DATABASE IF NOT EXISTS Haushaltsbuch;

CREATE TABLE IF NOT EXISTS Category (
  ID INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  is_income BIT(1) NOT NULL,
  is_deleted BIT(1) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE Person (
  ID INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  is_deleted BIT(1) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Payments (
  ID INT NOT NULL AUTO_INCREMENT,
  categoryID INT NOT NULL,
  amount FLOAT NOT NULL,
  personID INT NOT NULL,
  note VARCHAR(250) DEFAULT NULL,
  created DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (ID),
  FOREIGN KEY (categoryID) REFERENCES Category (ID),
  FOREIGN KEY (personID) REFERENCES Person (ID)
); 
```
### Aufsetzen Hibernate
Die Anwendung nutzt das Framework "Hibernate" zur Kommunikation mit einer MySQL-Datenbank. In der Konfigurationsdatei "hibernate.cfg" sind die Parameter der Datenbank einzutragen.
