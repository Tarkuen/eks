package com.example.eksamen.Repository;


import com.example.eksamen.model.Restordre;

import java.util.List;

public interface RestordreInterface {


    List<Restordre> readAllRestordrer();
    void create(Restordre restordre);
    Restordre read(String restordreId);
    void update(Restordre restordre);

    void delete(Integer restordreID);
    List<Restordre> search(String search);
    void table();
    void filer();

}
