package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClienteTest {

    private Cliente cliente;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        // Crear un ClienteDto con datos de prueba
        clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("1985-05-10");
        clienteDto.setBanco("Banco Nación");
        clienteDto.setTipoPersona("F");  // Tipo persona como "F" (Física)

        // Crear el cliente usando el constructor que acepta ClienteDto
        cliente = new Cliente(clienteDto);
    }

    @Test
    void testConstructorCliente() {
        // Verificar que el cliente se haya creado correctamente
        assertNotNull(cliente);
        assertEquals(12345678L, cliente.getDni());
        assertEquals("Juan", cliente.getNombre());
        assertEquals("Perez", cliente.getApellido());
        assertEquals("Banco Nación", cliente.getBanco());
        assertEquals("PERSONA_FISICA", cliente.getTipoPersona().toString());  // Verifica el tipo de persona
        assertEquals(LocalDate.now(), cliente.getFechaAlta());  // Fecha de alta debe ser la actual
    }

    @Test
    void testAddCuenta() {
        // Crear una cuenta
        Cuenta cuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 500.0);

        // Añadir la cuenta al cliente
        cliente.addCuenta(cuenta);

        // Verificar que la cuenta fue añadida correctamente y el titular se haya establecido
        assertTrue(cliente.getCuentas().contains(cuenta));
        assertEquals(cliente, cuenta.getTitular());
    }

    @Test
    void testAddCuentaConCuentaExistente() {
        // Crear y añadir una cuenta
        Cuenta cuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 500.0);
        cliente.addCuenta(cuenta);

        // Intentar añadir la misma cuenta nuevamente
        cliente.addCuenta(cuenta);

        // Verificar que la cuenta no se haya añadido de nuevo
        assertEquals(1, cliente.getCuentas().size());  // Solo debería haber una cuenta
    }

    @Test
    void testTieneCuenta() {
        // Crear cuentas
        Cuenta cuenta1 = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS, 1000.0);
        Cuenta cuenta2 = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES, 200.0);

        // Añadir la cuenta1 al cliente
        cliente.addCuenta(cuenta1);

        // Verificar que el cliente tiene la cuenta de tipo CAJA_AHORRO y PESOS
        assertTrue(cliente.tieneCuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS));

        // Verificar que el cliente no tiene la cuenta de tipo CAJA_AHORRO y DOLARES
        assertFalse(cliente.tieneCuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES));

        // Añadir la cuenta2 al cliente
        cliente.addCuenta(cuenta2);

        // Verificar que ahora tiene la cuenta de tipo CAJA_AHORRO y DOLARES
        assertTrue(cliente.tieneCuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES));
    }

    @Test
    void testToString() {
        // Crear un ClienteDto y Cliente
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("1985-05-10");
        clienteDto.setBanco("Banco Nación");
        clienteDto.setTipoPersona("F");

        Cliente cliente = new Cliente(clienteDto);

        // Verificar el formato del método toString
        String expectedString = "Cliente{tipoPersona=F, banco='Banco Nación', fechaAlta=" + LocalDate.now() + ", cuentas=[]}";
        assertTrue(cliente.toString().contains("Banco Nación"));
        assertTrue(cliente.toString().contains("F"));
    }
}
