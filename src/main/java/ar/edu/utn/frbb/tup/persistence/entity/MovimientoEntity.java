package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Movimiento;

import java.time.LocalDateTime;

public class MovimientoEntity extends BaseEntity {
    private long numeroCuenta;    // Relación directa con la cuenta
    private String tipo;
    private double monto;
    private String descripcion;
    private LocalDateTime fecha;

    public MovimientoEntity(Movimiento movimiento) {
        super(movimiento.getIdMovimiento()); // Asigna el ID desde Movimiento
        this.numeroCuenta = movimiento.getNumeroCuenta();  // Asigna el número de cuenta desde Movimiento
        this.tipo = movimiento.getTipo();                  // Asigna el tipo desde Movimiento
        this.monto = movimiento.getMonto();                // Asigna el monto desde Movimiento
        this.descripcion = movimiento.getDescripcion();    // Asigna la descripción desde Movimiento
        this.fecha = movimiento.getFecha();                // Asigna la fecha desde Movimiento
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
