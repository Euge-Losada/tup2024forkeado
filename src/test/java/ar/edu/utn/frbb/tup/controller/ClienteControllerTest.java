package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCliente_Success() {
        // Preparar datos
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setDni(12345678);
        clienteDto.setFechaNacimiento(LocalDate.parse("1990-01-01"));

        Cliente clienteMock = new Cliente(clienteDto);
        Mockito.doNothing().when(clienteValidator).validate(clienteDto);
        Mockito.when(clienteService.darDeAltaCliente(any())).thenReturn(clienteMock);

        // Ejecutar el método
        ResponseEntity<Object> response = clienteController.crearCliente(clienteDto);

        // Verificar resultados
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Cliente creado con éxito: Juan", response.getBody());
    }

    @Test
    void crearCliente_InvalidDni() {
        // Preparar datos
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(-1);

        Mockito.doThrow(new IllegalArgumentException("DNI inválido"))
                .when(clienteValidator).validate(clienteDto);

        // Ejecutar el método
        try {
            clienteController.crearCliente(clienteDto);
        } catch (IllegalArgumentException e) {
            assertEquals("DNI inválido", e.getMessage());
        }
    }
}
