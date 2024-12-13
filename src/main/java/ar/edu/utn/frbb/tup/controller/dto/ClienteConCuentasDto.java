package ar.edu.utn.frbb.tup.controller.dto;

import java.util.List;

public class ClienteConCuentasDto {
    private String nombre;
    private String apellido;
    private long dni;
    private String banco;
    private List<CuentaResponseDto> cuentas;

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public List<CuentaResponseDto> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaResponseDto> cuentas) {
        this.cuentas = cuentas;
    }
}
