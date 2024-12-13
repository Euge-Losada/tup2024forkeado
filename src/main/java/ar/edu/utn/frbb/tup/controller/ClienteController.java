package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;

    @PostMapping
    public ResponseEntity<Object> crearCliente(@RequestBody ClienteDto clienteDto) {
        clienteValidator.validate(clienteDto); // Validación de datos
        Cliente nuevoCliente = clienteService.darDeAltaCliente(clienteDto);
        return ResponseEntity.ok("Cliente creado con éxito: " + nuevoCliente.getNombre());
    }
    @PostMapping ("/con-cuenta")
    public Cliente crearClienteConCuenta(@RequestBody ClienteDto clienteDto) {
        return clienteService.crearClienteConCuenta(clienteDto);
    }

    @GetMapping("/{dni}")
    public Cliente getCliente(@PathVariable long dni) {
        clienteValidator.validateDni(dni);
        return clienteService.buscarClientePorDni(dni);
    }
}
