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
public class IndicesNaoUtilizados {

    private String nomeDaTabela;
    private String nomeDoIndice;

    public String getNomeDaTabela() {
        return nomeDaTabela;
    }

    public void setNomeDaTabela(String nomeDaTabela) {
        this.nomeDaTabela = nomeDaTabela;
    }

    public String getNomeDoIndice() {
        return nomeDoIndice;
    }

    public void setNomeDoIndice(String nomeDoIndice) {
        this.nomeDoIndice = nomeDoIndice;
    }
    
    public String nomedoSelect() {
        String nomedoSelect = String.format("***Índices Não Utilizados***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-20s|%-30s|\n",
                "nomeDaTabela", "nomeDoIndice");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-20s|%-30s|\n", nomeDaTabela,
                nomeDoIndice);
        return corpoTabela;
    }

}
