package ar.edu.utn.frbb.tup.controller.dto;

public class CuentaDto {

    private long dniTitular;
    private String tipoCuenta;
    private String moneda;
    private double balanceInicial;



    public long getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
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

    public double getBalanceInicial() {
        return balanceInicial;
    }

    public void setBalanceInicial(double balanceInicial) {
        this.balanceInicial = balanceInicial;
    }
}
