package com.example.vehiculoscrud;

public class Vehiculo {
    private int id;
    private String marca;
    private String modelo;
    private String anio;
    private String placa;
    private String color;

    // Constructor completo
    public Vehiculo(int id, String marca, String modelo,
                    String anio, String placa, String color) {
        this.id     = id;
        this.marca  = marca;
        this.modelo = modelo;
        this.anio   = anio;
        this.placa  = placa;
        this.color  = color;
    }

    // Getters
    public int    getId()     { return id; }
    public String getMarca()  { return marca; }
    public String getModelo() { return modelo; }
    public String getAnio()   { return anio; }
    public String getPlaca()  { return placa; }
    public String getColor()  { return color; }

    // Para mostrar en el ListView
    @Override
    public String toString() {
        return "🚗 " + marca + " " + modelo +
                " | Placa: " + placa +
                " | Año: " + anio +
                " | Color: " + color;
    }
}