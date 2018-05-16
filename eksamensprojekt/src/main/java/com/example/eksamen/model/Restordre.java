package com.example.eksamen.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

public class Restordre {

    Integer restordre_nummer;

    String kunde_nummer =" ";
    String kunde_navn = " ";

    String vare_nummer = " ";
    String vare_navn = " ";

    Integer antal_varer = 0;
    String note = " ";


    Date ekspeditions_dato;

    String leverance_navn = " ";
    String leverance_dato = " ";
    Integer aktiv = 1;

    public Restordre() {
    }

    //Fra Resultsettet


    public Restordre(Integer restordre_nummer, String kunde_nummer, String kunde_navn, String vare_nummer, String vare_navn, Integer antal_varer, String note, Date ekspeditions_dato, String leverance_navn, String leverance_dato, Integer aktiv) {
        this.restordre_nummer = restordre_nummer;
        this.kunde_nummer = kunde_nummer;
        this.kunde_navn = kunde_navn;
        this.vare_nummer = vare_nummer;
        this.vare_navn = vare_navn;
        this.antal_varer = antal_varer;
        this.note = note;
        this.ekspeditions_dato = ekspeditions_dato;
        this.leverance_navn = leverance_navn;
        this.leverance_dato = leverance_dato;
        this.aktiv = aktiv;
    }


    public void setRestordre_nummer(Integer restordre_nummer) {
        this.restordre_nummer = restordre_nummer;
    }

    public void setKunde_nummer(String kunde_nummer) {
        this.kunde_nummer = kunde_nummer;
    }

    public void setKunde_navn(String kunde_navn) {
        this.kunde_navn = kunde_navn;
    }

    public void setVare_nummer(String vare_nummer) {
        this.vare_nummer = vare_nummer;
    }

    public void setVare_navn(String vare_navn) {
        this.vare_navn = vare_navn;
    }

    public void setAntal_varer(Integer antal_varer) {
        this.antal_varer = antal_varer;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEkspeditions_dato(Date ekspeditions_dato) {
        this.ekspeditions_dato = ekspeditions_dato;
    }

    public void setLeverance_navn(String leverance_navn) {
        this.leverance_navn = leverance_navn;
    }

    public void setLeverance_dato(String leverance_dato) {
        this.leverance_dato = leverance_dato;
    }

    public void setAktiv(Integer aktiv) {
        this.aktiv = aktiv;
    }

    public String getKunde_navn() {
        return kunde_navn;
    }

    public String getVare_navn() {
        return vare_navn;
    }

    public Integer getRestordre_nummer() {
        return restordre_nummer;
    }

    public String getKunde_nummer() {
        return kunde_nummer;
    }

    public String getVare_nummer() {
        return vare_nummer;
    }

    public Integer getAntal_varer() {
        return antal_varer;
    }

    public String getLeverance_navn() {
        return leverance_navn;
    }

    public String getLeverance_dato() {
        return leverance_dato;
    }

    public String getNote() {
        return note;
    }

    public Date getEkspeditions_dato() {
        return ekspeditions_dato;
    }

    public Integer getAktiv() {
        return aktiv;
    }

    @Override
    public String toString() {
        return "restordre_nummer?=" + restordre_nummer + '&' +
                "kunde_nummer?=" + kunde_nummer + '&' +
                "kunde_navn?=" + kunde_navn + '&' +
                "vare_nummer?=" + vare_nummer + '&' +
                "vare_navn?=" + vare_navn + '&' +
                "antal_varer?=" + antal_varer +
                "leverance_navn?=" + leverance_navn + '&' +
                "leverance_dato?=" + leverance_dato + '&' +
                "note?=" + note + '&' +
                "ekspeditions_dato?=" + ekspeditions_dato + '&' +
                "aktiv?=" + aktiv;
    }
}
