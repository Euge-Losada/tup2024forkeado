package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TupResponseEntityExceptionHandlerTest {

    private final TupResponseEntityExceptionHandler handler = new TupResponseEntityExceptionHandler();

    @Test
    public void testHandleBusinessLogicException() {
        BusinessLogicException exception = new BusinessLogicException("Error de lógica");

        ResponseEntity<TransferenciaResponseDto> response = handler.handleBusinessLogicException(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("ERROR", response.getBody().getEstado());
        assertEquals("Error de lógica", response.getBody().getMensaje());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new Exception("Error inesperado");

        ResponseEntity<TransferenciaResponseDto> response = handler.handleGenericException(exception);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("ERROR", response.getBody().getEstado());
        assertTrue(response.getBody().getMensaje().contains("Error inesperado"));
    }
}
