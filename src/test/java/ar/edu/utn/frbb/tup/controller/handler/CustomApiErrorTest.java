package ar.edu.utn.frbb.tup.controller.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomApiErrorTest {

    @Test
    public void testCustomApiError() {
        CustomApiError error = new CustomApiError(404, "Recurso no encontrado");

        assertEquals(404, error.getErrorCode());
        assertEquals("Recurso no encontrado", error.getErrorMessage());

        error.setErrorCode(500);
        error.setErrorMessage("Error interno");

        assertEquals(500, error.getErrorCode());
        assertEquals("Error interno", error.getErrorMessage());
    }
}
