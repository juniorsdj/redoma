/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

/**
 *
 * @author QueroDelivery
 */
public class MaioresIndicesPorTamanho {

    private long idDoObjeto;
    private String indexName;
    private double indexSizeKB;

    public long getIdDoObjeto() {
        return idDoObjeto;
    }

    public void setIdDoObjeto(long idDoObjeto) {
        this.idDoObjeto = idDoObjeto;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public double getIndexSizeKB() {
        return indexSizeKB;
    }

    public void setIndexSizeKB(double indexSizeKB) {
        this.indexSizeKB = indexSizeKB;
    }
    
    
    
    public String nomedoSelect() {
        String nomedoSelect = String.format("***Maiores Indices***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-20s|%-30s|%-20s|\n", "idDoObjeto", "indexName", "indexSizeKB");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-20s|%-30s|%-20s|\n", idDoObjeto, indexName, indexSizeKB);
        return corpoTabela;
    }
}
