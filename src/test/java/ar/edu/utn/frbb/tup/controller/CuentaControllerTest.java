package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.CuentaResponseDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @Mock
    private CuentaValidator cuentaValidator;

    @InjectMocks
    private CuentaController cuentaController;

    private CuentaDto cuentaDto;

    @BeforeEach
    public void setUp() {
        cuentaDto = new CuentaDto();
        cuentaDto.setNumeroCuenta(12345L);
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setBalanceInicial(1000.0);
    }

    @Test
    public void testCrearCuentaSuccess() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setNumeroCuenta(12345L);
        cuentaMock.setTipoCuenta(TipoCuenta.CAJA_AHORRO); // Aseguramos que no sea null
        cuentaMock.setMoneda(TipoMoneda.PESOS);           // Aseguramos que no sea null
        cuentaMock.setBalance(1000.0);
        when(cuentaService.darDeAltaCuenta(any(CuentaDto.class))).thenReturn(cuentaMock);

        CuentaResponseDto response = cuentaController.crearCuenta(cuentaDto).getBody();

        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
        assertNotNull(response);
        assertEquals(12345L, response.getNumeroCuenta());
        assertEquals("CAJA_AHORRO", response.getTipoCuenta());
        assertEquals("PESOS", response.getMoneda());
        assertEquals(1000.0, response.getBalance());
    }

    @Test
    public void testCrearCuentaValidationError() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        doThrow(new IllegalArgumentException("Error de validación"))
                .when(cuentaValidator).validate(any(CuentaDto.class));

        assertThrows(IllegalArgumentException.class, () -> cuentaController.crearCuenta(cuentaDto));
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, times(0)).darDeAltaCuenta(any(CuentaDto.class));
    }
}
