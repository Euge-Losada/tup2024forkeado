package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Movimiento {
    private String tipo;         // "CRÉDITO" o "DÉBITO"
    private double monto;        // Monto del movimiento
    private String descripcion;  // Descripción del movimiento
    private LocalDateTime fecha; // Fecha y hora del movimiento

    public Movimiento(String tipo, double monto, String descripcion) {
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public Movimiento() {

    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
