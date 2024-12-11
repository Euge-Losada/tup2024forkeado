package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private MovimientoService movimientoService;

    @Mock
    private BanelcoService banelcoService;

    @Mock
    private TransferenciaValidator transferenciaValidator;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private Cuenta cuentaOrigen2;
    private Cuenta cuentaDestino2;

    @BeforeEach
    public void setUp() {
        cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345678L);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(2000000);

        cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(87654321L);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setBalance(1000);

        // Cuentas en DÓLARES
        cuentaOrigen2 = new Cuenta();
        cuentaOrigen2.setNumeroCuenta(22222222L);
        cuentaOrigen2.setMoneda(TipoMoneda.DOLARES);
        cuentaOrigen2.setBalance(15000.0);

        cuentaDestino2 = new Cuenta();
        cuentaDestino2.setNumeroCuenta(33333333L);
        cuentaDestino2.setMoneda(TipoMoneda.DOLARES);
        cuentaDestino2.setBalance(5000.0);
    }

    @Test
    public void testTransferenciaExitosa() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(87654321L);
        dto.setMonto(1500000);
        dto.setMoneda("PESOS");

        when(cuentaDao.find(12345678L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(87654321L)).thenReturn(cuentaDestino);
        doNothing().when(transferenciaValidator).validarCuentas(cuentaOrigen, cuentaDestino, dto);

        transferenciaService.transferir(dto);

        assertEquals(470000, cuentaOrigen.getBalance()); // 2M - 1.5M - comisión 30k
        assertEquals(1501000, cuentaDestino.getBalance());

        verify(movimientoService, times(1)).registrarMovimiento(eq(12345678L), any(Movimiento.class));
        verify(movimientoService, times(1)).registrarMovimiento(eq(87654321L), any(Movimiento.class));
        verify(cuentaDao, times(2)).save(any(Cuenta.class));
    }

    @Test
    public void testTransferenciaConComision_Pesos() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(87654321L);
        dto.setMonto(1100000.0); // Excede límite de comisión
        dto.setMoneda("PESOS");

        when(cuentaDao.find(12345678L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(87654321L)).thenReturn(cuentaDestino);
        doNothing().when(transferenciaValidator).validarCuentas(cuentaOrigen, cuentaDestino, dto);

        transferenciaService.transferir(dto);

        double comision = 1100000 * 0.02; // 2% comisión
        assertEquals(878000.0, cuentaOrigen.getBalance()); // Saldo origen actualizado
        assertEquals(1101000.0, cuentaDestino.getBalance()); // Saldo destino actualizado
    }

    @Test
    public void testTransferenciaConComision_Dolares() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(22222222L);
        dto.setCuentaDestino(33333333L);
        dto.setMonto(6000.0); // Excede límite de comisión
        dto.setMoneda("DOLARES");

        cuentaOrigen2.setMoneda(TipoMoneda.DOLARES);
        cuentaDestino2.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(22222222L)).thenReturn(cuentaOrigen2);
        when(cuentaDao.find(33333333L)).thenReturn(cuentaDestino2);
        doNothing().when(transferenciaValidator).validarCuentas(cuentaOrigen2, cuentaDestino2, dto);

        transferenciaService.transferir(dto);

        //double comision = 6000 * 0.005; // 0.5% comisión
        assertEquals(8970.0, cuentaOrigen2.getBalance()); // Saldo origen actualizado
        assertEquals(11000.0, cuentaDestino2.getBalance()); // Saldo destino actualizado
    }



    @Test
    public void testTransferenciaExterna() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(99999999L); // Cuenta externa
        dto.setMonto(1000.0);
        dto.setMoneda("PESOS");

        when(cuentaDao.find(12345678L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(99999999L)).thenReturn(null);

        transferenciaService.transferir(dto);

        verify(banelcoService, times(1)).realizarTransferenciaExterna(dto);
        verify(movimientoService, times(1)).registrarMovimiento(eq(12345678L), any(Movimiento.class));
        verify(cuentaDao, times(1)).save(cuentaOrigen);

        assertEquals(1999000, cuentaOrigen.getBalance());
    }

    @Test
    public void testTransferenciaExternaFallida() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(99999999L);
        dto.setMonto(2000.0);
        dto.setMoneda("PESOS");

        when(cuentaDao.find(12345678L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(99999999L)).thenReturn(null);
        doThrow(new BusinessLogicException("Banelco no disponible"))
                .when(banelcoService).realizarTransferenciaExterna(dto);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> transferenciaService.transferir(dto));

        assertEquals("Banelco no disponible", exception.getMessage());
    }


    @Test
    public void testCuentaOrigenInexistente() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(99999999L);
        dto.setCuentaDestino(87654321L);
        dto.setMonto(1000);
        dto.setMoneda("PESOS");

        when(cuentaDao.find(99999999L)).thenReturn(null);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> transferenciaService.transferir(dto));

        assertEquals("La cuenta origen no existe.", exception.getMessage());
    }

    @Test
    public void testFondosInsuficientes() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(87654321L);
        dto.setMonto(5000000); // Monto mayor que el balance de origen
        dto.setMoneda("PESOS");

        when(cuentaDao.find(12345678L)).thenReturn(cuentaOrigen);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> transferenciaService.transferir(dto));

        assertEquals("La cuenta origen no tiene fondos suficientes: Saldo insuficiente para realizar el débito.",
                exception.getMessage());
    }

    @Test
    public void testMonedaInvalida() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setCuentaOrigen(12345678L);
        dto.setCuentaDestino(87654321L);
        dto.setMonto(1000);
        dto.setMoneda("DOLARES");

        when(cuentaDao.find(12345678L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(87654321L)).thenReturn(cuentaDestino);

        doThrow(new BusinessLogicException("La moneda de la transferencia no coincide con la de la cuenta origen."))
                .when(transferenciaValidator).validarCuentas(cuentaOrigen, cuentaDestino, dto);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> transferenciaService.transferir(dto));

        assertEquals("La moneda de la transferencia no coincide con la de la cuenta origen.", exception.getMessage());
    }

    @Test
    public void testValidarCuentasConMonedaInvalida() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setMoneda("YENES"); // Moneda inválida

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(500.0);

        TransferenciaValidator validator = new TransferenciaValidator();

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> {
            validator.validarCuentas(cuentaOrigen, null, dto);
        });

        assertEquals("La moneda de la transferencia es inválida.", exception.getMessage());
    }




}
