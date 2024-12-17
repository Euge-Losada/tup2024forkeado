package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cuentas")
public class MovimientoController {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping("/{numeroCuenta}/movimientos")
    public ResponseEntity<Map<String, Object>> obtenerHistorialMovimientos(@PathVariable long numeroCuenta) {
        System.out.println("Buscando movimientos para la cuenta: " + numeroCuenta);
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(numeroCuenta);

        if (movimientos.isEmpty()) {
            return ResponseEntity.ok(Map.of("estado", "SIN MOVIMIENTOS", "mensaje", "No se encontraron movimientos."));
        }

        return ResponseEntity.ok(Map.of(
                "numeroCuenta", numeroCuenta,
                "movimientos", movimientos.stream().map(mov -> Map.of(
                        "fecha", mov.getFecha(),
                        "tipo", mov.getTipo(),
                        "descripcion", mov.getDescripcion(),
                        "monto", mov.getMonto()
                )).collect(Collectors.toList())
        ));
    }

    @PostMapping("/{numeroCuenta}")
    public ResponseEntity<Void> registrarMovimiento(
            @PathVariable long numeroCuenta,
            @RequestBody Movimiento movimiento) {
        try {
            movimientoService.registrarMovimiento(numeroCuenta, movimiento); // Llama al servicio para registrar el movimiento
            return ResponseEntity.ok().build(); // Devuelve un 200 OK si se registra correctamente
        } catch (Exception e) {
            // Devuelve un error genérico en caso de excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
