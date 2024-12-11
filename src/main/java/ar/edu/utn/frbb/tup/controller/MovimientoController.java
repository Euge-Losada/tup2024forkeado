package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorCuenta(@PathVariable long numeroCuenta) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(numeroCuenta);

        if (movimientos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("/{numeroCuenta}")
    public ResponseEntity<Void> registrarMovimiento(
            @PathVariable long numeroCuenta,
            @RequestBody Movimiento movimiento) {
        movimientoService.registrarMovimiento(numeroCuenta, movimiento);
        return ResponseEntity.ok().build();
    }
}
