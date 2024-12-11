package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaValidatorTest {

    private CuentaValidator validator;
    private CuentaDto cuentaDto;

    @BeforeEach
    public void setUp() {
        validator = new CuentaValidator();
        cuentaDto = new CuentaDto();
    }

    @Test
    public void testValidateSuccess() {
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setBalanceInicial(1000.0);
        cuentaDto.setDniTitular(12345678L);

        assertDoesNotThrow(() -> validator.validate(cuentaDto));
    }

    @Test
    public void testValidateTipoCuentaError() {
        cuentaDto.setTipoCuenta("INVALIDO");

        Exception e = assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
        assertTrue(e.getMessage().contains("El tipo de cuenta es inválido"));
    }

    @Test
    public void testValidateBalanceNegativo() {
        // Configuración mínima válida para CuentaDto
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setDniTitular(12345678L);

        // Configuramos el balance inválido
        cuentaDto.setBalanceInicial(-1000.0);

        // Verificamos que se lance la excepción esperada
        Exception e = assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
        assertTrue(e.getMessage().contains("El balance inicial no puede ser negativo"));
    }



}
