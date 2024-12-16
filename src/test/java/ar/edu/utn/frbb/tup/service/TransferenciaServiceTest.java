package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
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
class TransferenciaServiceTest {

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private BanelcoService banelcoService;

    @Mock
    private TransferenciaValidator transferenciaValidator;

    private TransferenciaDto transferenciaDto;
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;

    @BeforeEach
    void setUp() {
        // Configuración del DTO de la transferencia
        transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1001L);
        transferenciaDto.setCuentaDestino(1002L);
        transferenciaDto.setMonto(500.0);
        transferenciaDto.setMoneda("PESOS");

        // Configuración de las cuentas
        Cliente titularOrigen = new Cliente();
        titularOrigen.setBanco("Banco1");

        Cliente titularDestino = new Cliente();
        titularDestino.setBanco("Banco1");

        cuentaOrigen = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 1000.0); // Balance suficiente
        cuentaOrigen.setNumeroCuenta(1001L);
        cuentaOrigen.setTitular(titularOrigen);

        cuentaDestino = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 200.0);
        cuentaDestino.setNumeroCuenta(1002L);
        cuentaDestino.setTitular(titularDestino);
    }

    @Test
    void testTransferenciaInternaExitosa() throws Exception, NoAlcanzaException, CantidadNegativaException {
        when(cuentaDao.find(1001L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(1002L)).thenReturn(cuentaDestino);

        // Ejecutar transferencia
        transferenciaService.transferir(transferenciaDto);

        // Verificar interacciones
        verify(transferenciaValidator).validarCuentas(cuentaOrigen, cuentaDestino, transferenciaDto);
        verify(cuentaService, times(1)).realizarTransferenciaInterna(cuentaOrigen, cuentaDestino, transferenciaDto);
        verifyNoInteractions(banelcoService);
    }

    @Test
    void testTransferenciaExternaExitosa() throws Exception {
        transferenciaDto.setCuentaDestino(99999L); // Cuenta destino externa

        when(cuentaDao.find(1001L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(99999L)).thenReturn(null); // Cuenta destino no existe localmente

        // Mock validaciones y transferencia externa
        doNothing().when(banelcoService).validarCuentaDestino(99999L);
        doNothing().when(banelcoService).realizarTransferenciaExterna(transferenciaDto);

        // Ejecutar transferencia
        transferenciaService.transferir(transferenciaDto);

        // Verificar interacciones
        verify(banelcoService, times(1)).validarCuentaDestino(99999L);
        verify(banelcoService, times(1)).realizarTransferenciaExterna(transferenciaDto);
    }

    @Test
    void testTransferenciaFondosInsuficientes() throws NoAlcanzaException, CantidadNegativaException {
        transferenciaDto.setMonto(2000.0); // Monto mayor al balance de cuentaOrigen

        when(cuentaDao.find(1001L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(1002L)).thenReturn(cuentaDestino);

        // Mockear el comportamiento de debitarDeCuenta para lanzar la excepción
        doThrow(new NoAlcanzaException("Saldo insuficiente para realizar el débito."))
                .when(cuentaService).realizarTransferenciaInterna(any(), any(), any());

        // Ejecutar y verificar la excepción esperada
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () ->
                transferenciaService.transferir(transferenciaDto)
        );

        assertEquals("Error en la transferencia: Saldo insuficiente para realizar el débito.", exception.getMessage());
        verify(cuentaService, times(1)).realizarTransferenciaInterna(cuentaOrigen, cuentaDestino, transferenciaDto);
    }


    @Test
    void testTransferenciaCuentaInexistente() {
        when(cuentaDao.find(1001L)).thenReturn(null); // Cuenta origen no existe

        // Ejecutar y verificar la excepción esperada
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () ->
                transferenciaService.transferir(transferenciaDto)
        );

        assertEquals("La cuenta origen no existe.", exception.getMessage());
        verifyNoInteractions(cuentaService, banelcoService);
    }
}
