package com.example.eksamen.Repository;

import com.example.eksamen.DBConn;
import com.example.eksamen.model.Restordre;

import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class RestordreRepository implements RestordreInterface {

    /**
     * Privat instans af vores forbindelsesobjekt.
     */
    private DBConn databaseConnection;

    /**
     * Statisk instans af klassen, som indgår i vores constructor. Dette gør det muligt at kalde til den samme instans
     * for hver gang klassen skal bruges og derved overholde Singleton princippet.
     */
    private static RestordreRepository instance;

    /**
     * Instantiering af singleton objektet RestordreRepository, der sikrer kun én instance af objektetet eksisterer.
     * Desuden er den public og static, da man skal kunne kalde den fra alle steder I applikationen (Global Access).
     * @return et  <code>com.example.eksamen.RestordreRepository</code>  objekt.
     */
    public static RestordreRepository getInstance() {
        if (instance == null)
            instance = new RestordreRepository();
        return instance;
    }

    //Klassens constructor er nødt til at være public for at Spring.org.boot.Datasource kan finde den

    /**
     * Public Constructor, hvilket strider mod Singleton pattern.
     * Denne constructor er nødt til at være public, da vores springframework smider BeanCreationException
     * da den ikke kan tilgå en "non-visible class" hvilket vores private constructor er.
     * @throws org.springframework.beans.factory.BeanCreationException
     * Hvis constructoren er private, kan Springframework ikke oprette en Bean og definere den som Repository.
     */
    public RestordreRepository() {
        databaseConnection = DBConn.getInstance();
    }

    /**
     * CRUD: Read funktion.
     * Metode der indlæser samtlige restordrerer fra databasen, opretter dem som Restordre objekter
     * og indsætter dem i et <code>java.util.Vector</code> objekt.
     * Herefter sorteres de efter restordre nummer, med <code>sort()</code> metoden fra <code>Java.util.list</code>
     * @return a <code>Java.util.List</code> object
     */
    @Override
    public List<Restordre> readAllRestordrer() {
        List<Restordre> restordreList = new Vector<>();

        try (Connection conn = databaseConnection.getConn()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT restordre_nummer, restordrer.kunde_nummer, kundeimport.kunde_navn, restordrer.vare_nummer, vareimport.vare_navn, antal_varer, note, ekspeditionsDato, vare_leverance.leveranceNavn, vare_leverance.leverance_dato, aktiv   FROM eksamens_project.restordrer   LEFT JOIN kundeimport ON (restordrer.kunde_nummer = kundeimport.kunde_nummer)  LEFT JOIN vareimport ON restordrer.vare_nummer = vareimport.vare_nummer LEFT JOIN vare_leverance ON (vare_leverance.leveranceNavn = vareimport.vare_leverancenr);");

            while (rs.next()) {
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
            restordreList.sort(Comparator.comparing(Restordre::getRestordre_nummer));
            return restordreList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * CRUD: Create funktion.
     * Opretter en restordre og indsætter den i vores database.
     * Selve restordren bliver oprettet under Create.HTML, hvor den bindes i en form.
     * @param restordre
     * Tager et Restordre objekt som parameter, som er blevet bundet til modellen fra vores view Create.HTML.
     */
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

    /**
     * CRUD: Read funktion.
     * Søger efter en restordre i vores database, for at returnere den som et objekt.
     * @param restordreNummer
     * <code>Java.lang.String</code> objekt der henviser til vores primary key i tablet 'restordrer'.
     * @return Returnerer et Restordre objekt
     */
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

    /**
     * CRUD: Update funktion.
     * Metode der opdaterer et restordre objekts information i vores database.
     * Bruges til at udføre rettelser på objektets information.
     * @param restordre
     * Restordre er blevet passed fra en HTML form i et view.
     */
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

    /**
     * CRUD: Delete funktion.
     * Bruges til at slette en restordre fra vores database. Dette gøres ved at den pågæeldende restordres primary key
     * er bundet til modellen vha. en HTML form og derefter indsættes i vores precompiled statement.
     * @param restordreId
     * <code>java.lang.String</code> objekt der er passed fra en HTML form i et view. Repræsenterer objektets primary key
     * i tablet 'restordrer' i databasen.
     */
    @Override
    public void delete(String restordreId) {

        try (Connection conn = databaseConnection.getConn()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            PreparedStatement pstm = conn.prepareStatement("DELETE FROM eksamens_project.restordrer WHERE restordre_nummer=?;");
            pstm.setString(1, restordreId);
            pstm.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }


    /**
     * Metoden modtager et søgeparameter og matcher det mod informationen i samtlige restordrer.
     * Dette gør det nemmere at søge og finde restordrer, da man kan søge på tværs af samtlige parametre.
     * @param search
     * Modtager dette parameter fra viewet 'HomePage' af typen <code>jave.lang.String</code>.
     * @return
     * Returnerer en liste af restordrer, som matcher parameters kriterier.
     */
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



}
