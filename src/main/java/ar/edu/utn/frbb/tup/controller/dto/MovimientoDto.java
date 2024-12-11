package ar.edu.utn.frbb.tup.controller.dto;

import java.time.LocalDateTime;

public class MovimientoDto {
    private String tipo;
    private double monto;
    private String descripcion;
    private LocalDateTime fecha;

    public MovimientoDto(String tipo, double monto, String descripcion, LocalDateTime fecha) {
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
