package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
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
        // Crear una nueva cuenta mapeada desde el DTO
        Cuenta cuenta = new Cuenta(
                TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase()),
                TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase()),
                cuentaDto.getBalanceInicial()
        );

        // Generar automáticamente el número de cuenta
        cuenta.setNumeroCuenta(cuenta.generarNumeroCuentaUnico());

        // Buscar al cliente titular
        Cliente clienteTitular = clienteService.buscarClientePorDni(cuentaDto.getDniTitular());
        if (clienteTitular == null) {
            throw new BusinessLogicException("El cliente con DNI " + cuentaDto.getDniTitular() + " no existe.");
        }

        // Verificar si ya existe una cuenta del mismo tipo y moneda para este cliente
        boolean existeCuentaDelMismoTipoYMoneda = clienteTitular.getCuentas().stream()
                .anyMatch(c -> c.getTipoCuenta().equals(TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase())) &&
                        c.getMoneda().equals(TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase())));

        if (existeCuentaDelMismoTipoYMoneda) {
            throw new CuentaAlreadyExistsException("El cliente ya tiene una cuenta del tipo " + cuentaDto.getTipoCuenta() + " y moneda " + cuentaDto.getMoneda());
        }

        // Asociar la cuenta al cliente y guardar
        clienteTitular.addCuenta(cuenta);
        cuentaDao.save(cuenta);
        clienteDao.save(clienteTitular);

        return cuenta;
    }
}
