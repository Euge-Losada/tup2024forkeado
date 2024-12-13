package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteDao clienteDao;

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
        Cuenta cuenta = mapearDtoACuenta(cuentaDto);

        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta ya existe.");
        }

        // Buscar el cliente titular
        Cliente clienteTitular = clienteService.buscarClientePorDni(cuentaDto.getDniTitular());
        if (clienteTitular == null) {
            throw new BusinessLogicException("El cliente no existe.");
        }

        if (!clienteTitular.getCuentas().contains(cuenta)) {
            clienteTitular.addCuenta(cuenta);  // Solo agregar la cuenta si no está asociada aún
        }


        // Guardar la cuenta en la base de datos
        cuentaDao.save(cuenta);
        System.out.println("Cuenta guardada: " + cuenta.getNumeroCuenta());

        clienteDao.save(clienteTitular); // Actualizar el cliente con la cuenta asociada
        System.out.println("Cliente actualizado con cuenta: " + cuenta.getNumeroCuenta());

        return cuenta;
    }


    private Cuenta mapearDtoACuenta(CuentaDto cuentaDto) {
        Cuenta cuenta = new Cuenta(TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase()),
                TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase()),
                cuentaDto.getBalanceInicial());

        cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta() != 0 ? cuentaDto.getNumeroCuenta() : System.currentTimeMillis());
        return cuenta;
    }

}

