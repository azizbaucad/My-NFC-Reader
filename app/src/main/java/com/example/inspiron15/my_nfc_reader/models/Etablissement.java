package com.example.inspiron15.my_nfc_reader.models;

/**
 * Created by bass on 04/03/2019.
 */

public class Etablissement {


    public int id;

    public String name;

    public Etablissement() {

    }

    public Etablissement(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
