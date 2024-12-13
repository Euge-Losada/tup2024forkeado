package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteCuentaService {

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CuentaDao cuentaDao;

    public Cliente findClienteWithCuentas(long dni) {
        Cliente cliente = clienteDao.find(dni, true);
        if (cliente != null) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                Cuenta cuentaCompleta = cuentaDao.find(cuenta.getNumeroCuenta());
                if (cuentaCompleta != null) {
                    cliente.addCuenta(cuentaCompleta);
                }
            }
        }
        return cliente;
    }
}

