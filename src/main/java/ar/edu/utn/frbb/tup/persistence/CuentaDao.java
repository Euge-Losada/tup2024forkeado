package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;

import java.util.List;

public interface CuentaDao {
    void save(Cuenta cuenta);
    Cuenta find(long numeroCuenta);
    List<Cuenta> getCuentasByCliente(long dniTitular);
}