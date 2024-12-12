package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Movimiento;
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

    // Obtener los movimientos de una cuenta
    public List<Movimiento> obtenerMovimientosPorCuenta(long numeroCuenta) {
        // Convertir cada MovimientoEntity a un objeto Movimiento
        return movimientoDao.obtenerMovimientosPorCuenta(numeroCuenta).stream()
                .map(entity -> new Movimiento(
                        entity.getNumeroCuenta(),  // numeroCuenta
                        entity.getTipo(),           // tipo (CRÉDITO/DÉBITO)
                        entity.getMonto(),          // monto
                        entity.getDescripcion(),    // descripción
                        entity.getFecha()           // fecha
                ))
                .collect(Collectors.toList());
    }

    // Registrar un nuevo movimiento
    public void registrarMovimiento(long numeroCuenta, Movimiento movimiento) {
        // Convertir el objeto Movimiento en MovimientoEntity
        MovimientoEntity entity = new MovimientoEntity(
                movimiento // Le pasamos el objeto Movimiento completo
        );

        // Guardar el movimiento utilizando el DAO
        movimientoDao.guardarMovimiento(entity);
    }
}

