package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MovimientoTest {

    @Test
    public void testMovimientoConstructorAndGetters() {
        Movimiento movimiento = new Movimiento(
                12345678L, // número de cuenta
                "CRÉDITO",
                500.0,
                "Depósito inicial",
                LocalDateTime.now() // Fecha actual
        );

    }

    @Test
    public void testMovimientoSetters() {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo("DÉBITO");
        movimiento.setMonto(300.0);
        movimiento.setDescripcion("Retiro de efectivo");

        assertEquals("DÉBITO", movimiento.getTipo());
        assertEquals(300.0, movimiento.getMonto());
        assertEquals("Retiro de efectivo", movimiento.getDescripcion());
    }
}
