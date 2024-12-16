package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.InvalidDateFormatException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Component
public class ClienteValidator {

    public void validate(ClienteDto clienteDto) {
        validateFechaFormato(clienteDto.getFechaNacimiento());
        validateTipoPersona(clienteDto.getTipoPersona());
        LocalDate fechaNacimiento = LocalDate.parse(clienteDto.getFechaNacimiento(), DateTimeFormatter.ISO_LOCAL_DATE);
        validateFechaNacimiento(fechaNacimiento);
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


    private void validateFechaFormato(String fechaNacimientoStr) {
        try {
            // Validamos que la fecha esté en el formato correcto (yyyy-MM-dd)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(fechaNacimientoStr, formatter);
            //LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);

            // Si la fecha es válida, la validamos en el siguiente paso
           // validateFechaNacimiento(fechaNacimiento);

        } catch (DateTimeParseException e) {
            // Si el formato no es válido, lanzamos una excepción personalizada
            throw new InvalidDateFormatException("La fecha de nacimiento debe usar el formato año-mes-día (YYYY-MM-DD).");
        }
    }

    private void validateFechaNacimiento(LocalDate fechaNacimiento) {
        // Validación de fecha no futura
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }

        // Validación de edad
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor de 18 años.");
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
