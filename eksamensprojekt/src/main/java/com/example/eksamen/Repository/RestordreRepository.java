package com.example.eksamen.Repository;


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
        List<Restordre> restordreList = new ArrayList<>();

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
        Restordre r = null;
        List<Restordre> restordreList = null;
        List<Restordre> newList = new ArrayList<>();

        try (Connection conn = databaseConnection.getConn()) {

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            restordreList = readAllRestordrer();

            for (Restordre restordre : restordreList) {
                if(restordre.getKunde_nummer().matches(search)) {
                    r=read(restordre.getRestordre_nummer().toString());
                    newList.add(r);}
               else if (restordre.getVare_nummer().matches(search)){
                    r= read(restordre.getRestordre_nummer().toString());
                    newList.add(r);}
               else if(restordre.getLeverance_navn().matches(search)){
                    r=read(restordre.getRestordre_nummer().toString());
                    newList.add(r);}
                }

            conn.commit();
        return newList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restordreList;
    }
}
