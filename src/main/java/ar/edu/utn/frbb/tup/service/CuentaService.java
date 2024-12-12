package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteService clienteService;

    /**
     * Dar de alta una nueva cuenta a partir de un DTO.
     */
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
        // Mapear DTO a entidad Cuenta
        Cuenta cuenta = mapearDtoACuenta(cuentaDto);

        // Verificar si ya existe una cuenta con ese número
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta con número " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        // Asociar cuenta al cliente titular
        Cliente clienteTitular = clienteService.buscarClientePorDni(cuentaDto.getDniTitular());
        if (clienteTitular == null) {
            throw new BusinessLogicException("El cliente titular no existe.");
        }
        clienteTitular.addCuenta(cuenta);

        // Guardar la cuenta en la base de datos
        cuentaDao.save(cuenta);

        return cuenta;
    }

    /**
     * Mapear CuentaDto a Cuenta.
     */
    private Cuenta mapearDtoACuenta(CuentaDto cuentaDto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta() != 0 ? cuentaDto.getNumeroCuenta() : System.currentTimeMillis());
        cuenta.setTipoCuenta(TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase()));
        cuenta.setMoneda(TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase()));
        cuenta.setBalance(cuentaDto.getBalanceInicial());
        return cuenta;
    }
}


