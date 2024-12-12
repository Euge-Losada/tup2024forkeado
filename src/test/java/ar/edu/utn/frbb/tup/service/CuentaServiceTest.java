package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaServiceTest {

    @InjectMocks
    private CuentaService cuentaService;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDarDeAltaCuenta() throws Exception, CuentaAlreadyExistsException {
        // Preparar datos
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setNumeroCuenta(12345678L);
        cuentaDto.setDniTitular(87654321L);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setBalanceInicial(1000.0);

        Cliente clienteMock = mock(Cliente.class);

        // Simular clienteService y cuentaDao
        when(clienteService.buscarClientePorDni(87654321L)).thenReturn(clienteMock);
        when(cuentaDao.find(12345678L)).thenReturn(null);

        // Acción
        Cuenta cuenta = cuentaService.darDeAltaCuenta(cuentaDto);

        // Verificar resultados
        assertNotNull(cuenta);
        assertEquals(12345678L, cuenta.getNumeroCuenta());
        assertEquals(TipoCuenta.CAJA_AHORRO, cuenta.getTipoCuenta());
        assertEquals(TipoMoneda.PESOS, cuenta.getMoneda());
        assertEquals(1000.0, cuenta.getBalance());

        // Verificar interacción con clienteService y cuentaDao
        verify(clienteService, times(1)).buscarClientePorDni(87654321L);
        verify(cuentaDao, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testDarDeAltaCuentaConCuentaExistente() {
        // Preparar datos
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setNumeroCuenta(12345678L);
        cuentaDto.setDniTitular(87654321L);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setBalanceInicial(1000.0);

        when(cuentaDao.find(12345678L)).thenReturn(new Cuenta());

        // Verificar que lanza excepción
        Exception exception = assertThrows(Exception.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
        assertEquals("La cuenta con número 12345678 ya existe.", exception.getMessage());
    }
}
