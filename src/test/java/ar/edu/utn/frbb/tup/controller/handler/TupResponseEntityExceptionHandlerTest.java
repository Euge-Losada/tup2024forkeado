package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.InvalidDniException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TupResponseEntityExceptionHandlerTest {

    private final TupResponseEntityExceptionHandler handler = new TupResponseEntityExceptionHandler();

    @Test
    void handleBusinessLogicException() {
        // Preparar datos
        BusinessLogicException exception = new BusinessLogicException("Error en la lógica de negocio");

        // Invocar el manejador
        ResponseEntity<CustomApiError> response = handler.handleBusinessLogicException(exception, null);

        // Verificar resultados
        assertEquals(4000, response.getBody().getErrorCode());
        assertEquals("Error en la lógica de negocio", response.getBody().getErrorMessage());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleInvalidDniException() {
        // Preparar datos
        InvalidDniException exception = new InvalidDniException("DNI inválido");

        // Invocar el manejador
        ResponseEntity<CustomApiError> response = handler.handleInvalidDniException(exception, null);

        // Verificar resultados
        assertEquals(4001, response.getBody().getErrorCode());
        assertEquals("DNI inválido", response.getBody().getErrorMessage());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleGenericException() {
        // Preparar datos
        Exception exception = new Exception("Error genérico");

        // Invocar el manejador
        ResponseEntity<CustomApiError> response = handler.handleGenericException(exception, null);

        // Verificar resultados
        CustomApiError error = (CustomApiError) response.getBody();
        assertEquals("Error inesperado: Error genérico", error.getErrorMessage());
        assertEquals(500, response.getStatusCode().value());
    }
}
