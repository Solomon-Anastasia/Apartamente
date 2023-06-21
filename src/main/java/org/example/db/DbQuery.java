package org.example.db;

public class DbQuery {
    public static final String SELECT_ALL_FROM_AGENT = "SELECT * FROM Agent";
    public static final String UPDATE_AGENT = "UPDATE Agent SET nume = ?, prenume = ?, virsta = ?, telefon = ? WHERE codAgent = ?";
    public static final String INSERT_AGENT_QUERY = "INSERT INTO Agent (nume, prenume, virsta, telefon) VALUES (?, ?, ?, ?)";
    public static final String RETREIVE_AGENT_ID_BY_NUME_AND_PRENUME = "SELECT codAgent FROM Agent WHERE nume = ? AND prenume = ?";
    public static final String DELETE_AGENT = "DELETE FROM Agent WHERE nume = ? AND prenume = ?";
    public static final String FIND_AGENT_BY_PHONE_NUMBER = """
            SELECT nume, prenume, telefon, virsta
            FROM Agent
            WHERE telefon = ? AND virsta BETWEEN 20 AND 30""";

    public static final String INSERT_INTO_APARTAMENT = "INSERT INTO Apartament (etaj, nrCamere, pret, metriPatrati, codAgent) VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_APARTAMENT = "DELETE FROM Apartament WHERE etaj = ? AND nrCamere = ? AND pret = ? AND metriPatrati = ?";
    public static final String CALCULATE_AGENT_SALES = """
            SELECT a.nume, a.prenume, SUM(ap.Pret) AS total_vanzari
            FROM Agent a
            JOIN Apartament ap ON a.CodAgent = ap.CodAgent
            GROUP BY a.CodAgent
            ORDER BY total_vanzari DESC
            """;
    public static final String SELECT_APARTAMENTE_ON_FLOOR_4 = """
            SELECT a.etaj, a.nrCamere, a.pret, a.metriPatrati, ag.nume, ag.prenume
            FROM Apartament a
            JOIN Agent ag ON a.codAgent = ag.codAgent
            WHERE nrCamere = 4 AND etaj IN (2, 3)
            """;
    public static final String UPDATE_APARTAMENT = """
            UPDATE Apartament
            SET etaj = ?, nrCamere = ?, pret = ?, metriPatrati = ?
            WHERE etaj = ? AND nrCamere = ? AND pret = ? AND metriPatrati = ?
            """;

    public static final String SELECT_ALL_FROM_APARTAMENT_WITH_AGENT = """
            SELECT a.etaj, a.nrCamere, a.pret, a.metriPatrati, ag.nume, ag.prenume
            FROM Apartament a
            JOIN Agent ag ON a.codAgent = ag.codAgent
            """;
}
