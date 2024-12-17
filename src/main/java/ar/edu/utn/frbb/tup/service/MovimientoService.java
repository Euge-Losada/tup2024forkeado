package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoDao movimientoDao;

    @Autowired
    private CuentaDao cuentaDao;

    public List<Movimiento> obtenerMovimientosPorCuenta(long numeroCuenta) {
        return movimientoDao.obtenerMovimientosPorCuenta(numeroCuenta).stream()
                .map(entity -> new Movimiento(
                        entity.getNumeroCuenta(),
                        entity.getTipo(),
                        entity.getMonto(),
                        entity.getDescripcion(),
                        entity.getFecha()
                ))
                .collect(Collectors.toList());
    }

    public void registrarMovimiento(long numeroCuenta, Movimiento movimiento) {
        // Registrar el movimiento en el DAO
        MovimientoEntity entity = new MovimientoEntity(movimiento);
        movimientoDao.guardarMovimiento(entity);

        // Actualizar el balance de la cuenta si aplica
        Cuenta cuenta = cuentaDao.find(numeroCuenta);
        if (cuenta != null) {
            if ("CRÉDITO".equalsIgnoreCase(movimiento.getTipo())) {
                cuenta.acreditarEnCuenta(movimiento.getMonto());
            } else if ("DÉBITO".equalsIgnoreCase(movimiento.getTipo())) {
                try {
                    cuenta.debitarDeCuenta(movimiento.getMonto());
                } catch (NoAlcanzaException | CantidadNegativaException e) {
                    // Aquí puedes registrar el error o manejarlo de forma personalizada
                    throw new BusinessLogicException("Error al debitar de la cuenta: " + e.getMessage());
                }
            }
            cuentaDao.save(cuenta); // Guardar los cambios en la cuenta
        }
    }



    public void registrarMovimientoEnCuenta(long numeroCuenta, Movimiento movimiento) {
        Cuenta cuenta = cuentaDao.find(numeroCuenta);
        if (cuenta == null) {
            throw new BusinessLogicException("La cuenta con número " + numeroCuenta + " no existe.");
        }

        // Actualizar cuenta y registrar movimiento
        cuenta.acreditarEnCuenta(movimiento.getMonto());
        MovimientoEntity movimientoEntity = new MovimientoEntity(movimiento);
        movimientoDao.guardarMovimiento(movimientoEntity);
        cuentaDao.save(cuenta); // Persistir cambios en la cuenta
    }
}

