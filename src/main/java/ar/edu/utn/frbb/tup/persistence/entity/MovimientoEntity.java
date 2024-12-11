package ar.edu.utn.frbb.tup.persistence.entity;

import java.time.LocalDateTime;

public class MovimientoEntity {
    private long numeroCuenta;    // Relación directa con la cuenta
    private String tipo;
    private double monto;
    private String descripcion;
    private LocalDateTime fecha;

    public MovimientoEntity() {}

    public MovimientoEntity(long numeroCuenta, String tipo, double monto, String descripcion, LocalDateTime fecha) {
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

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
