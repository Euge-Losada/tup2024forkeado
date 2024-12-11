package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferenciaValidatorTest {

    private final TransferenciaValidator validator = new TransferenciaValidator();

    @Test
    public void testValidarCuentasSuccess() {
        Cuenta origen = new Cuenta().setMoneda(TipoMoneda.PESOS).setBalance(2000);
        Cuenta destino = new Cuenta().setMoneda(TipoMoneda.PESOS);
        TransferenciaDto dto = new TransferenciaDto();
        dto.setMoneda("PESOS");
        dto.setMonto(1000);

        assertDoesNotThrow(() -> validator.validarCuentas(origen, destino, dto));
    }

    @Test
    public void testValidarFondosInsuficientes() {
        Cuenta origen = new Cuenta().setMoneda(TipoMoneda.PESOS).setBalance(500);
        TransferenciaDto dto = new TransferenciaDto();
        dto.setMoneda("PESOS");
        dto.setMonto(1000);

        BusinessLogicException e = assertThrows(BusinessLogicException.class, () -> validator.validarCuentas(origen, null, dto));
        assertTrue(e.getMessage().contains("no tiene fondos suficientes"));
    }
}
