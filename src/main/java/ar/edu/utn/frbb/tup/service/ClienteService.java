package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private final ClienteDao clienteDao;

    @Autowired
    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente(clienteDto);
        clienteDao.save(cliente);
        return cliente;
    }

    public Cliente buscarClientePorDni(long dni) {
        return clienteDao.find(dni, true);
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) {
        Cliente cliente = buscarClientePorDni(dniTitular);
        if (cliente != null) {
            cliente.addCuenta(cuenta);
        } else {
            throw new BusinessLogicException("El cliente no existe.");
        }
    }

}
