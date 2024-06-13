package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CuentaService {
    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteService clienteService;

    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        // Chequear cuentas soportadas por el banco
        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new IllegalArgumentException("Tipo de cuenta no soportado: " + cuenta.getTipoCuenta());
        }

        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    private static final Set<TipoCuenta> TIPOS_CUENTA_SOPORTADOS = new HashSet<>(Arrays.asList(TipoCuenta.values()));

    private boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return TIPOS_CUENTA_SOPORTADOS.contains(cuenta.getTipoCuenta());
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}