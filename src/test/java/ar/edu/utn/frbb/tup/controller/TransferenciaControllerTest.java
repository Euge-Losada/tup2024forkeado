package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaControllerTest {

    @InjectMocks
    private TransferenciaController transferenciaController;

    @Mock
    private TransferenciaService transferenciaService;

    @Test
    void testTransferenciaExitosa() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1001L);
        transferenciaDto.setCuentaDestino(1002L);
        transferenciaDto.setMonto(500.0);

        TransferenciaResponseDto respuesta = transferenciaController.realizarTransferencia(transferenciaDto).getBody();

        assertEquals("EXITOSA", respuesta.getEstado());
        assertEquals("Transferencia realizada con éxito", respuesta.getMensaje());

        verify(transferenciaService, times(1)).transferir(transferenciaDto);
    }

    @Test
    void testTransferenciaFallida() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();

        doThrow(new BusinessLogicException("Error en la transferencia"))
                .when(transferenciaService).transferir(any(TransferenciaDto.class));

        TransferenciaResponseDto respuesta = transferenciaController.realizarTransferencia(transferenciaDto).getBody();

        assertEquals("FALLIDA", respuesta.getEstado());
        assertEquals("Error en la transferencia: Error en la transferencia", respuesta.getMensaje());
    }
}
