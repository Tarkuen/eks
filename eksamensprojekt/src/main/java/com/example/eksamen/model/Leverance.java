package com.example.eksamen.model;

public class Leverance {

    String name;
    String dato;

    public Leverance(String name, String dato) {
        this.name = name;
        this.dato = dato;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }


    @Override
    public String toString() {
        return "Leverance{" +
                "name='" + name + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}
