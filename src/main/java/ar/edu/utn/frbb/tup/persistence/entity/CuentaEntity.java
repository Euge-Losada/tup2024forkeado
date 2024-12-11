package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;


import java.time.LocalDateTime;

public class CuentaEntity extends BaseEntity{
    String nombre;
    LocalDateTime fechaCreacion;
    double balance;
    String tipoCuenta;
    Long titular;
    long numeroCuenta;
    String tipoMoneda;


    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta());

        if (cuenta.getTitular() == null) {
            throw new IllegalArgumentException("La cuenta debe tener un titular.");
        }

        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.titular = cuenta.getTitular().getDni();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.tipoMoneda = cuenta.getMoneda().toString();
    }


    public Cuenta toCuenta(ClienteDao clienteDao) {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(this.balance);
        cuenta.setNumeroCuenta(this.numeroCuenta);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setMoneda(TipoMoneda.valueOf(this.tipoMoneda));

        // Buscar el Cliente asociado al titular (DNI) y asignarlo
        if (this.titular != null) {
            Cliente clienteTitular = clienteDao.find(this.titular, true);  // Buscar Cliente usando ClienteDao
            if (clienteTitular == null) {

                throw new BusinessLogicException("No se encontró un cliente con el DNI: " + this.titular);
            }
            cuenta.setTitular(clienteTitular);  // Asignar Cliente completo a la cuenta
        } else {
            cuenta.setTitular(null);
        }


        return cuenta;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Long getTitular() {
        return titular;
    }

    public void setTitular(Long titular) {
        this.titular = titular;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setMoneda(String tipoMoneda) { this.tipoMoneda = tipoMoneda; }


}
