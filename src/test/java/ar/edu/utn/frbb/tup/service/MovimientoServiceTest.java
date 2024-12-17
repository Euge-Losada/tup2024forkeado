package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceTest {

    @InjectMocks
    private MovimientoService movimientoService;

    @Mock
    private MovimientoDao movimientoDao;

    @Mock
    private CuentaDao cuentaDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarMovimiento() {
        // Preparar datos
        Movimiento movimiento = new Movimiento(
                12345678L, // Número de cuenta
                "CRÉDITO", // Tipo de movimiento
                1000.0,    // Monto
                "Transferencia entrante", // Descripción
                LocalDateTime.now()       // Fecha actual
        );

        MovimientoEntity movimientoEntity = new MovimientoEntity(movimiento);

        // Simular DAO
        doNothing().when(movimientoDao).guardarMovimiento(movimientoEntity);

        // Acción
        movimientoService.registrarMovimiento(12345678L, movimiento);

        // Verificar interacción con el DAO
        verify(movimientoDao, times(1)).guardarMovimiento(any(MovimientoEntity.class));
    }

    @Test
    void testObtenerMovimientosPorCuenta() {
        // Preparar datos
        MovimientoEntity mov1 = new MovimientoEntity(
                new Movimiento(12345678L, "CRÉDITO", 1000.0, "Transferencia entrante", LocalDateTime.now())
        );
        MovimientoEntity mov2 = new MovimientoEntity(
                new Movimiento(12345678L, "DÉBITO", 500.0, "Pago de servicio", LocalDateTime.now())
        );

        when(movimientoDao.obtenerMovimientosPorCuenta(12345678L))
                .thenReturn(List.of(mov1, mov2));

        // Acción
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(12345678L);

        // Verificar resultados
        assertEquals(2, movimientos.size());
        assertEquals("CRÉDITO", movimientos.get(0).getTipo());
        assertEquals(1000.0, movimientos.get(0).getMonto());
    }
}
