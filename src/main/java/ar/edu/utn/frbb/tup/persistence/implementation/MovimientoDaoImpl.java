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
        System.out.println("Guardando movimiento: " + movimiento);
        Object movimientosExistentes = getInMemoryDatabase().get(movimiento.getNumeroCuenta());

        if (movimientosExistentes instanceof List<?>) {
            List<MovimientoEntity> movimientos = (List<MovimientoEntity>) movimientosExistentes;
            movimientos.add(movimiento);
        } else {
            List<MovimientoEntity> nuevaLista = new ArrayList<>();
            nuevaLista.add(movimiento);
            getInMemoryDatabase().put(movimiento.getNumeroCuenta(), nuevaLista);
        }
    }

    @Override
    public List<MovimientoEntity> obtenerMovimientosPorCuenta(long numeroCuenta) {
        System.out.println("Buscando movimientos para cuenta: " + numeroCuenta);
        Object movimientosExistentes = getInMemoryDatabase().get(numeroCuenta);

        if (movimientosExistentes instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<MovimientoEntity> movimientos = (List<MovimientoEntity>) movimientosExistentes;
            System.out.println("Movimientos encontrados: " + movimientos);
            return movimientos;
        }
        System.out.println("No se encontraron movimientos para la cuenta: " + numeroCuenta);
        return new ArrayList<>(); // Si no hay movimientos, retorna una lista vacía
    }

    @Override
    protected String getEntityName() {
        return "MOVIMIENTO";
    }
}
