/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author elvis
 */
public class IndicesFillFactor {
    private String nomeDoBanco;
    private String nomeIndice;
    private int fillFactor;
    private String nomeTabela;

    public String getNomeDoBanco() {
        return nomeDoBanco;
    }

    public void setNomeDoBanco(String nomeDoBanco) {
        this.nomeDoBanco = nomeDoBanco;
    }

    public String getNomeIndice() {
        return nomeIndice;
    }

    public void setNomeIndice(String nomeIndice) {
        this.nomeIndice = nomeIndice;
    }

    public int getFillFactor() {
        return fillFactor;
    }

    public void setFillFactor(int fillFactor) {
        this.fillFactor = fillFactor;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }
    
    
}