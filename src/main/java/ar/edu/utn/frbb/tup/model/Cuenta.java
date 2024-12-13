package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cuenta {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    @JsonBackReference
    private Cliente titular;
    private TipoMoneda moneda;
    private final List<MovimientoEntity> movimientos;

    public Cuenta(TipoCuenta tipoCuenta, TipoMoneda moneda, double balance) {
        this.numeroCuenta = new Random().nextLong();  // Genera un número de cuenta aleatorio
        this.fechaCreacion = LocalDateTime.now();     // Establece la fecha de creación
        this.tipoCuenta = tipoCuenta;                 // Asigna el tipo de cuenta
        this.moneda = moneda;                         // Asigna la moneda
        this.balance = balance;                       // Asigna el balance inicial
        this.movimientos = new ArrayList<>();         // Inicializa la lista de movimientos
    }


    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public List<MovimientoEntity> getMovimientos() {
        return new ArrayList<>(movimientos); // Retornar copia para evitar modificaciones externas
    }

    public void debitarDeCuenta(double cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar < 0) {
            throw new CantidadNegativaException("No se puede debitar una cantidad negativa.");
        }

        if (balance < cantidadADebitar) {
            throw new NoAlcanzaException("Saldo insuficiente para realizar el débito.");
        }

        balance -= cantidadADebitar;
        agregarMovimiento("DÉBITO", cantidadADebitar, "Débito por transferencia");
    }

    public void acreditarEnCuenta(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("No se puede acreditar una cantidad negativa.");
        }

        balance += monto;
        agregarMovimiento("CRÉDITO", monto, "Crédito por transferencia");
    }

    private void agregarMovimiento(String tipo, double monto, String descripcion) {
        // Crear una instancia de Movimiento con los datos proporcionados
        Movimiento movimiento = new Movimiento(
                this.numeroCuenta, // Número de cuenta
                tipo,              // Tipo de movimiento (CRÉDITO o DÉBITO)
                monto,             // Monto del movimiento
                descripcion,       // Descripción breve del movimiento
                LocalDateTime.now() // Fecha y hora actual
        );

        // Usar Movimiento para crear un MovimientoEntity
        MovimientoEntity movimientoEntity = new MovimientoEntity(movimiento);

        // Agregar el movimiento a la lista
        movimientos.add(movimientoEntity);
    }



    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }
}
