package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteService clienteService;

    // Método para dar de alta la cuenta desde CuentaDto
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        // Crear una nueva cuenta a partir del DTO
        Cuenta cuenta = new Cuenta();

        // Si numeroCuenta no se pasa, genera uno aleatorio
        if (cuentaDto.getNumeroCuenta() == 0) {
            cuenta.setNumeroCuenta(System.currentTimeMillis());
        } else {
            cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta());
        }

        cuenta.setTipoCuenta(TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase()));
        cuenta.setMoneda(TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase()));
        cuenta.setBalance(cuentaDto.getBalanceInicial());
        cuenta.setFechaCreacion(java.time.LocalDateTime.now());

        // Buscar el cliente y asignarlo como titular
        Cliente clienteTitular = clienteService.buscarClientePorDni(cuentaDto.getDniTitular());
        cuenta.setTitular(clienteTitular);

        if (cuenta.getTitular() == null) {
            throw new BusinessLogicException("El cliente titular no existe.");
        }
        // Llamar al método original con la cuenta creada
        darDeAltaCuenta(cuenta, cuentaDto.getDniTitular());

        return cuenta;
    }

    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new IllegalArgumentException("Tipo de cuenta no soportado: " + cuenta.getTipoCuenta());
        }

        // Guardar la cuenta en el cliente
        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    private boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return Arrays.asList(TipoCuenta.values()).contains(cuenta.getTipoCuenta());
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }



}

