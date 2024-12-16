package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferenciaValidatorTest {

    private final TransferenciaValidator validator = new TransferenciaValidator();

    @Test
    public void testValidarCuentasSuccess() {
        // Usando el constructor adecuado para crear la cuenta
        Cuenta cuentaOrigen = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 2000.0);
        Cuenta cuentaDestino = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 0.0);

        TransferenciaDto dto = new TransferenciaDto();
        dto.setMoneda("PESOS");
        dto.setMonto(1000);

        assertDoesNotThrow(() -> validator.validarCuentas(cuentaOrigen, cuentaDestino, dto));
    }

    @Test
    public void testValidarFondosInsuficientes() {
        Cuenta cuentaOrigen = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 200.0);
        TransferenciaDto dto = new TransferenciaDto();
        dto.setMoneda("PESOS");
        dto.setMonto(1000);

        BusinessLogicException e = assertThrows(BusinessLogicException.class, () -> validator.validarCuentas(cuentaOrigen, null, dto));
        assertTrue(e.getMessage().contains("no tiene fondos suficientes"));
    }
}
