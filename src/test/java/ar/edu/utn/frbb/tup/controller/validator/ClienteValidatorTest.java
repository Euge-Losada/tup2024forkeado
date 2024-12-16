package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteValidatorTest {

    private ClienteValidator validator;
    private ClienteDto clienteDto;

    @BeforeEach
    public void setUp() {
        validator = new ClienteValidator();
        clienteDto = new ClienteDto();
    }

    @Test
    public void testValidateSuccess() {
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setDni(12345678L);
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Nación");

        assertDoesNotThrow(() -> validator.validate(clienteDto));
    }

    @Test
    public void testValidateTipoPersonaError() {
        // Configuración mínima para evitar NullPointerException
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setDni(12345678L);
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setBanco("Banco Nación");

        // Configuramos un valor inválido para TipoPersona
        clienteDto.setTipoPersona("X");

        Exception e = assertThrows(IllegalArgumentException.class, () -> validator.validate(clienteDto));
        assertTrue(e.getMessage().contains("El tipo de persona no es correcto"));
    }


    @Test
    public void testValidateDniError() {
        // Configuración mínima válida para el ClienteDto
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setTipoPersona("F");
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setBanco("Banco Nación");

        // Configuramos el DNI inválido
        clienteDto.setDni(123L);

        // Verificamos que lanza la excepción esperada
        Exception e = assertThrows(IllegalArgumentException.class, () -> validator.validate(clienteDto));
        assertTrue(e.getMessage().contains("El DNI debe ser un número positivo de 7 u 8 dígitos"));
    }

}
