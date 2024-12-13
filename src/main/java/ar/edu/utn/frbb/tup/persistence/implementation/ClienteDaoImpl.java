package ar.edu.utn.frbb.tup.persistence.implementation;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.AbstractBaseDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ClienteDaoImpl extends AbstractBaseDao implements ClienteDao {

    @Autowired
    CuentaDao cuentaDao;

    @Override
    public Cliente find(long dni, boolean loadComplete) {
        ClienteEntity clienteEntity = (ClienteEntity) getInMemoryDatabase().get(dni);
        if (clienteEntity == null) {
            return null;
        }

        Cliente cliente = clienteEntity.toCliente();

        if (loadComplete && clienteEntity.getCuentas() != null) {
            for (Long numeroCuenta : clienteEntity.getCuentas()) {
                // Verifica si la cuenta ya está asociada al cliente
                Cuenta cuenta = cuentaDao.find(numeroCuenta);
                if (cuenta != null && !cliente.getCuentas().contains(cuenta)) {
                    cliente.addCuenta(cuenta);  // Solo agrega si no está asociada aún
                }
            }
        }

        return cliente;
    }


    @Override
    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);

        // Si el cliente tiene cuentas, guardar las referencias
        if (cliente.getCuentas() != null) {
            List<Long> cuentasIds = cliente.getCuentas().stream()
                    .map(Cuenta::getNumeroCuenta)
                    .collect(Collectors.toList());
            entity.setCuentas(cuentasIds);
        }

        getInMemoryDatabase().put(cliente.getDni(), entity);
        System.out.println("Cliente guardado con cuentas: " + entity.getCuentas());
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
