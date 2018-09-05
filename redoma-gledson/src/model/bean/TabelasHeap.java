/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

/**
 *
 * @author elvis
 */
public class TabelasHeap {

    private String nomeIndice;
    private String descricao;
    private int chaveUnica;
    private int chavePrimaria;

    public String getNomeIndice() {
        return nomeIndice;
    }

    public void setNomeIndice(String nomeIndice) {
        this.nomeIndice = nomeIndice;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getChaveUnica() {
        return chaveUnica;
    }

    public void setChaveUnica(int chaveUnica) {
        this.chaveUnica = chaveUnica;
    }

    public int getChavePrimaria() {
        return chavePrimaria;
    }

    public void setChavePrimaria(int chavePrimaria) {
        this.chavePrimaria = chavePrimaria;
    }
    
    
    
    public String nomedoSelect() {
        String nomedoSelect = String.format("***Tabela Heap***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-30s|%-30s|%-20s|%-20s|\n",
                "nomeIndice", "descricao", "chaveUnica", "chavePrimaria");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-30s|%-30s|%-20s|%-20s|\n", nomeIndice,
                descricao, chaveUnica, chavePrimaria);
        return corpoTabela;
    }

}
