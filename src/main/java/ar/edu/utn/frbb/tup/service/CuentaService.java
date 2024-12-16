package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ClienteService clienteService;

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
        // Crear una nueva cuenta mapeada desde el DTO
        Cuenta cuenta = new Cuenta(
                TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase()),
                TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase()),
                cuentaDto.getBalanceInicial()
        );

        // Generar automáticamente el número de cuenta
        cuenta.setNumeroCuenta(cuenta.generarNumeroCuentaUnico());

        // Buscar al cliente titular
        Cliente clienteTitular = clienteService.buscarClientePorDni(cuentaDto.getDniTitular());
        if (clienteTitular == null) {
            throw new BusinessLogicException("El cliente con DNI " + cuentaDto.getDniTitular() + " no existe.");
        }

        // Verificar si ya existe una cuenta del mismo tipo y moneda para este cliente
        boolean existeCuentaDelMismoTipoYMoneda = clienteTitular.getCuentas().stream()
                .anyMatch(c -> c.getTipoCuenta().equals(TipoCuenta.valueOf(cuentaDto.getTipoCuenta().toUpperCase())) &&
                        c.getMoneda().equals(TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase())));

        if (existeCuentaDelMismoTipoYMoneda) {
            throw new CuentaAlreadyExistsException("El cliente ya tiene una cuenta del tipo " + cuentaDto.getTipoCuenta() +
                    " y moneda " + cuentaDto.getMoneda());
        }

        // Asociar la cuenta al cliente y guardar
        clienteTitular.addCuenta(cuenta);
        cuentaDao.save(cuenta);
        clienteDao.save(clienteTitular);

        return cuenta;
    }

    public void realizarTransferenciaInterna(Cuenta cuentaOrigen, Cuenta cuentaDestino, TransferenciaDto transferenciaDto)
            throws NoAlcanzaException, CantidadNegativaException {
        double monto = transferenciaDto.getMonto();

        // Realizar débitos y créditos
        cuentaOrigen.debitarDeCuenta(monto);
        cuentaDestino.acreditarEnCuenta(monto);

        // Registrar movimientos
        registrarMovimientos(cuentaOrigen, cuentaDestino, transferenciaDto);

        // Guardar cuentas actualizadas
        cuentaDao.save(cuentaOrigen);
        cuentaDao.save(cuentaDestino);
    }

    public void registrarMovimientoExterno(Cuenta cuentaOrigen, TransferenciaDto transferenciaDto) throws NoAlcanzaException, CantidadNegativaException {
        cuentaOrigen.debitarDeCuenta(transferenciaDto.getMonto());

        Movimiento movimientoDebito = new Movimiento(
                cuentaOrigen.getNumeroCuenta(),
                "DÉBITO",
                transferenciaDto.getMonto(),
                "Transferencia externa a cuenta " + transferenciaDto.getCuentaDestino(),
                LocalDateTime.now()
        );

        movimientoService.registrarMovimiento(cuentaOrigen.getNumeroCuenta(), movimientoDebito);
        cuentaDao.save(cuentaOrigen);
    }

    public void verificarFondosSuficientes(Cuenta cuenta, double monto) {
        if (cuenta.getBalance() < monto) {
            throw new BusinessLogicException("Fondos insuficientes en la cuenta.");
        }
    }

    private void registrarMovimientos(Cuenta cuentaOrigen, Cuenta cuentaDestino, TransferenciaDto transferenciaDto) {
        Movimiento movimientoDebito = new Movimiento(
                cuentaOrigen.getNumeroCuenta(),
                "DÉBITO",
                transferenciaDto.getMonto(),
                "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta(),
                LocalDateTime.now()
        );

        Movimiento movimientoCredito = new Movimiento(
                cuentaDestino.getNumeroCuenta(),
                "CRÉDITO",
                transferenciaDto.getMonto(),
                "Transferencia desde cuenta " + cuentaOrigen.getNumeroCuenta(),
                LocalDateTime.now()
        );

        movimientoService.registrarMovimiento(cuentaOrigen.getNumeroCuenta(), movimientoDebito);
        movimientoService.registrarMovimiento(cuentaDestino.getNumeroCuenta(), movimientoCredito);
    }
}

