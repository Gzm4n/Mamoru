package com.mamoru.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//clase de valores de estado del mamoru
public class MamoruStatus implements Serializable { //hace de la clase un objeto serializable, es decir, la convierte en una secuencia de bytes
    private int hunger;
    private int energy;
    private int hygiene;
    private LocalDateTime lastUpdated;

    //constructor
    public MamoruStatus() {
        this.hunger = 50;
        this.energy = 50;
        this.hygiene = 50;
        this.lastUpdated = LocalDateTime.now();
    }

    //getters and setters
    public int getHunger() {
        return hunger;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHygiene() {
        return hygiene;
    }

    //actualiza valores de los atributos y el tiempo real al realizar la accion de alimentar
    public void feed() {
        hunger = Math.min(100, hunger + 10);
        updateTime();
    }

    //lo mismo pero con jugar
    public void play() {
        energy = Math.max(0, energy - 10);
        hygiene = Math.max(0, hygiene - 5);
        updateTime();
    }

    //dormir
    public void sleep() {
        energy = Math.min(100, energy + 15);
        updateTime();
    }

    //limpiar
    public void clean() {
        hygiene = Math.min(100, hygiene + 20);
        updateTime();
    }

    //es el metodo que baja los valores segun el tiempo, con un maximo de 0
    public void decay() {
        hunger = Math.max(0, hunger - 2);
        energy = Math.max(0, energy - 1);
        hygiene = Math.max(0, hygiene - 1);
        updateTime();
    }

    //añade la respectiva disminucion de valores segun el tiempo que se dejó de jugar
    public void applyTimeDecay() {
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = ChronoUnit.MINUTES.between(lastUpdated, now);

        int intervals = (int) (minutesPassed / 30); // decay cada 30 minutos
        if (intervals > 0) {
            hunger = Math.max(0, hunger - 2 * intervals);
            energy = Math.max(0, energy - 1 * intervals);
            hygiene = Math.max(0, hygiene - 1 * intervals);
            lastUpdated = now;
        }
    }

    //actualiza el tiempo
    private void updateTime() {
        lastUpdated = LocalDateTime.now();
    }
}
