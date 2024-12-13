package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;


@Component
public class ClienteValidator {

    public void validate(ClienteDto clienteDto) {
        validateTipoPersona(clienteDto.getTipoPersona());
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
        validateDni(clienteDto.getDni());
        validateNombre(clienteDto.getNombre());
        validateApellido(clienteDto.getApellido());
        validateBanco(clienteDto.getBanco());
    }

    private void validateTipoPersona(String tipoPersona) {
        try {
            TipoPersona.fromString(tipoPersona);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de persona no es correcto");
        }
    }

    private void validateFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor de 18 años");
        }
    }

    public void validateDni(long dni) {
        String dniStr = String.valueOf(dni);
        if (dni <= 0 || dniStr.length() < 7 || dniStr.length() > 8) {
            throw new IllegalArgumentException("El DNI debe ser un número positivo de 7 u 8 dígitos");
        }
    }

    private void validateNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
    }

    private void validateApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
    }

    private void validateBanco(String banco) {
        if (banco != null && banco.trim().isEmpty()) {
            throw new IllegalArgumentException("El banco no puede ser solo espacios en blanco");
        }
    }
}
