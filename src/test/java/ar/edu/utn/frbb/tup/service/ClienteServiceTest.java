package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.InvalidDateFormatException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    //@InjectMocks
    private ClienteService clienteService; // Servicio que estamos testeando

    @Mock
    private ClienteDao clienteDao; // Mock del DAO

    @Mock
    private ClienteValidator clienteValidator; // Mock del validador

    private ClienteDto clienteDto;

    @BeforeEach
    public void setUp() {
        clienteService = new ClienteService(clienteDao, clienteValidator);


        clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setDni(12345678L);
        clienteDto.setFechaNacimiento("1985-10-15");  // Formato correcto
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Nación");
    }

    @Test
    public void testDarDeAltaClienteSuccess() throws ClienteAlreadyExistsException {
        // Mockeamos la búsqueda en el DAO para que retorne null (no existe el cliente)
        when(clienteDao.find(12345678L, false)).thenReturn(null);

        // Llamamos al método de servicio
        Cliente cliente = clienteService.darDeAltaCliente(clienteDto);

        // Verificamos que se haya guardado el cliente correctamente
        verify(clienteDao, times(1)).save(cliente);
        assertEquals("Juan", cliente.getNombre());
        assertEquals("Pérez", cliente.getApellido());
    }

    @Test
    public void testDarDeAltaClienteAlreadyExists() {
        // Mockeamos que ya existe un cliente con el mismo DNI
        when(clienteDao.find(12345678L, false)).thenReturn(new Cliente());

        // Verificamos que se lance la excepción ClienteAlreadyExistsException
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));

        // Verificamos que no se intente guardar el cliente
        verify(clienteDao, never()).save(any(Cliente.class));
    }

    @Test
    void testDarDeAltaClienteInvalidDateFormat() {
        // Configuramos el DTO con una fecha en formato incorrecto
        clienteDto.setFechaNacimiento("15-10-1985");  // Fecha en formato incorrecto (dd-MM-yyyy)

        // Configuramos el mock para lanzar InvalidDateFormatException
        doThrow(new InvalidDateFormatException("La fecha de nacimiento debe usar el formato año-mes-día (YYYY-MM-DD)."))
                .when(clienteValidator).validate(clienteDto);

        // Ejecutamos el método y verificamos que se lanza la excepción esperada
        InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class, () -> {
            clienteService.darDeAltaCliente(clienteDto);
        });

        // Verificamos el mensaje de la excepción
        assertEquals("La fecha de nacimiento debe usar el formato año-mes-día (YYYY-MM-DD).", exception.getMessage());

        // Verificamos que el ClienteDao nunca intenta guardar un cliente
        verify(clienteDao, never()).save(any());
    }



}
