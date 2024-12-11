package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaResponseDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping
    public ResponseEntity<CuentaResponseDto> crearCuenta(@RequestBody CuentaDto cuentaDto)
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        // Validar los datos de la cuenta
        cuentaValidator.validate(cuentaDto);

        Cuenta cuentaCreada = cuentaService.darDeAltaCuenta(cuentaDto);

        // Crear el DTO de respuesta con nombre completo
        String titularNombreCompleto = cuentaCreada.getTitular() != null
                ? cuentaCreada.getTitular().getNombre() + " " + cuentaCreada.getTitular().getApellido()
                : null;

        // Crear el DTO de respuesta
        CuentaResponseDto responseDto = new CuentaResponseDto(
                cuentaCreada.getNumeroCuenta(),
                cuentaCreada.getTipoCuenta().toString(),
                cuentaCreada.getMoneda().toString(),
                cuentaCreada.getBalance(),
                titularNombreCompleto
        );

        return ResponseEntity.ok(responseDto);
    }

}

