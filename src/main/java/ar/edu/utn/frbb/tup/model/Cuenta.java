package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.*;

public class Cuenta {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    @JsonBackReference
    private Cliente titular;
    private TipoMoneda moneda;
    private final List<MovimientoEntity> movimientos;
    private static final Set<Long> numerosDeCuentaUsados = new HashSet<>();

    public Cuenta(TipoCuenta tipoCuenta, TipoMoneda moneda, double balance) {
        this.numeroCuenta = generarNumeroCuentaUnico();   // Generar número único de cuenta
        this.fechaCreacion = LocalDateTime.now();         // Fecha de creación actual
        this.tipoCuenta = tipoCuenta;                     // Tipo de cuenta
        this.moneda = moneda;                             // Moneda
        this.balance = balance;                           // Balance inicial
        this.movimientos = new ArrayList<>();             // Lista de movimientos
    }

    public long generarNumeroCuentaUnico() {
        Random random = new Random();
        long numeroCuenta;

        do {
            numeroCuenta = 100_000_000L + random.nextInt(900_000_000); // Número entre 100000000 y 999999999
        } while (numerosDeCuentaUsados.contains(numeroCuenta)); // Verifica duplicados

        numerosDeCuentaUsados.add(numeroCuenta); // Registra el número generado
        return numeroCuenta;
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

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<MovimientoEntity> getMovimientos() {
        return new ArrayList<>(movimientos); // Retornar copia para evitar modificaciones externas
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
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
        Movimiento movimiento = new Movimiento(
                this.numeroCuenta, // Número de cuenta
                tipo,              // Tipo de movimiento (CRÉDITO o DÉBITO)
                monto,             // Monto del movimiento
                descripcion,       // Descripción breve del movimiento
                LocalDateTime.now() // Fecha y hora actual
        );

        MovimientoEntity movimientoEntity = new MovimientoEntity(movimiento);
        movimientos.add(movimientoEntity);
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta=" + numeroCuenta +
                ", tipoCuenta=" + tipoCuenta +
                ", moneda=" + moneda +
                ", balance=" + balance +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
