/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author aldam
 */
public class IndiceTabelaHeap {

    /**
     * @return the nomeDoArq
     */
    public String getNomeDoArq() {
        return nomeDoArq;
    }

    /**
     * @param nomeDoArq the nomeDoArq to set
     */
    public void setNomeDoArq(String nomeDoArq) {
        this.nomeDoArq = nomeDoArq;
    }

    /**
     * @return the TipoArquivo
     */
    public String getTipoArquivo() {
        return TipoArquivo;
    }

    /**
     * @param TipoArquivo the TipoArquivo to set
     */
    public void setTipoArquivo(String TipoArquivo) {
        this.TipoArquivo = TipoArquivo;
    }

    /**
     * @return the duplaKey
     */
    public boolean isDuplaKey() {
        return duplaKey;
    }

    /**
     * @param duplaKey the duplaKey to set
     */
    public void setDuplaKey(boolean duplaKey) {
        this.duplaKey = duplaKey;
    }

    /**
     * @return the primaryKey
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the constante
     */
    public boolean isConstante() {
        return constante;
    }

    /**
     * @param constante the constante to set
     */
    public void setConstante(boolean constante) {
        this.constante = constante;
    }

    /**
     * @return the fillFactor
     */
    public int getFillFactor() {
        return fillFactor;
    }

    /**
     * @param fillFactor the fillFactor to set
     */
    public void setFillFactor(int fillFactor) {
        this.fillFactor = fillFactor;
    }

    /**
     * @return the padded
     */
    public long getPadded() {
        return padded;
    }

    /**
     * @param padded the padded to set
     */
    public void setPadded(long padded) {
        this.padded = padded;
    }

    /**
     * @return the desabilitado
     */
    public boolean isDesabilitado() {
        return desabilitado;
    }

    /**
     * @param desabilitado the desabilitado to set
     */
    public void setDesabilitado(boolean desabilitado) {
        this.desabilitado = desabilitado;
    }

    /**
     * @return the indeceBloq
     */
    public boolean isIndeceBloq() {
        return indeceBloq;
    }

    /**
     * @param indeceBloq the indeceBloq to set
     */
    public void setIndeceBloq(boolean indeceBloq) {
        this.indeceBloq = indeceBloq;
    }

    /**
     * @return the indeceBloqDB
     */
    public boolean isIndeceBloqDB() {
        return indeceBloqDB;
    }

    /**
     * @param indeceBloqDB the indeceBloqDB to set
     */
    public void setIndeceBloqDB(boolean indeceBloqDB) {
        this.indeceBloqDB = indeceBloqDB;
    }
    private String nomeDoBanco;
    private String nomeTabela;
    private String tipoDeIndice;
    private int restricaoIndice;
    private String nomeDoArq;
    private String TipoArquivo;
    private boolean duplaKey;
    private boolean primaryKey;
    private boolean constante;
    private int fillFactor;
    private long padded;
    private boolean desabilitado;
    private boolean indeceBloq;
    private boolean indeceBloqDB;
}
