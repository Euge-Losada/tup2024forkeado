package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDarDeAltaCuenta_CuentaExistente() {
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setNumeroCuenta(12345);

        when(cuentaDao.find(12345)).thenReturn(cuentaExistente);

        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setNumeroCuenta(12345);

        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(nuevaCuenta, 12345678));
    }

    @Test
    public void testDarDeAltaCuenta_CuentaNoSoportada() {
        Cuenta cuentaNoSoportada = new Cuenta();
        cuentaNoSoportada.setTipoCuenta(null);  
        cuentaNoSoportada.setNumeroCuenta(54321);

        assertThrows(IllegalArgumentException.class, () -> cuentaService.darDeAltaCuenta(cuentaNoSoportada, 12345678));
    }

    @Test
    public void testDarDeAltaCuenta_ClienteYaTieneCuentaDeEseTipo() throws TipoCuentaAlreadyExistsException {
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setNumeroCuenta(54321);
        nuevaCuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        nuevaCuenta.setMoneda(TipoMoneda.PESOS);

        doThrow(new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda")).when(clienteService).agregarCuenta(nuevaCuenta, 12345678);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(nuevaCuenta, 12345678));
    }

    @Test
    void testDarDeAltaCuenta_CuentaCreadaExitosamente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setTitular(cliente);  
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        
        when(cuentaDao.find(1L)).thenReturn(null);
        doNothing().when(clienteService).agregarCuenta(cuenta, 12345678L);

        
        cuentaService.darDeAltaCuenta(cuenta, 12345678L);

        
        verify(cuentaDao).save(cuenta);
        verify(clienteService).agregarCuenta(cuenta, 12345678L);
    }
}

