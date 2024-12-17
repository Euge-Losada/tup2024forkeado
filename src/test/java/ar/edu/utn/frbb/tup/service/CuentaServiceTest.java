package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @InjectMocks
    private CuentaService cuentaService;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private MovimientoService movimientoService;

    @Mock
    private ClienteService clienteService;

    private CuentaDto cuentaDto;
    private Cliente cliente;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setBalanceInicial(500.0);

        cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.addCuenta(cuenta);

        cuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 500.0);

    }

    @Test
    void testDarDeAltaCuentaExitosa() throws Exception, CuentaAlreadyExistsException {
        // Simula cliente encontrado sin cuentas
        when(clienteService.buscarClientePorDni(12345678L)).thenReturn(cliente);

        // Simula el guardado de la cuenta
        doNothing().when(cuentaDao).save(any(Cuenta.class));
        doNothing().when(clienteDao).save(any(Cliente.class));

        // Ejecuta el método
        cuentaService.darDeAltaCuenta(cuentaDto);

        // Verificaciones
        verify(cuentaDao, times(1)).save(any(Cuenta.class));
        verify(clienteDao, times(1)).save(any(Cliente.class));
    }

    @Test
    void testDarDeAltaCuentaDuplicada() {
        // Simula que el cliente ya tiene una cuenta con mismo tipo y moneda
        cliente.getCuentas().add(cuenta);
        when(clienteService.buscarClientePorDni(12345678L)).thenReturn(cliente);

        // Verifica que se lanza la excepción esperada
        assertThatThrownBy(() -> cuentaService.darDeAltaCuenta(cuentaDto))
                .isInstanceOf(CuentaAlreadyExistsException.class)
                .hasMessageContaining("El cliente ya tiene una cuenta del tipo");

        verify(cuentaDao, never()).save(any());
    }

    @Test
    void testRealizarTransferenciaInternaExitosa() throws Exception, NoAlcanzaException, CantidadNegativaException {
        Cuenta cuentaDestino = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 100.0);
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setMonto(200.0);

        // Simula transferencias
        doNothing().when(movimientoService).registrarMovimiento(anyLong(), any());
        doNothing().when(cuentaDao).save(any(Cuenta.class));

        // Ejecuta el método
        cuentaService.realizarTransferenciaInterna(cuenta, cuentaDestino, transferenciaDto);

        // Verificaciones
        verify(cuentaDao, times(2)).save(any(Cuenta.class));
        verify(movimientoService, times(2)).registrarMovimiento(anyLong(), any());
    }

    @Test
    void testRealizarTransferenciaInternaFondosInsuficientes() throws NoAlcanzaException, CantidadNegativaException {
        // Configuración de mocks
        Cuenta cuentaOrigen = mock(Cuenta.class); // Mock de cuenta origen
        Cuenta cuentaDestino = mock(Cuenta.class); // Mock de cuenta destino
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setMonto(1000.0); // Monto mayor al balance esperado

        // Configura el mock para lanzar la excepción al debitar
        doThrow(new NoAlcanzaException("Saldo insuficiente para realizar el débito."))
                .when(cuentaOrigen).debitarDeCuenta(anyDouble());

        // Verifica que se lanza una NoAlcanzaException al realizar la transferencia
        assertThatThrownBy(() -> cuentaService.realizarTransferenciaInterna(cuentaOrigen, cuentaDestino, transferenciaDto))
                .isInstanceOf(NoAlcanzaException.class)
                .hasMessageContaining("Saldo insuficiente para realizar el débito.");

        // Verifica que nunca se guardó la cuenta porque la transferencia falló
        verify(cuentaDao, never()).save(any());
    }




    @Test
    void testRegistrarMovimientoExternoExitoso() throws Exception, NoAlcanzaException, CantidadNegativaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setMonto(200.0);
        transferenciaDto.setCuentaDestino(222L);

        // Simula débito exitoso
        doNothing().when(movimientoService).registrarMovimiento(anyLong(), any());
        doNothing().when(cuentaDao).save(any(Cuenta.class));

        // Ejecuta el método
        cuentaService.registrarMovimientoExterno(cuenta, transferenciaDto);

        // Verificaciones
        verify(movimientoService, times(1)).registrarMovimiento(anyLong(), any());
        verify(cuentaDao, times(1)).save(any(Cuenta.class));
    }
}
