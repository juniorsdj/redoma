package model.bean;

public class IndicesFillFactor {

    private String nomeDoBanco;
    private String nomeDoIndice;
    private int fillFactor;
    private String nomeDaTabela;

    public String getNomeDoBanco() {
        return nomeDoBanco;
    }

    public void setNomeDoBanco(String nomeDoBanco) {
        this.nomeDoBanco = nomeDoBanco;
    }

    public String getNomeDoIndice() {
        return nomeDoIndice;
    }

    public void setNomeDoIndice(String nomeDoIndice) {
        this.nomeDoIndice = nomeDoIndice;
    }

    public int getFillFactor() {
        return fillFactor;
    }

    public void setFillFactor(int fillFactor) {
        this.fillFactor = fillFactor;
    }

    public String getNomeDaTabela() {
        return nomeDaTabela;
    }

    public void setNomeDaTabela(String nomeDaTabela) {
        this.nomeDaTabela = nomeDaTabela;
    }

    public String nomedoSelect() {
        String nomedoSelect = String.format("***Índices com Fill Factor***%n");
        return nomedoSelect;
    }

    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-20s|%-30s|%-20s|%-20s|\n",
                "nomeDoBanco", "nomeDoIndice", "fillFactor", "nomeDaTabela");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-20s|%-30s|%-20s|%-20s|\n", nomeDoBanco,
                nomeDoIndice, fillFactor, nomeDaTabela);
        return corpoTabela;
    }

}
