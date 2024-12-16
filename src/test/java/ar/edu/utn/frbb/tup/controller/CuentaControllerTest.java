package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.CuentaResponseDto;
import ar.edu.utn.frbb.tup.controller.handler.CustomApiError;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CuentaControllerTest {

    @InjectMocks
    private CuentaController cuentaController;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private CuentaValidator cuentaValidator;

    @Test
    void testCrearCuentaSuccess() throws CuentaAlreadyExistsException {
        // Crear CuentaDto de prueba
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setBalanceInicial(1000.0);

        // Crear Cliente Mock
        Cliente titularMock = new Cliente();
        titularMock.setNombre("Juan");
        titularMock.setApellido("Perez");
        titularMock.setDni(12345678L);

        // Crear Cuenta Mock con titular asignado
        Cuenta cuentaMock = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 1000.0);
        cuentaMock.setNumeroCuenta(1001L);
        cuentaMock.setTitular(titularMock); // Asignar titular

        // Simular comportamiento del servicio
        when(cuentaService.darDeAltaCuenta(any(CuentaDto.class))).thenReturn(cuentaMock);

        // Llamar al controlador
        ResponseEntity<?> response = cuentaController.crearCuenta(cuentaDto);

        // Verificar la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

        @Test
        void testCrearCuentaAlreadyExists() throws CuentaAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);

        doThrow(new CuentaAlreadyExistsException("Cuenta ya existe"))
                .when(cuentaService).darDeAltaCuenta(any(CuentaDto.class));

        ResponseEntity<?> response = cuentaController.crearCuenta(cuentaDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof CustomApiError);

        CustomApiError error = (CustomApiError) response.getBody();
        assertTrue(error.getErrorMessage().contains("Cuenta ya existe"));

        verify(cuentaService, times(1)).darDeAltaCuenta(any(CuentaDto.class));
    }
}
