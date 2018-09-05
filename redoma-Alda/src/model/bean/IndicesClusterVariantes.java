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
public class IndicesClusterVariantes {

    private int id;
    private String name;
    private String systemType;
    private String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String nomedoSelect() {
        String nomedoSelect = String.format("***Indices Clustered Variantes***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-20s|%-30s|%-20s|%-20s|\n", "id", "name", "systemType", "descricao");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-20s|%-30s|%-20s|%-20s|\n", id, name, systemType, descricao);
        return corpoTabela;
    }

}
