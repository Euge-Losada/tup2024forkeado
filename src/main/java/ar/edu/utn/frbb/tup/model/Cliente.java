package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.InvalidDateFormatException;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona{

    private TipoPersona tipoPersona;
    private String banco;
    private LocalDate fechaAlta;
    @JsonManagedReference
    private Set<Cuenta> cuentas = new HashSet<>();

    public Cliente() {
        super();
    }
    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), clienteDto.getApellido(), clienteDto.getNombre(), clienteDto.getFechaNacimiento());

        this.fechaAlta = LocalDate.now();
        this.banco = clienteDto.getBanco();
        this.tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        if (cuenta != null && !this.cuentas.contains(cuenta)) {
            this.cuentas.add(cuenta);
            cuenta.setTitular(this);
        }
    }

    public boolean tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        for (Cuenta cuenta:
                cuentas) {
            if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {
                return true;
            }
        }
        return false;
    }



    @Override
    public String toString() {
        return "Cliente{" +
                "tipoPersona=" + tipoPersona +
                ", banco='" + banco + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", cuentas=" + cuentas +
                '}';
    }
}
