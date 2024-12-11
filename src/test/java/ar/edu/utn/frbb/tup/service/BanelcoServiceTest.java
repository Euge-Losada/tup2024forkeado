package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BanelcoServiceTest {

    private final BanelcoService banelcoService = new BanelcoService();

    @Test
    public void testRealizarTransferenciaExterna_Success() {
        // Configurar el DTO de transferencia
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(99999999L); // Cuenta de otro banco
        dto.setMonto(5000.0);
        dto.setMoneda("PESOS");

        // Simulación de la transferencia (debería ejecutarse sin errores)
        assertDoesNotThrow(() -> banelcoService.realizarTransferenciaExterna(dto));
    }

    @Test
    public void testRealizarTransferenciaExterna_DatosIncompletos() {
        // DTO incompleto (sin cuenta destino)
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setMonto(1000.0);
        dto.setMoneda("PESOS");

        // Asegurarse de que el método aún maneja la ejecución sin lanzar excepciones
        assertDoesNotThrow(() -> banelcoService.realizarTransferenciaExterna(dto));
    }

    @Test
    public void testRealizarTransferenciaExterna_MontoCero() {
        // DTO con monto igual a cero
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(99999999L);
        dto.setMonto(0.0);
        dto.setMoneda("PESOS");

        // El método debe ejecutarse sin errores (es una simulación)
        assertDoesNotThrow(() -> banelcoService.realizarTransferenciaExterna(dto));
    }
}
