package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaValidator {

    public void validarCuentas(Cuenta origen, Cuenta destino, TransferenciaDto transferenciaDto) {
        if (origen.getMoneda() == null) {
            throw new BusinessLogicException("La cuenta origen no tiene asignada una moneda.");
        }

        TipoMoneda monedaTransferencia;
        try {
            monedaTransferencia = TipoMoneda.valueOf(transferenciaDto.getMoneda().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessLogicException("La moneda de la transferencia es inválida.");
        }

        if (destino != null && !destino.getMoneda().equals(monedaTransferencia)) {
            throw new BusinessLogicException("La cuenta destino no maneja la misma moneda que la transferencia.");
        }

        if (!origen.getMoneda().equals(monedaTransferencia)) {
            throw new BusinessLogicException("La moneda de la transferencia no coincide con la de la cuenta origen.");
        }

        if (origen.getBalance() < transferenciaDto.getMonto()) {
            throw new BusinessLogicException("La cuenta origen no tiene fondos suficientes.");
        }
    }
}
