
package model.bean;


public class IndicesClustered {
    private String nomeTabela;
    private String nomeIndice;
    private String descricaoIndice;
    private int fragmentacao;
   
    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public String getNomeIndice() {
        return nomeIndice;
    }

    public void setNomeIndice(String nomeIndice) {
        this.nomeIndice = nomeIndice;
    }

    public String getDescricaoIndice() {
        return descricaoIndice;
    }

    public void setDescricaoIndice(String descricaoIndice) {
        this.descricaoIndice = descricaoIndice;
    }

    public int getFragmentacao() {
        return fragmentacao;
    }

    public void setFragmentacao(int fragmentacao) {
        this.fragmentacao = fragmentacao;
    }
    
    public String nomedoSelect() {
        String nomedoSelect = String.format("***Índices Clustered***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-40s|%-120s|%-20s|%-20s|\n",
                "nomeTabela", "nomeIndice", "descricaoIndice", "fragmentacao");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-40s|%-120s|%-20s|%-20s|\n",
                nomeTabela,nomeIndice, descricaoIndice, fragmentacao);
        return corpoTabela;
    }
}
