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

CREATE TABLE IF NOT EXISTS Fixpayments (
  ID INT NOT NULL AUTO_INCREMENT,
  categoryID INT NOT NULL,
  amount FLOAT NOT NULL,
  personID INT NOT NULL,
  note VARCHAR(250) DEFAULT NULL,
  created DATETIME DEFAULT CURRENT_TIMESTAMP,
  regularityID INT NOT NULL,
  PRIMARY KEY (ID)
);

CREATE IF DEFINER=`root`@`localhost` EVENT `fixPaymentsHalfYear` ON SCHEDULE EVERY 6 MONTH STARTS '2018-01-01 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO INSERT INTO `payments`(`categoryID`, `amount`, `personID`, `note`) SELECT categoryID, amount, personID, note FROM fixpayments WHERE regularityID=3

CREATE DEFINER=`root`@`localhost` EVENT `fixPaymentsMonthly` ON SCHEDULE EVERY 1 MONTH STARTS '2018-08-01 00:00:00' ON COMPLETION PRESERVE ENABLE DO INSERT INTO `payments`(`categoryID`, `amount`, `personID`, `note`) SELECT categoryID, amount, personID, note FROM fixpayments WHERE regularityID=1

CREATE DEFINER=`root`@`localhost` EVENT `fixPaymentsQuartal` ON SCHEDULE EVERY 1 QUARTER STARTS '2018-01-01 00:00:00' ON COMPLETION PRESERVE ENABLE DO INSERT INTO `payments`(`categoryID`, `amount`, `personID`, `note`) SELECT categoryID, amount, personID, note FROM fixpayments WHERE regularityID=2

CREATE DEFINER=`root`@`localhost` EVENT `fixPaymentsYear` ON SCHEDULE EVERY 1 YEAR STARTS '2018-01-01 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO INSERT INTO `payments`(`categoryID`, `amount`, `personID`, `note`) SELECT categoryID, amount, personID, note FROM fixpayments WHERE regularityID=4
```

### Aufsetzen Hibernate
Die Anwendung nutzt das Framework "Hibernate" zur Kommunikation mit einer MySQL-Datenbank. In der Konfigurationsdatei "hibernate.cfg" sind die Parameter der Datenbank einzutragen.
## Thanks
* [milanvrekic](https://github.com/milanvrekic/CSS3-Bar-Graphs) für die Bereitstellung von einer CSS basierten Lösung für Balkendiagramme
* [chartjs](https://github.com/chartjs/Chart.js) für die Bereitstellung von einer JavaScript basierten Lösung für Diagramme verschiedener Art
* [Alicia Kruckow](http://aliciakruckow.com/) für das Designen des Logos
* [JQuery](https://github.com/jquery/jquery)
