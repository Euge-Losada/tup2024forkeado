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

@Service
public class TransferenciaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private MovimientoService movimientoService;

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

        // Validar cuentas y el DTO de transferencia
        transferenciaValidator.validarCuentas(cuentaOrigen, cuentaDestino, transferenciaDto);

        // Calcular la comisión de la transferencia
        double comision = calcularComision(
                TipoMoneda.valueOf(transferenciaDto.getMoneda().toUpperCase()),
                transferenciaDto.getMonto()
        );

        double montoConComision = transferenciaDto.getMonto() + comision;

        // Realizar el débito en la cuenta origen
        try {
            cuentaOrigen.debitarDeCuenta(montoConComision);
        } catch (CantidadNegativaException | NoAlcanzaException e) {
            throw new BusinessLogicException("La cuenta origen no tiene fondos suficientes: " + e.getMessage());
        }
        cuentaDao.save(cuentaOrigen);

        // Registrar movimiento de débito en la cuenta origen
        Movimiento movimientoDebito = new Movimiento(
                "DÉBITO",
                montoConComision,
                "Transferencia realizada a la cuenta " + transferenciaDto.getCuentaDestino()
        );
        movimientoService.registrarMovimiento(cuentaOrigen.getNumeroCuenta(), movimientoDebito);

        if (cuentaDestino != null) {
            // Transferencia local: registrar movimientos en origen y destino
            cuentaDestino.acreditarEnCuenta(transferenciaDto.getMonto());
            cuentaDao.save(cuentaDestino);

            Movimiento movimientoCredito = new Movimiento(
                    "CRÉDITO",
                    transferenciaDto.getMonto(),
                    "Transferencia recibida de la cuenta " + transferenciaDto.getCuentaOrigen()
            );
            movimientoService.registrarMovimiento(cuentaDestino.getNumeroCuenta(), movimientoCredito);
        } else {
            // Transferencia externa: no registrar un movimiento adicional de débito para la cuenta origen
            banelcoService.realizarTransferenciaExterna(transferenciaDto);
            System.out.println("Transferencia externa realizada...");
        }
    }

    private double calcularComision(TipoMoneda moneda, double monto) {
        if (moneda == TipoMoneda.PESOS && monto > 1_000_000) {
            return monto * 0.02; // 2% de comisión
        } else if (moneda == TipoMoneda.DOLARES && monto > 5_000) {
            return monto * 0.005; // 0.5% de comisión
        }
        return 0;
    }
}
