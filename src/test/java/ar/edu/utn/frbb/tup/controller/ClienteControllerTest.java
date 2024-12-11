package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteValidator clienteValidator;

    private ClienteDto clienteDto;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setTipoPersona("F");
    }

    @Test
    public void testCrearClienteSuccess() throws ClienteAlreadyExistsException {
        // Configuración del mock
        Cliente clienteMock = new Cliente();
        clienteMock.setDni(12345678L);
        when(clienteService.darDeAltaCliente(any(ClienteDto.class))).thenReturn(clienteMock);

        // Ejecución
        Cliente result = clienteController.crearCliente(clienteDto);

        // Verificaciones
        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
    }

    @Test
    public void testCrearClienteThrowsException() throws ClienteAlreadyExistsException {
        doThrow(new ClienteAlreadyExistsException("El cliente ya existe"))
                .when(clienteService).darDeAltaCliente(any(ClienteDto.class));

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteController.crearCliente(clienteDto));
        verify(clienteValidator, times(1)).validate(clienteDto);
    }



    @Test
    public void testObtenerClientePorDni() {
        Cliente cliente = new Cliente(clienteDto);
        when(clienteService.buscarClientePorDni(12345678L)).thenReturn(cliente);

        Cliente result = clienteController.getCliente(12345678L);

        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
        assertEquals("Juan", result.getNombre());
        assertEquals("Pérez", result.getApellido());

        verify(clienteService, times(1)).buscarClientePorDni(12345678L);
    }
}
