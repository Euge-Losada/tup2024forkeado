package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteDaoTest {

    @Mock
    private ClienteDao clienteDao;

    @BeforeEach
    public void setUp() {
        clienteDao = new ClienteDaoImpl();

    }

    @Test
    public void testGuardarYBuscarCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        clienteDao.save(cliente);

        Cliente encontrado = clienteDao.find(12345678L, true);
        assertNotNull(encontrado);
        assertEquals(12345678L, encontrado.getDni());
        assertEquals("Juan", encontrado.getNombre());
        assertEquals("Pérez", encontrado.getApellido());
    }

    @Test
    public void testBuscarClienteNoExistente() {
        Cliente encontrado = clienteDao.find(99999999L, true);
        assertNull(encontrado);
    }
}
