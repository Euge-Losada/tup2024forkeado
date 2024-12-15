package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteDao clienteDao;

    @Autowired
    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    @Autowired
    private CuentaDao cuentaDao;

    // Método para crear un cliente con una cuenta asociada
    public Cliente crearClienteConCuenta(ClienteDto clienteDto) {
        // Crear el cliente a partir del DTO
        Cliente cliente = new Cliente(clienteDto);

        // Crear la cuenta asociada, usando los valores del DTO
        TipoCuenta tipoCuenta = TipoCuenta.valueOf(clienteDto.getTipoCuenta());  // Convertir el tipo de cuenta desde el DTO
        TipoMoneda moneda = TipoMoneda.valueOf(clienteDto.getMoneda());  // Convertir la moneda desde el DTO
        double balance = clienteDto.getBalance();  // Usar el balance desde el DTO

        Cuenta cuenta = new Cuenta(tipoCuenta, moneda, balance);

        // Agregar la cuenta al cliente
        cliente.addCuenta(cuenta);

        // Guardar el cliente con la cuenta asociada
        clienteDao.save(cliente);  // Guarda el cliente con sus cuentas
        cuentaDao.save(cuenta);    // Guarda la cuenta en el sistema

        return cliente;  // Devuelve el cliente con la cuenta asociada
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        Cliente clienteExistente = clienteDao.find(clienteDto.getDni(), false);

        if (clienteExistente != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el DNI: " + clienteDto.getDni());
        }
        Cliente cliente = new Cliente(clienteDto);
        clienteDao.save(cliente);
        return cliente;
    }



    public Cliente buscarClientePorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, true);
        if (cliente == null) {
            throw new BusinessLogicException("Cliente no encontrado con DNI: " + dni);
        }
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) {
        Cliente cliente = buscarClientePorDni(dniTitular);
        if (cliente != null) {
            cliente.addCuenta(cuenta);
            clienteDao.save(cliente); // Guardar el cliente actualizado con la nueva cuenta
        } else {
            throw new BusinessLogicException("El cliente no existe.");
        }
    }
}
