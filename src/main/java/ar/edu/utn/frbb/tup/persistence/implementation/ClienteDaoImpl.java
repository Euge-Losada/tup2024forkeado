package ar.edu.utn.frbb.tup.persistence.implementation;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.AbstractBaseDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClienteDaoImpl extends AbstractBaseDao implements ClienteDao {

    @Override
    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null) {
            return null;
        }
        return ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
    }

    @Override
    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}