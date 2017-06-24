package com.example.felipe.buracometro_v5.modelo;

public class DadosEstatisticos {

    private String  dados;
    private String  dados2;
    private String  dados3;
    private int     qtdDados;
    private double  porcertagem;


    //----------------------------------------------------------------------------------------
    //                                      CONSTRTURES
    //----------------------------------------------------------------------------------------

    public DadosEstatisticos(){

    }

    public DadosEstatisticos(String dados, String dados2, String dados3, int qtdDados, double porcertagem) {
        this.dados = dados;
        this.dados = dados2;
        this.dados = dados3;
        this.qtdDados = qtdDados;
        this.porcertagem = porcertagem;
    }


    //----------------------------------------------------------------------------------------
    //                                   GETTERS e SETTERS
    //----------------------------------------------------------------------------------------

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public String getDados2() {
        return dados2;
    }

    public void setDados2(String dados2) {
        this.dados2 = dados2;
    }

    public String getDados3() {
        return dados3;
    }

    public void setDados3(String dados3) {
        this.dados3 = dados3;
    }

    public int getQtdDados() {
        return qtdDados;
    }

    public void setQtdDados(int qtdDados) {
        this.qtdDados = qtdDados;
    }

    public double getPorcertagem() {
        return porcertagem;
    }

    public void setPorcertagem(double porcertagem) {
        this.porcertagem = porcertagem;
    }


}
