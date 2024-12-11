package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovimientoTest {

    @Test
    public void testMovimientoConstructorAndGetters() {
        Movimiento movimiento = new Movimiento("CRÉDITO", 500.0, "Depósito inicial");

        assertEquals("CRÉDITO", movimiento.getTipo());
        assertEquals(500.0, movimiento.getMonto());
        assertEquals("Depósito inicial", movimiento.getDescripcion());
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
