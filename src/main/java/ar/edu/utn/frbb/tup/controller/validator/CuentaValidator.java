package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import org.springframework.stereotype.Component;

@Component
public class CuentaValidator {

    public void validate(CuentaDto cuentaDto) {
        // Validar tipoCuenta
        try {
            TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de cuenta es inválido. Debe ser CAJA_AHORRO o CUENTA_CORRIENTE.");
        }

        // Validar moneda
        if (cuentaDto.getMoneda() == null || cuentaDto.getMoneda().trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía.");
        }
        try {
            TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La moneda es inválida. Debe ser PESOS o DOLARES.");
        }

        // Validar balance inicial
        if (cuentaDto.getBalanceInicial() < 0) {
            throw new IllegalArgumentException("El balance inicial no puede ser negativo.");
        }

        // Validar DNI titular
        if (cuentaDto.getDniTitular() <= 0) {
            throw new IllegalArgumentException("El DNI del titular debe ser un número positivo.");
        }
    }
}

