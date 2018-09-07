/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

/**
 *
 * @author PC
 */
public class MaioresIndices {

    private long idDoObjeto;
    private String descricaoDoIndice;
    private double fragmentacao;

    public long getIdDoObjeto() {
        return idDoObjeto;
    }

    public void setIdDoObjeto(long idDoObjeto) {
        this.idDoObjeto = idDoObjeto;
    }

    public String getDescricaoDoIndice() {
        return descricaoDoIndice;
    }

    public void setDescricaoDoIndice(String descricaoDoIndice) {
        this.descricaoDoIndice = descricaoDoIndice;
    }

    public double getFragmentacao() {
        return fragmentacao;
    }

    public void setFragmentacao(double fragmentacao) {
        this.fragmentacao = fragmentacao;
    }

    public String nomedoSelect() {
        String nomedoSelect = String.format("***Maiores Indices***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-20s|%-30s|%-20s|\n", "idDoObjeto", "descricaoDoIndice", "fragmentacao");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-20s|%-30s|%-20s|\n", idDoObjeto, descricaoDoIndice, fragmentacao);
        return corpoTabela;
    }
    
}
