package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaDaoTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private CuentaDao cuentaDao;


    @Test
    public void testGuardarYBuscarCuenta() {
        Cliente titular = new Cliente();
        titular.setDni(12345678L);
        titular.setNombre("Juan");
        titular.setApellido("Pérez");

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
        cuenta.setFechaCreacion(java.time.LocalDateTime.now());
        cuenta.setTitular(titular);

        when(clienteDao.find(12345678L, true)).thenReturn(titular);

        cuentaDao.save(cuenta);

        Cuenta encontrada = cuentaDao.find(12345L);
        assertNotNull(encontrada);
        assertEquals(12345L, encontrada.getNumeroCuenta());
        assertNotNull(encontrada.getTitular());
        assertEquals("Juan", encontrada.getTitular().getNombre());
    }

    @Test
    public void testGuardarCuentaSinTitularLanzaExcepcion() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
        cuenta.setFechaCreacion(java.time.LocalDateTime.now());
        cuenta.setTitular(null); // Sin titular

        Exception exception = assertThrows(BusinessLogicException.class, () -> cuentaDao.save(cuenta));
        assertEquals("La cuenta debe tener un titular.", exception.getMessage());
    }

}
