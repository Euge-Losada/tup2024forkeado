package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceTest {

    @Mock
    private MovimientoDao movimientoDao;

    @InjectMocks
    private MovimientoService movimientoService;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this); --> @ExtendWith(MockitoExtension.class) ya lo hace
    }

    @Test
    public void testObtenerMovimientosPorCuenta_ConMovimientos() {
        // Configuración de datos simulados
        MovimientoEntity mov1 = new MovimientoEntity(12345678L, "CRÉDITO", 1000.0, "Transferencia entrante", LocalDateTime.now());
        MovimientoEntity mov2 = new MovimientoEntity(12345678L, "DÉBITO", 500.0, "Pago de servicio", LocalDateTime.now());

        when(movimientoDao.obtenerMovimientosPorCuenta(12345678L)).thenReturn(Arrays.asList(mov1, mov2));

        // Llamada al método del servicio
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(12345678L);

        // Verificaciones
        assertEquals(2, movimientos.size());
        assertEquals("CRÉDITO", movimientos.get(0).getTipo());
        assertEquals(1000.0, movimientos.get(0).getMonto());
        assertEquals("DÉBITO", movimientos.get(1).getTipo());
        assertEquals(500.0, movimientos.get(1).getMonto());
        verify(movimientoDao, times(1)).obtenerMovimientosPorCuenta(12345678L);
    }

    @Test
    public void testObtenerMovimientosPorCuenta_SinMovimientos() {
        // Simula que no hay movimientos
        when(movimientoDao.obtenerMovimientosPorCuenta(12345678L)).thenReturn(Collections.emptyList());

        // Llamada al método del servicio
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(12345678L);

        // Verificaciones
        assertTrue(movimientos.isEmpty());
        verify(movimientoDao, times(1)).obtenerMovimientosPorCuenta(12345678L);
    }
}
