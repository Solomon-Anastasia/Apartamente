DROP TABLE IF EXISTS Apartament;
DROP TABLE IF EXISTS Agent;

CREATE TABLE Agent
(
    CodAgent INT PRIMARY KEY AUTO_INCREMENT,
    nume     VARCHAR(30) NOT NULL,
    prenume  VARCHAR(30) NOT NULL,
    virsta   INT         NOT NULL,
    telefon  VARCHAR(9)  NOT NULL
);

CREATE TABLE Apartament
(
    CodApartament INT PRIMARY KEY AUTO_INCREMENT,
    etaj          INT    NOT NULL,
    nrCamere      INT    NOT NULL,
    Pret          DOUBLE NOT NULL,
    metriPatrati  DOUBLE NOT NULL,
    CodAgent      INT    NOT NULL,
    FOREIGN KEY (CodAgent) REFERENCES Agent (CodAgent) ON DELETE CASCADE
);

INSERT INTO Agent (nume, prenume, virsta, telefon)
VALUES ('Solomon', 'Anastasia', 27, '072256780'),
       ('Gheorghe', 'Gabriel', 31, '072289765'),
       ('Cristea', 'Cristina', 29, '072267801'),
       ('Radu', 'Robert', 26, '072290865'),
       ('Florea', 'Florin', 33, '072278956');

INSERT INTO Apartament (etaj, nrCamere, pret, metriPatrati, CodAgent)
VALUES (1, 4, 175000, 130, 1),
       (3, 3, 110000, 85, 4),
       (2, 2, 70000, 50, 5),
       (5, 1, 50000, 40, 3),
       (4, 4, 200000, 150, 2);
