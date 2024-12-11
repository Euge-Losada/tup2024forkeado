package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuentaDao  extends AbstractBaseDao{

    @Autowired
    private ClienteDao clienteDao;

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        if (cuenta.getTitular() == null) {
            throw new BusinessLogicException("La cuenta debe tener un titular.");
        }
        if (cuenta.getMoneda() == null) {
            throw new BusinessLogicException("No se puede guardar una cuenta sin moneda.");
        }
        if (cuenta.getNumeroCuenta() <= 0) {
            throw new BusinessLogicException("Número de cuenta inválido al guardar la cuenta.");
        }

        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
        System.out.println("Cuenta guardada con ID: " + entity.getId()); // Agregar log de verificación
    }



    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta(clienteDao);
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular() != null && cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta(clienteDao));
            }
        }
        return cuentasDelCliente;
    }
}
