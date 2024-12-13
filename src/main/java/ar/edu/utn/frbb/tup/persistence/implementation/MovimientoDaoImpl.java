package ar.edu.utn.frbb.tup.persistence.implementation;

import ar.edu.utn.frbb.tup.persistence.AbstractBaseDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovimientoDaoImpl extends AbstractBaseDao implements MovimientoDao {

    @Override
    public void guardarMovimiento(MovimientoEntity movimiento) {
        if (movimiento.getNumeroCuenta() <= 0) {
            throw new IllegalArgumentException("Número de cuenta inválido: " + movimiento.getNumeroCuenta());
        }

        // Verificar si ya existe una lista de movimientos para esa cuenta
        Object movimientosExistentes = getInMemoryDatabase().get(movimiento.getNumeroCuenta());

        if (movimientosExistentes instanceof List<?>) {
            List<MovimientoEntity> movimientos = (List<MovimientoEntity>) movimientosExistentes;
            movimientos.add(movimiento);
        } else {
            // Si no existe, crear una nueva lista y agregar el movimiento
            List<MovimientoEntity> nuevaLista = new ArrayList<>();
            nuevaLista.add(movimiento);
            getInMemoryDatabase().put(movimiento.getNumeroCuenta(), nuevaLista);
        }
    }


    @Override
    public List<MovimientoEntity> obtenerMovimientosPorCuenta(long numeroCuenta) {
        Object movimientosExistentes = getInMemoryDatabase().get(numeroCuenta);

        if (movimientosExistentes instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<MovimientoEntity> movimientos = (List<MovimientoEntity>) movimientosExistentes;
            return movimientos;
        }

        return new ArrayList<>(); // Devuelve una lista vacía si no hay movimientos
    }


    @Override
    protected String getEntityName() {
        return "MOVIMIENTO";
    }
}
