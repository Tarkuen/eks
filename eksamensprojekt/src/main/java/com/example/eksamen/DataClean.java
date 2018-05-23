package com.example.eksamen;

import org.springframework.dao.DeadlockLoserDataAccessException;

import java.sql.Connection;
import java.sql.Savepoint;
import java.sql.Statement;

public class DataClean {


    /**
     * Privat access instans af vores databaseforbindelse
     */
    private DBConn databaseConnection;


    /**
     * Statisk instans af klassen, som indgår i vores constructor. Dette gør det muligt at kalde til den samme instans
     * for hver gang klassen skal bruges og derved overholde Singleton princippet.
     */
    private static DataClean instance;

    /**
     * Instantiering af singleton objektet Dataclean, der sikrer kun én instance af objektetet eksisterer.
     * Desuden er den public og static, da man skal kunne kalde den fra alle steder I applikationen (Global Access).
     * @return et  <code>com.example.eksamen.DataClean</code>  objekt.
     */
    public static DataClean getInstance() {
        if (instance == null)
            instance=new DataClean();
        return instance;
    }

    private DataClean() {
        databaseConnection = DBConn.getInstance();
    }

    /**
     * Metode der importerer data fra lokale filer og indsætter distinct resultater i vores to tables 'kundeimport' og 'vareimport'.
     * Begge database kald er en del af én transaction, for at undgå mere end én person importerer fra filerne af gangen.
     */
    public void filer() {

        try(Connection conn = databaseConnection.getConn()){

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            Savepoint sp1= conn.setSavepoint();

            String query=" LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/files/debitorAfterCleanup.txt' INTO TABLE kundeimport FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\r\\n' (kunde_nummer, kunde_navn);";
            try(Statement stmt= conn.createStatement()){
                stmt.execute(query);
            }
            catch (DeadlockLoserDataAccessException ex){
                conn.rollback(sp1);
            }

            Savepoint sp2= conn.setSavepoint();

            String query2=" LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/files/varekartAfterCleanup.txt' INTO TABLE vareimport FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\r\\n' (vare_nummer, vare_navn, vare_leverancenr);";
            try(Statement stmt2= conn.createStatement()){
                stmt2.execute(query2);
            }
            catch (DeadlockLoserDataAccessException ex){
                conn.rollback(sp2);
            }

            conn.commit();

        }

        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Metoder der tjekker om samtlige af vores default schemas og tables eksisteter. Til dette bruges forbindelsen <code>DBConn.getConn1()</code>
     * først, for at tjekke om schemaet 'eksamens_project' eksisterer.
     * Hvis dette er tilfælde, tjekker metoden efterfølgende databasen for vores datatables.
     * Hvis ikke, oprettes schemaet og de fire tables.
     * Alt dette tjekkes ved opstart af applikationen, i <code>com.example.eksamen.EksamensprojectApplication</code>.
     *
     * Ved hver CREATE statement er der desuden indført rollbacks.
     * Disse rollbacks er indført for at undgå deadlocking, i tilfælde af at mere end én udgave startes op samtidigt.
     * Da disse queries er centrale for programmet er rollbacks også på plads for at sikre, at rest af programmet kører korrekt.
     */
    public void table(){

        try(Connection conn=databaseConnection.getConn1()){

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            Savepoint schemeSP = conn.setSavepoint();

            String schemeQuery = "CREATE SCHEMA IF NOT EXISTS  `eksamens_project` /*!40100 DEFAULT CHARACTER SET utf8 */;";
            try (Statement schemeStmt = conn.createStatement()) {
                schemeStmt.execute(schemeQuery);
            } catch (DeadlockLoserDataAccessException ex) {
                conn.rollback(schemeSP);
            }


            conn.commit();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try (Connection conn = databaseConnection.getConn()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            Savepoint sp1 = conn.setSavepoint();

            String query = "CREATE TABLE IF NOT EXISTS `kundeimport` (   `kunde_nummer` VARCHAR(50) NOT NULL,   `kunde_navn` VARCHAR(50) NOT NULL,   PRIMARY KEY (`kunde_nummer`) )  ENGINE=INNODB DEFAULT CHARSET=UTF8;";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(query);
            } catch (DeadlockLoserDataAccessException ex) {
                conn.rollback(sp1);
            }

            Savepoint sp2 = conn.setSavepoint();

            String query1 = "CREATE TABLE IF NOT EXISTS `vareimport` (   `vare_nummer` VARCHAR(50) NOT NULL,   `vare_navn` VARCHAR(50) NOT NULL,   `vare_leverancenr` VARCHAR(50) NOT NULL,   PRIMARY KEY (`vare_nummer`),   KEY `leverance_navn` (`vare_leverancenr`) )  ENGINE=INNODB DEFAULT CHARSET=UTF8";
            try (Statement stmt2 = conn.createStatement()) {
                stmt2.execute(query1);
            } catch (DeadlockLoserDataAccessException ex) {
                conn.rollback(sp2);
            }

            Savepoint sp3 = conn.setSavepoint();

            String query2 = "CREATE TABLE IF NOT EXISTS `vare_leverance` (   `leveranceNavn` VARCHAR(50) NOT NULL,   `leverance_dato` VARCHAR(45) DEFAULT NULL,   PRIMARY KEY (`leveranceNavn`),   CONSTRAINT `fk_leverance_navn` FOREIGN KEY (`leveranceNavn`)   REFERENCES `vareimport` (`vare_leverancenr`)     ON DELETE NO ACTION ON UPDATE NO ACTION )  ENGINE=INNODB DEFAULT CHARSET=UTF8;";
            try (Statement stmt3 = conn.createStatement()) {
                stmt3.execute(query2);
            } catch (DeadlockLoserDataAccessException ex) {
                conn.rollback(sp3);
            }

            Savepoint sp4 = conn.setSavepoint();

            String query3 = "CREATE TABLE IF NOT EXISTS `restordrer` (   `restordre_nummer` SMALLINT(10) NOT NULL AUTO_INCREMENT,   `kunde_nummer` VARCHAR(45) NOT NULL,   `vare_nummer` VARCHAR(50) NOT NULL,   `antal_varer` INT(11) DEFAULT NULL,   `note` VARCHAR(45) DEFAULT NULL,   `ekspeditionsDato` DATE DEFAULT NULL,   `aktiv` TINYINT(4) DEFAULT '1' COMMENT 'boolean value over aktive ordrer',   PRIMARY KEY (`restordre_nummer`) )  ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=UTF8;";
            try (Statement stmt4 = conn.createStatement()) {
                stmt4.execute(query3);
            } catch (DeadlockLoserDataAccessException ex) {
                conn.rollback(sp4);
            }

            conn.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Kunne ikke oprette tables. Fejlkode: "+e.getMessage());
        }
    }

}
