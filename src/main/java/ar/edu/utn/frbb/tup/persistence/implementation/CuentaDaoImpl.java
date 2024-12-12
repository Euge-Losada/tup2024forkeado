package ar.edu.utn.frbb.tup.persistence.implementation;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.AbstractBaseDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CuentaDaoImpl extends AbstractBaseDao implements CuentaDao {

    @Autowired
    private ClienteDao clienteDao;

    @Override
    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    public Cuenta find(long numeroCuenta) {
        if (getInMemoryDatabase().get(numeroCuenta) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(numeroCuenta)).toCuenta(clienteDao);
    }

    @Override
    public List<Cuenta> getCuentasByCliente(long dniTitular) {
        return getInMemoryDatabase().values().stream()
                .map(entity -> (CuentaEntity) entity)
                .filter(entity -> entity.getTitular().equals(dniTitular))
                .map(entity -> entity.toCuenta(clienteDao))
                .collect(Collectors.toList());
    }

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }
}