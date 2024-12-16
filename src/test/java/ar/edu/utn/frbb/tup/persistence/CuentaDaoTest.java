package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaDaoTest {

    @Mock
    private CuentaDao cuentaDao;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        // Crear un objeto Cuenta de ejemplo para las pruebas
        cuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 1000.0);
    }

    @Test
    void testSave() {
        // Simular que se llama el método save
        cuentaDao.save(cuenta);

        // Verificar que el método save fue llamado con la cuenta correcta
        verify(cuentaDao, times(1)).save(cuenta);
    }

    @Test
    void testFind() {
        // Simular que el método find devuelve la cuenta
        when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(cuenta);

        // Verificar que el método find devuelve la cuenta esperada
        Cuenta cuentaEncontrada = cuentaDao.find(cuenta.getNumeroCuenta());
        assertEquals(cuenta, cuentaEncontrada);

        // Verificar que el método find fue llamado una vez
        verify(cuentaDao, times(1)).find(cuenta.getNumeroCuenta());
    }
}
