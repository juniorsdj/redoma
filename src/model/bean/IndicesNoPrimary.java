
package model.bean;

public class IndicesNoPrimary {
    private String nomeDaTabela;
    private String nomeDoIndice;
    private long idDoObjeto;
    private String grupoDeArquivo;
    private String tipoDeIndice;
    private String tipoDeTabela;

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

    public long getIdDoObjeto() {
        return idDoObjeto;
    }

    public void setIdDoObjeto(long idDoObjeto) {
        this.idDoObjeto = idDoObjeto;
    }

    public String getGrupoDeArquivo() {
        return grupoDeArquivo;
    }

    public void setGrupoDeArquivo(String grupoDeArquivo) {
        this.grupoDeArquivo = grupoDeArquivo;
    }

    public String getTipoDeIndice() {
        return tipoDeIndice;
    }

    public void setTipoDeIndice(String tipoDeIndice) {
        this.tipoDeIndice = tipoDeIndice;
    }

    public String getTipoDeTabela() {
        return tipoDeTabela;
    }

    public void setTipoDeTabela(String tipoDeTabela) {
        this.tipoDeTabela = tipoDeTabela;
    }
    public String cabecalho(){
        String cabecalhoTabela = String.format("|%-20s|%-30s|%-20s|%-20s|%-20s|%-20s|\n", "nomeDaTabela","nomeDoIndice","idDoObjeto","grupoDeArquivo","tipoDeIndice","tipoDeTabela");
       // System.out.println(cabecalho);
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-20s|%-30s|%-20s|%-20s|%-20s|%-20s|\n", nomeDaTabela, nomeDoIndice, idDoObjeto, grupoDeArquivo, tipoDeIndice, tipoDeTabela);
        return corpoTabela;
    }
    
}
