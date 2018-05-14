package com.example.eksamen.model;

public class Vare {

    String nummer;
    String navn;

    public Vare(String nummer, String navn) {
        this.nummer = nummer;
        this.navn = navn;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    @Override
    public String toString() {
        return "Vare{" +
                "nummer='" + nummer + '\'' +
                ", navn='" + navn + '\'' +
                '}';
    }
}
