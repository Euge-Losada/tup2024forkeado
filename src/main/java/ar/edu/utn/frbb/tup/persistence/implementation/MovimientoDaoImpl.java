package ar.edu.utn.frbb.tup.persistence.implementation;

import ar.edu.utn.frbb.tup.persistence.AbstractBaseDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovimientoDaoImpl extends AbstractBaseDao implements MovimientoDao {

    @Override
    public void guardarMovimiento(MovimientoEntity movimiento) {
        if (movimiento.getNumeroCuenta() <= 0) {
            throw new IllegalArgumentException("Número de cuenta inválido: " + movimiento.getNumeroCuenta());
        }
        // Guardar el movimiento en el mapa in-memory
        getInMemoryDatabase().put(movimiento.getNumeroCuenta(), movimiento);
    }

    @Override
    public List<MovimientoEntity> obtenerMovimientosPorCuenta(long numeroCuenta) {
        return getInMemoryDatabase().values().stream()
                .map(obj -> (MovimientoEntity) obj)
                .filter(mov -> mov.getNumeroCuenta() == numeroCuenta)
                .collect(Collectors.toList());
    }

    @Override
    protected String getEntityName() {
        return "MOVIMIENTO";
    }
}
