package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteTest {

    @Test
    public void testTieneCuenta() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        cliente.addCuenta(cuenta);

        assertTrue(cliente.tieneCuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS));
        assertFalse(cliente.tieneCuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES));
    }
}
