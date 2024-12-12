package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoControllerTest {

    @InjectMocks
    private MovimientoController movimientoController;

    @Mock
    private MovimientoService movimientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerHistorialMovimientos() {
        // Simular datos de respuesta esperados
        Movimiento movimiento1 = new Movimiento(12345L, "CRÉDITO", 1000.0, "Depósito inicial", LocalDateTime.now());
        Movimiento movimiento2 = new Movimiento(12345L, "DÉBITO", 500.0, "Retiro en efectivo", LocalDateTime.now());

        List<Map<String, Object>> movimientosMock = List.of(
                Map.of(
                        "fecha", movimiento1.getFecha().toString(),
                        "tipo", movimiento1.getTipo(),
                        "descripcionBreve", movimiento1.getDescripcion(),
                        "monto", movimiento1.getMonto()
                ),
                Map.of(
                        "fecha", movimiento2.getFecha().toString(),
                        "tipo", movimiento2.getTipo(),
                        "descripcionBreve", movimiento2.getDescripcion(),
                        "monto", movimiento2.getMonto()
                )
        );

        Map<String, Object> responseMock = Map.of(
                "numeroCuenta", 12345L,
                "movimientos", movimientosMock
        );

        // Simular respuesta del controlador
        when(movimientoService.obtenerMovimientosPorCuenta(12345L)).thenReturn(List.of(movimiento1, movimiento2));

        // Acción
        ResponseEntity<Map<String, Object>> response = movimientoController.obtenerHistorialMovimientos(12345L);

        // Verificar resultados
        assertEquals(12345L, response.getBody().get("numeroCuenta"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> movimientos = (List<Map<String, Object>>) response.getBody().get("movimientos");

        assertEquals(2, movimientos.size());
        assertEquals("CRÉDITO", movimientos.get(0).get("tipo"));
        assertEquals(1000.0, movimientos.get(0).get("monto"));
    }

    @Test
    void testRegistrarMovimiento() {
        // Preparar datos
        Movimiento movimiento = new Movimiento(12345L, "CRÉDITO", 1000.0, "Transferencia entrante", LocalDateTime.now());

        // Simular acción en el servicio
        doNothing().when(movimientoService).registrarMovimiento(12345L, movimiento);

        // Acción
        ResponseEntity<Void> response = movimientoController.registrarMovimiento(12345L, movimiento);

        // Verificar resultado
        assertEquals(200, response.getStatusCode());

        // Verificar interacción con el servicio
        verify(movimientoService, times(1)).registrarMovimiento(12345L, movimiento);
    }
}

