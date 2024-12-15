package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.springframework.stereotype.Service;

@Service
public class BanelcoService {

    public void validarCuentaDestino(Long cuentaDestino) {
        if (cuentaDestino == null || cuentaDestino <= 0) {
            throw new BusinessLogicException("La cuenta destino no es válida en Banelco.");
        }
    }

    public void realizarTransferenciaExterna(TransferenciaDto transferenciaDto) {
        // Simular comunicación con un servicio externo.
        System.out.println("Simulando transferencia a través de Banelco:");
        System.out.println("Cuenta Origen: " + transferenciaDto.getCuentaOrigen());
        System.out.println("Cuenta Destino (otro banco): " + transferenciaDto.getCuentaDestino());
        System.out.println("Monto: " + transferenciaDto.getMonto());
        System.out.println("Moneda: " + transferenciaDto.getMoneda());
    }
}
