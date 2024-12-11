package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaTest {

    @Test
    public void testDebitarDeCuenta() throws CantidadNegativaException, NoAlcanzaException {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(1000.0);

        cuenta.debitarDeCuenta(500.0);
        assertEquals(500.0, cuenta.getBalance());
    }

    @Test
    public void testDebitarDeCuentaFondosInsuficientes() {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(1000.0);

        assertThrows(NoAlcanzaException.class, () -> cuenta.debitarDeCuenta(1500.0));
    }

    @Test
    public void testAcreditarEnCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(1000.0);

        cuenta.acreditarEnCuenta(500.0);
        assertEquals(1500.0, cuenta.getBalance());
    }
}
