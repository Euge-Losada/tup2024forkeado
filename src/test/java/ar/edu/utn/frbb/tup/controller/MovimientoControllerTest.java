package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoControllerTest {

    @InjectMocks
    private MovimientoController movimientoController;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private MovimientoService movimientoService;

    @Test
    void testObtenerHistorialMovimientosSuccess() {
        long numeroCuenta = 123456L;
        Cuenta cuentaMock = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 1000.0);
        cuentaMock.setNumeroCuenta(numeroCuenta);

        Movimiento movimiento = new Movimiento(
                numeroCuenta, "CRÉDITO", 100.0, "Prueba de movimiento", LocalDateTime.now()
        );

        when(cuentaDao.find(numeroCuenta)).thenReturn(cuentaMock);
        when(movimientoService.obtenerMovimientosPorCuenta(numeroCuenta))
                .thenReturn(Collections.singletonList(movimiento));

        ResponseEntity<Map<String, Object>> response = movimientoController.obtenerHistorialMovimientos(numeroCuenta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("movimientos"));

        List<?> movimientos = (List<?>) response.getBody().get("movimientos");
        assertEquals(1, movimientos.size());

        verify(movimientoService, times(1)).obtenerMovimientosPorCuenta(numeroCuenta);
    }

    @Test
    void testObtenerHistorialMovimientosCuentaNoExiste() {
        long numeroCuenta = 123456L;

        when(cuentaDao.find(numeroCuenta)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = movimientoController.obtenerHistorialMovimientos(numeroCuenta);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("mensaje").toString().contains("no existe"));

        verify(movimientoService, never()).obtenerMovimientosPorCuenta(numeroCuenta);
    }

    @Test
    void testRegistrarMovimientoSuccess() {
        long numeroCuenta = 123456L;
        Movimiento movimiento = new Movimiento(
                numeroCuenta, "DÉBITO", 50.0, "Movimiento registrado", LocalDateTime.now()
        );

        doNothing().when(movimientoService).registrarMovimiento(numeroCuenta, movimiento);

        ResponseEntity<Void> response = movimientoController.registrarMovimiento(numeroCuenta, movimiento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movimientoService, times(1)).registrarMovimiento(numeroCuenta, movimiento);
    }
}
