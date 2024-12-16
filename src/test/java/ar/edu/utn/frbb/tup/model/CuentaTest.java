package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        // Crear una cuenta con un balance inicial de 1000 y tipo de cuenta y moneda
        cuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 1000.0);
    }

    @Test
    void testGenerarNumeroCuentaUnico() {
        long numeroCuenta1 = cuenta.getNumeroCuenta();
        Cuenta cuenta2 = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 500.0);
        long numeroCuenta2 = cuenta2.getNumeroCuenta();

        // Verificar que los números de cuenta generados sean únicos
        assertNotEquals(numeroCuenta1, numeroCuenta2);
    }

    @Test
    void testDebitarDeCuentaConFondosSuficientes() throws NoAlcanzaException, CantidadNegativaException {
        double cantidadADebitar = 500.0;

        // Debitar la cantidad
        cuenta.debitarDeCuenta(cantidadADebitar);

        // Verificar que el balance se ha actualizado correctamente
        assertEquals(500.0, cuenta.getBalance(), 0.01); // balance inicial era 1000, ahora debería ser 500
    }

    @Test
    void testDebitarDeCuentaConFondosInsuficientes() {
        double cantidadADebitar = 1500.0;

        // Verificar que se lanza la excepción por saldo insuficiente
        assertThrows(NoAlcanzaException.class, () -> cuenta.debitarDeCuenta(cantidadADebitar));
    }

    @Test
    void testDebitarDeCuentaConCantidadNegativa() {
        double cantidadADebitar = -100.0;

        // Verificar que se lanza la excepción por cantidad negativa
        assertThrows(CantidadNegativaException.class, () -> cuenta.debitarDeCuenta(cantidadADebitar));
    }

    @Test
    void testAcreditarEnCuenta() {
        double montoACreditar = 500.0;

        // Acreditar el monto en la cuenta
        cuenta.acreditarEnCuenta(montoACreditar);

        // Verificar que el balance se ha actualizado correctamente
        assertEquals(1500.0, cuenta.getBalance(), 0.01); // balance inicial era 1000, ahora debería ser 1500
    }

    @Test
    void testAcreditarEnCuentaConCantidadNegativa() {
        double montoACreditar = -200.0;

        // Verificar que se lanza la excepción por cantidad negativa
        assertThrows(IllegalArgumentException.class, () -> cuenta.acreditarEnCuenta(montoACreditar));
    }

    @Test
    void testAgregarMovimiento() {
        // Agregar un movimiento
        cuenta.acreditarEnCuenta(500.0);

        // Verificar que se haya agregado el movimiento a la lista de movimientos
        assertEquals(1, cuenta.getMovimientos().size()); // Debería haber un movimiento registrado
        assertEquals("CRÉDITO", cuenta.getMovimientos().get(0).getTipo()); // El tipo del movimiento debe ser "CRÉDITO"
    }

    @Test
    void testSetGetNumeroCuenta() {
        // Verificar que se pueda setear y obtener el número de cuenta
        cuenta.setNumeroCuenta(123456789L);
        assertEquals(123456789L, cuenta.getNumeroCuenta());
    }

    @Test
    void testGetMovimientos() {
        // Verificar que el getter de movimientos devuelve una copia
        cuenta.acreditarEnCuenta(100.0);
        assertNotSame(cuenta.getMovimientos(), cuenta.getMovimientos()); // Verificar que no se devuelve la lista original
    }

    @Test
    void testToString() {
        // Llamar a toString()
        String cuentaString = cuenta.toString();

        // Verificar que el tipo de cuenta, moneda y balance están presentes en el toString()
        assertTrue(cuentaString.contains("tipoCuenta=CAJA_AHORRO"), "Debe contener 'tipoCuenta=CAJA_AHORRO'");
        assertTrue(cuentaString.contains("moneda=PESOS"), "Debe contener 'moneda=PESOS'");
        assertTrue(cuentaString.contains("balance=1000.0"), "Debe contener 'balance=1000.0'");

        // Verificar que la fecha de creación está presente, pero no verificar su valor exacto
        assertTrue(cuentaString.contains("fechaCreacion="), "Debe contener 'fechaCreacion='");
    }


}
