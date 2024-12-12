package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MovimientoService movimientoService;


    @GetMapping("/{numeroCuenta}/movimientos")
    public ResponseEntity<Map<String, Object>> obtenerHistorialMovimientos(@PathVariable long numeroCuenta) {
        // Obtener movimientos de la cuenta
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(numeroCuenta);

        // Verificar si no hay movimientos
        if (movimientos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Formatear respuesta según especificación del trabajo final
        Map<String, Object> response = new HashMap<>();
        response.put("numeroCuenta", numeroCuenta);
        response.put("movimientos", movimientos.stream().map(mov -> Map.of(
                "fecha", mov.getFecha(),
                "tipo", mov.getTipo(),
                "descripcionBreve", mov.getDescripcion(),
                "monto", mov.getMonto()
        )).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{numeroCuenta}")
    public ResponseEntity<Void> registrarMovimiento(
            @PathVariable long numeroCuenta,
            @RequestBody Movimiento movimiento) {
        movimientoService.registrarMovimiento(numeroCuenta, movimiento);
        return ResponseEntity.ok().build();
    }



}
