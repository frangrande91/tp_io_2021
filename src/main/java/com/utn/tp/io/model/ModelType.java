package com.utn.tp.io.model;

public enum ModelType {
    Q_MODEL("Q_MODEL"),
    P_MODEL("P_MODEL");

    private String name;

    ModelType (String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }
}
