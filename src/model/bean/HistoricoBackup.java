
package model.bean;

import java.sql.Date;

public class HistoricoBackup {
    private int IdBackup;
    private String NomedoServidor;
    private String NomedoBanco;
    private String NomedoBackup;
    private String TipodoBackup;
    private double TamanhodoBackup;
    private String UsuarioqueRealizou;
    private Date DatadeCriacao;
    private Date DataInicioBackup;
    private Date DataFimBackup;

    public int getIdBackup() {
        return IdBackup;
    }

    public void setIdBackup(int IdBackup) {
        this.IdBackup = IdBackup;
    }

    public String getNomedoServidor() {
        return NomedoServidor;
    }

    public void setNomedoServidor(String NomedoServidor) {
        this.NomedoServidor = NomedoServidor;
    }

    public String getNomedoBanco() {
        return NomedoBanco;
    }

    public void setNomedoBanco(String NomedoBanco) {
        this.NomedoBanco = NomedoBanco;
    }

    public String getNomedoBackup() {
        return NomedoBackup;
    }

    public void setNomedoBackup(String NomedoBackup) {
        this.NomedoBackup = NomedoBackup;
    }

    public String getTipodoBackup() {
        return TipodoBackup;
    }

    public void setTipodoBackup(String TipodoBackup) {
        this.TipodoBackup = TipodoBackup;
    }

    public double getTamanhodoBackup() {
        return TamanhodoBackup;
    }

    public void setTamanhodoBackup(double TamanhodoBackup) {
        this.TamanhodoBackup = TamanhodoBackup;
    }

    public String getUsuarioqueRealizou() {
        return UsuarioqueRealizou;
    }

    public void setUsuarioqueRealizou(String UsuarioqueRealizou) {
        this.UsuarioqueRealizou = UsuarioqueRealizou;
    }

    public Date getDatadeCriacao() {
        return DatadeCriacao;
    }

    public void setDatadeCriacao(Date DatadeCriacao) {
        this.DatadeCriacao = DatadeCriacao;
    }

    public Date getDataInicioBackup() {
        return DataInicioBackup;
    }

    public void setDataInicioBackup(Date DataInicioBackup) {
        this.DataInicioBackup = DataInicioBackup;
    }

    public Date getDataFimBackup() {
        return DataFimBackup;
    }

    public void setDataFimBackup(Date DataFimBackup) {
        this.DataFimBackup = DataFimBackup;
    }  
    
    public String cabecalho() {
        String cabecalhoTabela = String.format("|%-10s|%-20s|%-20s|%-40s|%-20s|%-20s|%-40s|%-20s|%-20s|%-20s|\n",
                "IdBackup", "NomedoServidor", "NomedoBanco", "NomedoBackup", "TipodoBackup", "TamanhodoBackup", "UsuarioqueRealizou", "DatadeCriacao", "DataInicioBackup", "DataFimBackup");
        return cabecalhoTabela;
    }

    @Override
    public String toString() {
        String corpoTabela = String.format("|%-10s|%-20s|%-20s|%-40s|%-20s|%-20s|%-40s|%-20s|%-20s|%-20s|\n",
                IdBackup, NomedoServidor, NomedoBanco, NomedoBackup, TipodoBackup, TamanhodoBackup, UsuarioqueRealizou, DatadeCriacao, DataInicioBackup, DataFimBackup);
        return corpoTabela;
    }
}
