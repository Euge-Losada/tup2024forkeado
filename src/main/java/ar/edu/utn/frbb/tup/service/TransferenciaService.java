package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BanelcoService banelcoService;

    @Autowired
    private TransferenciaValidator transferenciaValidator;

    public void transferir(TransferenciaDto transferenciaDto) {
        Cuenta cuentaOrigen = cuentaDao.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaDao.find(transferenciaDto.getCuentaDestino());

        if (cuentaOrigen == null || cuentaOrigen.getNumeroCuenta() <= 0) {
            throw new BusinessLogicException("La cuenta origen no existe.");
        }

        if (cuentaDestino == null || cuentaDestino.getNumeroCuenta() <= 0) {
            throw new BusinessLogicException("La cuenta destino no existe.");
        }

        // Validar cuentas y transferencia
        transferenciaValidator.validarCuentas(cuentaOrigen, cuentaDestino, transferenciaDto);

        try {
            if (!cuentaOrigen.getTitular().getBanco().equals(cuentaDestino.getTitular().getBanco())) {
                realizarTransferenciaExterna(cuentaOrigen, transferenciaDto);
            } else {
                cuentaService.realizarTransferenciaInterna(cuentaOrigen, cuentaDestino, transferenciaDto);
            }
        } catch (NoAlcanzaException | CantidadNegativaException e) {
            throw new BusinessLogicException("Error en la transferencia: " + e.getMessage());
        }
    }


    private void realizarTransferenciaExterna(Cuenta cuentaOrigen, TransferenciaDto transferenciaDto) throws NoAlcanzaException, CantidadNegativaException {
        // Calcular comisión
        double comision = calcularComision(
                TipoMoneda.valueOf(transferenciaDto.getMoneda().toUpperCase()),
                transferenciaDto.getMonto()
        );

        // Validar fondos suficientes incluyendo la comisión
        double montoConComision = transferenciaDto.getMonto() + comision;
        try {
            cuentaOrigen.debitarDeCuenta(montoConComision);
        } catch (CantidadNegativaException | NoAlcanzaException e) {
            throw new BusinessLogicException(e.getMessage());
        }

        // Registrar el movimiento externo a través de CuentaService
        cuentaService.registrarMovimientoExterno(cuentaOrigen, transferenciaDto);

        // Llamar al servicio Banelco
        banelcoService.realizarTransferenciaExterna(transferenciaDto);
    }

    private double calcularComision(TipoMoneda moneda, double monto) {
        if (moneda == TipoMoneda.PESOS && monto > 1_000_000) {
            return monto * 0.02;
        } else if (moneda == TipoMoneda.DOLARES && monto > 5_000) {
            return monto * 0.005;
        }
        return 0;
    }
}
