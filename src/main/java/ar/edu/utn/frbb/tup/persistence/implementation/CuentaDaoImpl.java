package ar.edu.utn.frbb.tup.persistence.implementation;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.AbstractBaseDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.stereotype.Repository;

@Repository
public class CuentaDaoImpl extends AbstractBaseDao implements CuentaDao {

    @Override
    public Cuenta find(long numeroCuenta) {
        return (Cuenta) getInMemoryDatabase().get(numeroCuenta);  // Recupera la cuenta por su número
    }

    @Override
    public void save(Cuenta cuenta) {
        getInMemoryDatabase().put(cuenta.getNumeroCuenta(), cuenta);  // Guarda la cuenta en memoria
    }

    // Implementación del método getEntityName() de AbstractBaseDao
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }
}
