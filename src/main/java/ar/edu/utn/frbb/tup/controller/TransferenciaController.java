package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.exception.BusinessLogicException;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaResponseDto> realizarTransferencia(@RequestBody TransferenciaDto transferenciaDto) {
        try {
            transferenciaService.transferir(transferenciaDto);
            return ResponseEntity.ok(new TransferenciaResponseDto("EXITOSA", "Transferencia realizada con éxito"));
        } catch (BusinessLogicException e) {
            // Manejar errores de lógica de negocio
            return ResponseEntity.badRequest().body(new TransferenciaResponseDto("FALLIDA", "Error en la transferencia: " + e.getMessage()));
        } catch (Exception e) {
            // Manejar otros errores genéricos
            return ResponseEntity.badRequest().body(new TransferenciaResponseDto("FALLIDA", "Error inesperado: " + e.getMessage()));
        }
    }


}





