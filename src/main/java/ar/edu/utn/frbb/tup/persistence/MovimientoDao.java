package ar.edu.utn.frbb.tup.persistence;


import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;

import java.util.List;

public interface MovimientoDao {
    void guardarMovimiento(MovimientoEntity movimiento);
    List<MovimientoEntity> obtenerMovimientosPorCuenta(long numeroCuenta);
}