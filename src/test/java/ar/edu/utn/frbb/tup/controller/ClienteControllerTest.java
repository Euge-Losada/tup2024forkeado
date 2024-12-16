package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.handler.CustomApiError;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.InvalidDateFormatException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;  // Agregar el mock de ClienteValidator

    private ClienteDto clienteDto;

    @BeforeEach
    void setup() {
        clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Santander");
        clienteDto.setTipoCuenta("CAJA_AHORRO");
        clienteDto.setMoneda("PESOS");
        clienteDto.setBalance(1000);
    }

    @Test
    void testCrearCliente() throws Exception, ClienteAlreadyExistsException {
        // Mock de cliente a devolver
        Cliente clienteMock = new Cliente();
        clienteMock.setDni(12345678L);
        clienteMock.setNombre("Juan");
        clienteMock.setApellido("Perez");

        when(clienteService.darDeAltaCliente(any(ClienteDto.class))).thenReturn(clienteMock);

        // Llamada al método del controlador
        ResponseEntity<Object> response = clienteController.crearCliente(clienteDto);

        // Verificaciones
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Uso de HttpStatus.OK
        assertTrue(response.getBody().toString().contains("Cliente creado con éxito"));

        verify(clienteService, times(1)).darDeAltaCliente(any(ClienteDto.class));
    }

    @Test
    void testCrearClienteYaExistente() throws ClienteAlreadyExistsException {
        // Mock para lanzar una excepción cuando el cliente ya existe
        when(clienteService.darDeAltaCliente(any(ClienteDto.class)))
                .thenThrow(new ClienteAlreadyExistsException("Cliente ya existe"));

        // Llamada al método y comprobación de la respuesta con error
        ResponseEntity<Object> response = clienteController.crearCliente(clienteDto);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());  // Uso de HttpStatus.BAD_REQUEST

        // Obtener el CustomApiError y verificar el mensaje
        CustomApiError error = (CustomApiError) response.getBody();
        assertTrue(error.getErrorMessage().contains("Error: Cliente ya existe"));

        verify(clienteService, times(1)).darDeAltaCliente(any(ClienteDto.class));
    }

    @Test
    void testCrearClienteConFechaInvalida() throws Exception, ClienteAlreadyExistsException {
        // Modificamos el clienteDto para usar una fecha inválida
        clienteDto.setFechaNacimiento("1990-31-02"); // Fecha no válida (el 31 de febrero no existe)

        // Mock para simular la validación de fecha y que lance una excepción
        when(clienteService.darDeAltaCliente(any(ClienteDto.class)))
                .thenThrow(new InvalidDateFormatException("Fecha de nacimiento inválida"));

        // Llamada al método y comprobación de la respuesta con error
        ResponseEntity<Object> response = clienteController.crearCliente(clienteDto);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());  // Uso de HttpStatus.BAD_REQUEST

        // Obtener el CustomApiError y verificar el mensaje de error
        CustomApiError error = (CustomApiError) response.getBody();
        assertTrue(error.getErrorMessage().contains("Fecha de nacimiento inválida"));

        verify(clienteService, times(1)).darDeAltaCliente(any(ClienteDto.class));
    }
}


