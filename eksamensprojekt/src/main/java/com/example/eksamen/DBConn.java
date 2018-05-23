package com.example.eksamen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Vores Singleton database forbindelses klasse.
 */
public class DBConn {

    //Vores singleton instance og Strings, som formateres i Constructoren.
    private static DBConn instance;
    private final String CONN_STRING = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSSL=false";
    private final String CONN_STRING1 = "jdbc:mysql://%s:%s/?user=%s&password=%s&useSSL=false";
    private String connString;
    private String connString2;

    /**
     * Formatterer og opretter vores database URL
     * connString2 bruges kun til at tjekke om vores default schema og tables eksisterer.
     * Desuden private, så ingen andre objekter kan instantiere klassen.
     */
    private DBConn() {
        connString = String.format(CONN_STRING, "localhost", "3306", "eksamens_project", "root", "1234");
        connString2 = String.format(CONN_STRING1, "localhost", "3306", "root", "1234");
    }

    /**
     * Instantiering af singleton objektet DBConn, der sikrer kun én instance af objektetet eksisterer.
     * Desuden er den public og static, da man skal kunne kalde den fra alle steder I applikationen (Global Access).
     *
     * @return et <code>com.example.eksamen.DBConn</code> objekt.
     */
    public static DBConn getInstance() {

        if (instance == null)
            instance = new DBConn();
        return instance;
    }

    /**
     * Metode der bruger vores SDK library's definerede JDBC driver, til at etablere en forbindelse til databasen.
     *
     * @return a <code>java.sql.Connection </code> objekt, vha. <code>java.sql.Drivermanager</code> objektet.
     * @throws SQLException Hvis ikke at vore driver er korrekt konfigureret eller at det er en forkert database URL,
     *                      så smider den en <code>SQLException</code>
     */
    public Connection getConn() throws SQLException {
        return DriverManager.getConnection(connString);
    }

    /**
     * Metode der bruger vores SDK library's definerede JDBC driver, til at etablere en forbindelse til databasen.
     *
     * @return a <code>java.sql.Connection </code> objekt, vha. <code>java.sql.Drivermanager</code> objektet.
     * @throws SQLException Hvis ikke at vore driver er korrekt konfigureret eller at det er en forkert database URL,
     *                      så smider den en <code>SQLException</code>
     */
    public Connection getConn1() throws SQLException {
        return DriverManager.getConnection(connString2);
    }


}
