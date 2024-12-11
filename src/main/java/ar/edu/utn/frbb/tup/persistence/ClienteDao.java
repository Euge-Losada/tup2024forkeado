package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{



    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null){
            return null;

        }

        Cliente cliente = ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();


        return cliente;

    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
