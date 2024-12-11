package ar.edu.utn.frbb.tup.controller.dto;

public class CuentaResponseDto {
    private long numeroCuenta;
    private String tipoCuenta;
    private String moneda;
    private double balance;
    private String titularNombreCompleto;


    // Constructor
    public CuentaResponseDto(long numeroCuenta, String tipoCuenta, String moneda, double balance, String titularNombre) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.moneda = moneda;
        this.balance = balance;
        this.titularNombreCompleto = titularNombre;
    }

    // Getters y setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getTitularNombreCompleto() {
        return titularNombreCompleto;
    }

    public void setTitularNombreCompleto(String titularNombre) {
        this.titularNombreCompleto = titularNombreCompleto;
    }
}
