package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cuenta {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    private Cliente titular;
    private TipoMoneda moneda;
    private final List<MovimientoEntity> movimientos;

    public Cuenta() {
        this.numeroCuenta = new Random().nextLong();
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.movimientos = new ArrayList<>();
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
        MovimientoEntity movimiento = new MovimientoEntity(
                this.numeroCuenta,  // Número de cuenta asociado al movimiento
                tipo,
                monto,
                descripcion,
                LocalDateTime.now()
        );
        movimientos.add(movimiento);
    }


    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }
}
