package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Movimiento {
    private long idMovimiento;          // ID único para el movimiento
    private long numeroCuenta;          // Relación con la cuenta asociada al movimiento
    private String tipo;                // "CRÉDITO" o "DÉBITO"
    private double monto;               // Monto del movimiento
    private String descripcion;         // Descripción del movimiento
    private LocalDateTime fecha;        // Fecha y hora del movimiento
    private static int contador = 1;    // Contador estático para generar IDs únicos

    // Constructor para crear un movimiento con fecha
    public Movimiento(long numeroCuenta, String tipo, double monto, String descripcion, LocalDateTime fecha) {
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idMovimiento = contador++; // Asigna un ID único y luego incrementa el contador
    }

    // Constructor vacío (por si se necesita para otras operaciones)
    public Movimiento() { }

    // Getters y Setters
    public long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

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

