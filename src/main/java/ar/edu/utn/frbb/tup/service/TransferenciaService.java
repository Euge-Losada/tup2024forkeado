package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    private MovimientoService movimientoService;

    public void transferir(TransferenciaDto transferenciaDto) {
        if (transferenciaDto.getMonto() <= 0) {
            throw new BusinessLogicException("Error en la transferencia: Monto negativo.");
        }

        Cuenta cuentaOrigen = cuentaDao.find(transferenciaDto.getCuentaOrigen());
        if (cuentaOrigen == null || cuentaOrigen.getNumeroCuenta() <= 0) {
            throw new BusinessLogicException("La cuenta origen no existe.");
        }

        Cuenta cuentaDestino = cuentaDao.find(transferenciaDto.getCuentaDestino());
        if (cuentaDestino == null) {
            banelcoService.validarCuentaDestino(transferenciaDto.getCuentaDestino());
            realizarTransferenciaExterna(cuentaOrigen, transferenciaDto);
            return;
        }

        transferenciaValidator.validarCuentas(cuentaOrigen, cuentaDestino, transferenciaDto);

        try {
            if (!cuentaOrigen.getTitular().getBanco().equals(cuentaDestino.getTitular().getBanco())) {
                realizarTransferenciaExterna(cuentaOrigen, transferenciaDto);
            } else {
                cuentaService.realizarTransferenciaInterna(cuentaOrigen, cuentaDestino, transferenciaDto);
            }
        }catch (NoAlcanzaException | CantidadNegativaException e) {
            throw new BusinessLogicException("Error en la transferencia: " + e.getMessage());
        }
    }





    private void realizarTransferenciaExterna(Cuenta cuentaOrigen, TransferenciaDto transferenciaDto) {
        double comision = calcularComision(
                TipoMoneda.valueOf(transferenciaDto.getMoneda().toUpperCase()),
                transferenciaDto.getMonto()
        );

        double montoConComision = transferenciaDto.getMonto() + comision;

        try {
            // Débito en la cuenta origen
            cuentaOrigen.debitarDeCuenta(montoConComision);

            // Movimiento de débito
            Movimiento movimientoDebito = new Movimiento(
                    cuentaOrigen.getNumeroCuenta(),
                    "DÉBITO",
                    montoConComision,
                    "Transferencia externa a cuenta " + transferenciaDto.getCuentaDestino(),
                    LocalDateTime.now()
            );
            movimientoService.registrarMovimiento(cuentaOrigen.getNumeroCuenta(), movimientoDebito);

            // Registrar el movimiento de crédito en la cuenta destino (simulado)
            Movimiento movimientoCredito = new Movimiento(
                    transferenciaDto.getCuentaDestino(),
                    "CRÉDITO",
                    transferenciaDto.getMonto(),
                    "Transferencia externa desde cuenta " + cuentaOrigen.getNumeroCuenta(),
                    LocalDateTime.now()
            );
            movimientoService.registrarMovimiento(transferenciaDto.getCuentaDestino(), movimientoCredito);

            // Simular transferencia externa a través de Banelco
            banelcoService.realizarTransferenciaExterna(transferenciaDto);

            // Guardar cuenta origen actualizada
            cuentaDao.save(cuentaOrigen);
        } catch (CantidadNegativaException | NoAlcanzaException e) {
            throw new BusinessLogicException("Error al realizar la transferencia externa: " + e.getMessage());
        }
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