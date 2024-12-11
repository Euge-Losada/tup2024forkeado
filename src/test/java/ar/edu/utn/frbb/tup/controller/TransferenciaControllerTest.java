package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferenciaControllerTest {

    @InjectMocks
    private TransferenciaController transferenciaController;

    @Mock
    private TransferenciaService transferenciaService;

    private TransferenciaDto transferenciaDto;

    @BeforeAll
    void setup() {
        transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(12345678L);
        transferenciaDto.setCuentaDestino(87654321L);
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setMoneda("PESOS");
    }

    @Test
    @DisplayName("Test: Realizar transferencia exitosa")
    void testRealizarTransferencia_Exitosa() {
        // Act
        ResponseEntity<TransferenciaResponseDto> responseEntity = transferenciaController.realizarTransferencia(transferenciaDto);

        // Assert
        assertNotNull(responseEntity, "La respuesta no debería ser nula");
        assertNotNull(responseEntity.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertEquals("EXITOSA", responseEntity.getBody().getEstado());
        assertEquals("Transferencia exitosa", responseEntity.getBody().getMensaje());
    }

    @Test
    @DisplayName("Test: Realizar transferencia fallida por fondos insuficientes")
    void testRealizarTransferencia_Fallida() {
        // Arrange
        doThrow(new BusinessLogicException("Fondos insuficientes"))
                .when(transferenciaService).transferir(any(TransferenciaDto.class));

        // Act & Assert
        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> transferenciaController.realizarTransferencia(transferenciaDto));

        assertNotNull(exception, "La excepción no debería ser nula");
        assertEquals("Fondos insuficientes", exception.getMessage());
    }

    @AfterAll
    void tearDown() {
        transferenciaDto = null;
    }
}
