package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MovimientoDao {
    private final List<MovimientoEntity> movimientos = new ArrayList<>();

    public void guardarMovimiento(MovimientoEntity movimiento) {
        if (movimiento.getNumeroCuenta() <= 0) {
            throw new IllegalArgumentException("El número de cuenta es inválido: " + movimiento.getNumeroCuenta());
        }
        movimientos.add(movimiento);
    }


    public List<MovimientoEntity> obtenerMovimientosPorCuenta(long numeroCuenta) {
        List<MovimientoEntity> resultados = new ArrayList<>();
        for (MovimientoEntity movimiento : movimientos) {
            if (movimiento.getNumeroCuenta() == numeroCuenta) {  // Filtrar por numeroCuenta
                resultados.add(movimiento);
            }
        }
        return resultados;
    }
}
