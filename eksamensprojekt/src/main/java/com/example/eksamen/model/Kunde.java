package com.example.eksamen.model;

public class Kunde {

    String name;
    String nummer;

    public Kunde(String name, String nummer) {
        this.name = name;
        this.nummer = nummer;
    }

    public Kunde() {
    }


    public String getName() {
        return name;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "name='" + name + '\'' +
                ", nummer='" + nummer + '\'' +
                '}';
    }
}
