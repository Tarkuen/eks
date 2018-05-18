package com.example.eksamen.Repository;

import com.example.eksamen.DBConn;
import com.example.eksamen.model.Restordre;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class RestordreRepository implements RestordreInterface {

    private DBConn databaseConnection;

    //Her opretter vi den som Singleton, da vi helst undgår mere end én åben DBforbindelse til DML
    private static RestordreRepository instance;

    public static RestordreRepository getInstance() {
        if (instance == null)
            instance = new RestordreRepository();
        return instance;
    }

    //Klassens constructor er nødt til at være public for at Spring.org.boot.Datasource kan finde den
    public RestordreRepository() {
        databaseConnection = DBConn.getInstance();
    }

    @Override
    public List<Restordre> readAllRestordrer() {
        List<Restordre> restordreList = new Vector<>();

        try (Connection conn = databaseConnection.getConn()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT restordre_nummer, restordrer.kunde_nummer, kundeimport.kunde_navn, restordrer.vare_nummer, vareimport.vare_navn, antal_varer, note, ekspeditionsDato, vare_leverance.leveranceNavn, vare_leverance.leverance_dato, aktiv   FROM eksamens_project.restordrer   LEFT JOIN kundeimport ON (restordrer.kunde_nummer = kundeimport.kunde_nummer)  LEFT JOIN vareimport ON restordrer.vare_nummer = vareimport.vare_nummer LEFT JOIN vare_leverance ON (vare_leverance.leveranceNavn = vareimport.vare_leverancenr);");

            while (rs.next()) {
                //Husk at SQL Resultset ikke er zero-indexet.
                int i = 1;

                Restordre r = new Restordre(
                        rs.getInt(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getInt(i++),
                        rs.getString(i++),
                        rs.getDate(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getInt(i));

                restordreList.add(r);
            }
            //Vi sorterer den lige, så det den ikke giver det i forkert rækkefølge i HTML
            restordreList.sort(Comparator.comparing(Restordre::getRestordre_nummer));
            return restordreList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public void create(Restordre restordre) {

        try (Connection conn = databaseConnection.getConn()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO eksamens_project.restordrer(kunde_nummer, vare_nummer, antal_varer,note,aktiv) VALUES(?,?,?,?,?);");
            pstmt.setString(1, restordre.getKunde_nummer());
            pstmt.setString(2, restordre.getVare_nummer());
            pstmt.setInt(3, restordre.getAntal_varer());
            pstmt.setString(4, restordre.getNote());
            pstmt.setInt(5, 1);

            pstmt.execute();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

    }

    @Override
    public Restordre read(String restordreNummer) {
        Restordre r = null;
        ResultSet rs = null;

        try (Connection conn = databaseConnection.getConn()) {
            PreparedStatement pstms = conn.prepareStatement("SELECT restordre_nummer, restordrer.kunde_nummer, kundeimport.kunde_navn, restordrer.vare_nummer, vareimport.vare_navn, antal_varer, note, ekspeditionsDato, vare_leverance.leveranceNavn, vare_leverance.leverance_dato, aktiv   FROM eksamens_project.restordrer LEFT JOIN kundeimport ON (restordrer.kunde_nummer = kundeimport.kunde_nummer)  LEFT JOIN vareimport ON restordrer.vare_nummer = vareimport.vare_nummer LEFT JOIN vare_leverance ON (vare_leverance.leveranceNavn = vareimport.vare_leverancenr) WHERE restordre_nummer=?;");
            pstms.setString(1, restordreNummer);
            rs = pstms.executeQuery();


            if (rs.next()) {
                //Husk at SQL Resultset ikke er zero-indexet.
                int i = 1;

                r = new Restordre(
                        rs.getInt(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getInt(i++),
                        rs.getString(i++),
                        rs.getDate(i++),
                        rs.getString(i++),
                        rs.getString(i++),
                        rs.getInt(i));
                return r;
            }
            return r;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public void update(Restordre restordre) {

        try (Connection conn = databaseConnection.getConn()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE eksamens_project.restordrer SET ekspeditionsDato=?, kunde_nummer=?, vare_nummer=?, antal_varer=?, note=?,  aktiv=? WHERE restordre_nummer=?;");
            pstmt.setDate(1, restordre.getEkspeditions_dato());
            pstmt.setString(2, restordre.getKunde_nummer());
            pstmt.setString(3, restordre.getVare_nummer());
            pstmt.setInt(4, restordre.getAntal_varer());
            pstmt.setString(5, restordre.getNote());
            pstmt.setInt(6, restordre.getAktiv());
            pstmt.setInt(7, restordre.getRestordre_nummer());
            pstmt.executeUpdate();
            conn.commit();


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public void delete(Integer restordreId) {

        try (Connection conn = databaseConnection.getConn()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            PreparedStatement pstm = conn.prepareStatement("DELETE FROM eksamens_project.restordrer WHERE restordre_nummer=?;");
            pstm.setInt(1, restordreId);
            pstm.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public List<Restordre> search(String search) {

        List<Restordre> restordreList = null;
        List<Restordre> newList = new Vector<>();

            restordreList = readAllRestordrer();

            for (Restordre restordre : restordreList) {
                if (restordre.getKunde_nummer().matches(search)) {
                    newList.add(restordre);
                } else if (restordre.getVare_nummer().matches(search)) {
                    newList.add(restordre);
                } else if (restordre.getLeverance_navn().matches(search)) {
                    newList.add(restordre);
                } else if (restordre.getAktiv().toString().matches(search)) {
                    newList.add(restordre);
                }
            }

        return newList;
        }



    public void table(){

        try(Connection conn=databaseConnection.getConn1()){

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String schemeQuery="CREATE SCHEMA IF NOT EXISTS  `eksamens_project` /*!40100 DEFAULT CHARACTER SET utf8 */;";
            Statement schemeStmt=conn.createStatement();
            schemeStmt.execute(schemeQuery);

            conn.commit();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try(Connection conn = databaseConnection.getConn()){
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String query="CREATE TABLE IF NOT EXISTS `kundeimport` (   `kunde_nummer` VARCHAR(50) NOT NULL,   `kunde_navn` VARCHAR(50) NOT NULL,   PRIMARY KEY (`kunde_nummer`) )  ENGINE=INNODB DEFAULT CHARSET=UTF8;";
            Statement stmt = conn.createStatement();

            String query1="CREATE TABLE IF NOT EXISTS `vareimport` (   `vare_nummer` VARCHAR(50) NOT NULL,   `vare_navn` VARCHAR(50) NOT NULL,   `vare_leverancenr` VARCHAR(50) NOT NULL,   PRIMARY KEY (`vare_nummer`),   KEY `leverance_navn` (`vare_leverancenr`) )  ENGINE=INNODB DEFAULT CHARSET=UTF8";
            Statement stmt2 = conn.createStatement();

            String query2="CREATE TABLE IF NOT EXISTS `vare_leverance` (   `leveranceNavn` VARCHAR(50) NOT NULL,   `leverance_dato` VARCHAR(45) DEFAULT NULL,   PRIMARY KEY (`leveranceNavn`),   CONSTRAINT `fk_leverance_navn` FOREIGN KEY (`leveranceNavn`)   REFERENCES `vareimport` (`vare_leverancenr`)     ON DELETE NO ACTION ON UPDATE NO ACTION )  ENGINE=INNODB DEFAULT CHARSET=UTF8;";
            Statement stmt3 = conn.createStatement();

            String query3="CREATE TABLE IF NOT EXISTS `restordrer` (   `restordre_nummer` SMALLINT(10) NOT NULL AUTO_INCREMENT,   `kunde_nummer` VARCHAR(45) NOT NULL,   `vare_nummer` VARCHAR(50) NOT NULL,   `antal_varer` INT(11) DEFAULT NULL,   `note` VARCHAR(45) DEFAULT NULL,   `ekspeditionsDato` DATE DEFAULT NULL,   `aktiv` TINYINT(4) DEFAULT '1' COMMENT 'boolean value over aktive ordrer',   PRIMARY KEY (`restordre_nummer`) )  ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=UTF8;";
            Statement stmt4=conn.createStatement();

            stmt.execute(query);
            stmt2.execute(query1);
            stmt3.execute(query2);
            stmt4.execute(query3);

            conn.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void filer() {

        try(Connection conn = databaseConnection.getConn()){

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String query=" LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/files/debitorAfterCleanup.txt' INTO TABLE kundeimport FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\r\\n' (kunde_nummer, kunde_navn);";
            Statement stmt= conn.createStatement();
            String query2=" LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/files/varekartAfterCleanup.txt' INTO TABLE vareimport FIELDS TERMINATED BY ';' ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\r\\n' (vare_nummer, vare_navn, vare_leverancenr);";
            Statement stmt2= conn.createStatement();

            stmt.execute(query);
            stmt2.execute(query2);

            conn.commit();

        }

        catch (Exception e){
            e.printStackTrace();
        }

    }


}
