package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovimientoControllerTest {

    @Mock
    private MovimientoService movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    private Movimiento movimiento;

    @BeforeEach
    public void setUp() {
        movimiento = new Movimiento("CRÉDITO", 500.0, "Depósito inicial");
    }

    // Test para obtener movimientos por cuenta
    @Test
    public void testObtenerMovimientosPorCuentaSuccess() {
        // Simulación de la respuesta del servicio
        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(movimiento);
        when(movimientoService.obtenerMovimientosPorCuenta(12345L)).thenReturn(movimientos);

        // Ejecución
        ResponseEntity<List<Movimiento>> response = movimientoController.obtenerMovimientosPorCuenta(12345L);

        // Verificación
        verify(movimientoService, times(1)).obtenerMovimientosPorCuenta(12345L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("CRÉDITO", response.getBody().get(0).getTipo());
    }

    @Test
    public void testObtenerMovimientosPorCuentaNotFound() {
        // Simulación de respuesta vacía
        when(movimientoService.obtenerMovimientosPorCuenta(12345L)).thenReturn(new ArrayList<>());

        // Ejecución
        ResponseEntity<List<Movimiento>> response = movimientoController.obtenerMovimientosPorCuenta(12345L);

        // Verificación
        verify(movimientoService, times(1)).obtenerMovimientosPorCuenta(12345L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() == null || response.getBody().isEmpty());
    }

    // Test para registrar un movimiento
    @Test
    public void testRegistrarMovimientoSuccess() {
        // Simulación del servicio
        doNothing().when(movimientoService).registrarMovimiento(12345L, movimiento);

        // Ejecución
        ResponseEntity<Void> response = movimientoController.registrarMovimiento(12345L, movimiento);

        // Verificación
        verify(movimientoService, times(1)).registrarMovimiento(12345L, movimiento);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
