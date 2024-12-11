package ar.edu.utn.frbb.tup.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteDtoTest {

    @Test
    public void testClienteDto() {
        ClienteDto dto = new ClienteDto();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setDni(12345678L);
        dto.setTipoPersona("F");
        dto.setBanco("Banco Nación");

        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellido());
        assertEquals(12345678L, dto.getDni());
        assertEquals("F", dto.getTipoPersona());
        assertEquals("Banco Nación", dto.getBanco());
    }
}
