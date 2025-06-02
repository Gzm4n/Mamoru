package com.mamoru.model;

import java.io.Serializable;

//clase de guardado de datos
public class MamoruSaveData implements Serializable { //clase serializable(explicado en MamoruStatus)
    private static final long serialVersionUID = 1L; //especifica la clase para reconstruir de forma segura
    //atributos
    private final String name;
    private final String type;
    private final MamoruStatus status;

    //constructor
    public MamoruSaveData(String name, String type, MamoruStatus status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public MamoruStatus getStatus() {
        return status;
    }
}
