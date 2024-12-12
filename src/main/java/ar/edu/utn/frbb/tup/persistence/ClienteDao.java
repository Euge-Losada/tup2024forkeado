package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;

public interface ClienteDao {
    Cliente find(long dni, boolean loadComplete);
    void save(Cliente cliente);

}