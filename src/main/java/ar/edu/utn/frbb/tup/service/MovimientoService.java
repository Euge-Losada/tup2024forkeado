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

    public List<Movimiento> obtenerMovimientosPorCuenta(long numeroCuenta) {
        return movimientoDao.obtenerMovimientosPorCuenta(numeroCuenta).stream()
                .map(entity -> new Movimiento(
                        entity.getTipo(),
                        entity.getMonto(),
                        entity.getDescripcion()))
                .collect(Collectors.toList());
    }

    public void registrarMovimiento(long numeroCuenta, Movimiento movimiento) {
        MovimientoEntity entity = new MovimientoEntity(
                numeroCuenta,  // Relación directa con la cuenta
                movimiento.getTipo(),
                movimiento.getMonto(),
                movimiento.getDescripcion(),
                movimiento.getFecha()
        );
        movimientoDao.guardarMovimiento(entity);
    }
}

