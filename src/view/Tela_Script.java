/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.IndicesClusterVariantes;
import model.bean.IndicesClustered;
import model.bean.IndicesFillFactor;
import model.bean.IndicesNaoUtilizados;
import model.bean.IndicesNoPrimary;
import model.bean.IndicesNonClustered;
import model.bean.MaioresIndices;
import model.bean.MaioresIndicesPorTamanho;
import model.bean.TabelasHeap;
import util.Bases;
import view.BasesDinamicas;
import util.ConnectionFactory;
import view.Tela_Data_Base;

/**
 *
 * @author aldam
 */
public class Tela_Script extends javax.swing.JFrame {

    public static Connection conection;
    public static List<Bases> selectedBancos;
    private List<Object> listaComTodosSelects = new ArrayList<>();

    public List<Object> getListaComTodosSelects() {
        return listaComTodosSelects;
    }

    public void setListaComTodosSelects(List<Object> listaComTodosSelects) {
        this.listaComTodosSelects = listaComTodosSelects;
    }

    private int idBanco;

    public int getIdBanco() {
        return idBanco;
    }
    private String nomeBanco;

    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * Creates new form Tela_Data_Base
     */
    public Tela_Script(Connection conection) {
        this.conection = conection;
        initComponents();
    }

    public Tela_Script() {
        initComponents();
    }

    public Tela_Script(Connection conection, List<Bases> selectedBancos) {
        this.conection = conection;
        this.selectedBancos = selectedBancos;
        this.idBanco = selectedBancos.get(0).getId();
        this.nomeBanco = selectedBancos.get(0).getNome();
        initComponents();
    }

    private Tela_Data_Base telaDataBase;
    private Tela_Resumo telaResumo;

    public Tela_Data_Base getTelaDataBase() {
        return telaDataBase;
    }

    public void setTelaDataBase(Tela_Data_Base telaDataBase) {
        this.telaDataBase = telaDataBase;
    }

    public Tela_Resumo getTelaResumo() {
        return telaResumo;
    }

    public void setTelaResumo(Tela_Resumo telaResumo) {
        this.telaResumo = telaResumo;
    }

    public void selecionarTop10IndexesSize() {
        List<String> listaResultSetString = new ArrayList<>();
        String selectTop10 = "use " + getNomeBanco()
                + "\n"
                + "SELECT top 10 i.[name] AS IndexName\n"
                + "    ,SUM(s.[used_page_count]) * 8 AS IndexSizeKB\n"
                + "	, i.[object_id] as objectId\n"
                + "FROM sys.dm_db_partition_stats AS s\n"
                + "INNER JOIN sys.indexes AS i ON s.[object_id] = i.[object_id]\n"
                + "    AND s.[index_id] = i.[index_id]\n"
                + "GROUP BY i.[name], i.[object_id]\n"
                + "ORDER BY 2 desc\n";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conection.prepareStatement(selectTop10);
            rs = stmt.executeQuery();
            //para percorrer o resultSet
            MaioresIndicesPorTamanho mit = new MaioresIndicesPorTamanho();
            listaResultSetString.add(mit.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            System.out.println(mit.nomedoSelect());
            listaResultSetString.add(mit.cabecalho());
            System.out.println(mit.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                mit.setIdDoObjeto(rs.getLong("objectId"));
                mit.setIndexName(rs.getString("IndexName"));
                mit.setIndexSizeKB(rs.getDouble("IndexSizeKB"));

                System.out.println(mit.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(mit.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarIndicesNonClustered() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        int parametroNonClustered = Integer.parseInt(txtIndiceNonClustered.getText());
        String selectNonClustered = "USE " + getNomeBanco() + ";"
                + "SELECT object_name(SysBases.object_id) AS nomeTabela ,\n"
                + "SisIndex.name AS  nomeIndice,\n"
                + "SysBases.Index_type_desc AS descricaoIndice,\n"
                + "SysBases.avg_fragmentation_in_percent AS fragmentacao\n"
                + "FROM sys.dm_db_index_physical_stats(db_id(DB_NAME()), NULL, NULL, NULL , 'DETAILED') SysBases\n"
                + "JOIN sys.tables SisTabelas WITH (nolock) ON SysBases.object_id = SisTabelas.object_id\n"
                + "JOIN sys.indexes SisIndex WITH (nolock) ON SysBases.object_id = SisIndex.object_id AND SysBases.index_id = SisIndex.index_id\n"
                + "WHERE SisTabelas.is_ms_shipped = 0 and index_type_desc = 'NONCLUSTERED INDEX' "
                + "and SysBases.avg_fragmentation_in_percent >= " + parametroNonClustered + "\n"
                + "order by SysBases.avg_fragment_size_in_pages desc";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conection.prepareStatement(selectNonClustered);
            rs = stmt.executeQuery();

            IndicesNonClustered inc = new IndicesNonClustered();
            listaResultSetString.add(inc.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            System.out.println(inc.nomedoSelect());
            listaResultSetString.add(inc.cabecalho());
            System.out.println(inc.cabecalho());
            while (rs.next()) {//enquanto houver próximo;

                inc.setNomeTabela(rs.getString("nomeTabela"));
                inc.setNomeIndice(rs.getString("nomeIndice"));
                inc.setDescricaoIndice(rs.getString("descricaoIndice"));
                inc.setFragmentacao(rs.getInt("fragmentacao"));

                System.out.println(inc.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(inc.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarIndicesClustered() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        int parametroClustered = Integer.parseInt(txtIndiceClustered.getText());
        String selectClustered = "USE " + getNomeBanco() + ";"
                + "SELECT object_name(SysBases.object_id) AS nomeTabela ,\n"
                + "SisIndex.name AS  nomeIndice,\n"
                + "SysBases.Index_type_desc AS descricaoIndice,\n"
                + "SysBases.avg_fragmentation_in_percent AS fragmentacao\n"
                + "FROM sys.dm_db_index_physical_stats(db_id(DB_NAME()), NULL, NULL, NULL , 'DETAILED') SysBases\n"
                + "JOIN sys.tables SisTabelas WITH (nolock) ON SysBases.object_id = SisTabelas.object_id\n"
                + "JOIN sys.indexes SisIndex WITH (nolock) ON SysBases.object_id = SisIndex.object_id AND SysBases.index_id = SisIndex.index_id\n"
                + "WHERE SisTabelas.is_ms_shipped = 0 and index_type_desc = 'CLUSTERED INDEX' "
                + "and SysBases.avg_fragmentation_in_percent >= " + parametroClustered + "\n"
                + "order by SysBases.avg_fragment_size_in_pages desc";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conection.prepareStatement(selectClustered);
            rs = stmt.executeQuery();

            IndicesClustered ic = new IndicesClustered();
            listaResultSetString.add(ic.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            System.out.println(ic.nomedoSelect());
            listaResultSetString.add(ic.cabecalho());
            System.out.println(ic.cabecalho());
            while (rs.next()) {//enquanto houver próximo;

                ic.setNomeTabela(rs.getString("nomeTabela"));
                ic.setNomeIndice(rs.getString("nomeIndice"));
                ic.setDescricaoIndice(rs.getString("descricaoIndice"));
                ic.setFragmentacao(rs.getInt("fragmentacao"));

                System.out.println(ic.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(ic.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarFillFactor() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        int parametroFill = Integer.parseInt(txtFillFactor.getText());
        String selectFill = "USE " + getNomeBanco() + ";"
                + "SELECT DB_NAME() AS nomeDoBanco, i.name AS nomeDoIndice, \n"
                + "                 i.fill_factor AS fill_Factor, b.table_name as nomeDaTabela\n"
                + "               FROM sys.indexes AS i\n"
                + "                inner join sys.data_spaces AS ds ON i.data_space_id = ds.data_space_id\n"
                + "                inner join sys.filegroups as fg on fg.data_space_id = ds.data_space_id\n"
                + "                inner join sys.objects as o on o.object_id = i.object_id\n"
                + "                inner join sys.master_files as smf on smf.data_space_id = ds.data_space_id\n"
                + "                inner join sys.databases as db on db.database_id = smf.database_id\n"
                + "                INNER JOIN information_schema.tables AS b\n"
                + "                 ON (OBJECT_ID(b.table_name) = i.object_id) \n"
                + "                 AND b.table_type = 'BASE TABLE'\n"
                + "                WHERE i.fill_factor < " + parametroFill + "\n"
                + "                ORDER BY i.fill_factor DESC";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conection.prepareStatement(selectFill);
            rs = stmt.executeQuery();

            IndicesFillFactor iff = new IndicesFillFactor();
            listaResultSetString.add(iff.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            System.out.println(iff.nomedoSelect());
            listaResultSetString.add(iff.cabecalho());
            System.out.println(iff.cabecalho());
            while (rs.next()) {//enquanto houver próximo;

                iff.setNomeDoBanco(rs.getString("nomeDoBanco"));
                iff.setNomeDoIndice(rs.getString("nomeDoIndice"));
                iff.setFillFactor(rs.getInt("fill_Factor"));
                iff.setNomeDaTabela(rs.getString("nomeDaTabela"));

                System.out.println(iff.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(iff.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarIndicesNaoUtilizados() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        String selectidxNaoU = "USE " + getNomeBanco() + ";"
                + "SELECT  OBJECT_NAME(i.[object_id]) AS nomeDaTabela ,\n"
                + "                     i.name as nomeDoIndice\n"
                + "                FROM    sys.indexes AS i\n"
                + "                       INNER JOIN sys.objects AS o ON i.[object_id] = o.[object_id]\n"
                + "                WHERE   i.index_id NOT IN ( SELECT  s.index_id\n"
                + "                                            FROM    sys.dm_db_index_usage_stats AS s\n"
                + "                                        WHERE   s.[object_id] = i.[object_id]\n"
                + "                                                  AND i.index_id = s.index_id\n"
                + "                                                 AND database_id = DB_ID() )\n"
                + "                      AND o.[type] = 'U'\n"
                + "                ORDER BY OBJECT_NAME(i.[object_id]) ASC";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conection.prepareStatement(selectidxNaoU);
            rs = stmt.executeQuery();

            IndicesNaoUtilizados idxNaoUtilizados = new IndicesNaoUtilizados();

            listaResultSetString.add(idxNaoUtilizados.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            listaResultSetString.add(idxNaoUtilizados.cabecalho());
            System.out.println(idxNaoUtilizados.nomedoSelect());
            System.out.println(idxNaoUtilizados.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                idxNaoUtilizados.setNomeDaTabela(rs.getString("nomeDaTabela"));
                idxNaoUtilizados.setNomeDoIndice(rs.getString("nomeDoIndice"));
                System.out.println(idxNaoUtilizados.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(idxNaoUtilizados.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarTop10() {
        //so essa função que passa nao pelo nome mais pelo id do banco
        List<String> listaResultSetString = new ArrayList<>();
        String selectTop10 = "select TOP (10) object_id as idDoObjeto,\n"
                + "                index_type_desc as descricaoDoIndice,\n"
                + "                avg_fragmentation_in_percent as fragmentacao\n"
                + "                from sys.dm_db_index_physical_stats (   \n"
                + "                " + this.getIdBanco() + "\n"
                + "                 , null\n"
                + "                , null\n"
                + "                 , null\n"
                + "                  , null\n"
                + "                ) \n"
                + "                where avg_fragmentation_in_percent >= 0 and\n"
                + "                index_id > 0\n"
                + "                order by avg_fragmentation_in_percent desc";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conection.prepareStatement(selectTop10);
            rs = stmt.executeQuery();
            //para percorrer o resultSet
            MaioresIndices mi = new MaioresIndices();
            listaResultSetString.add(mi.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            System.out.println(mi.nomedoSelect());
            listaResultSetString.add(mi.cabecalho());
            System.out.println(mi.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                mi.setIdDoObjeto(rs.getLong("idDoObjeto"));
                mi.setDescricaoDoIndice(rs.getString("descricaoDoIndice"));
                mi.setFragmentacao(rs.getDouble("fragmentacao"));

                System.out.println(mi.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(mi.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        getListaComTodosSelects().add(listaResultSetString);

    }

    public void selecionarIndicesNoPrimary() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        //PARA USAR O COMANDO USE BANCO EU DEVO COLOCAR ; PARA FUNCIONAR
        String selectNoPrimary = "USE " + getNomeBanco() + ";"
                + "Select distinct OBJECT_NAME(i.object_id) As Tabela,\n"
                + "             i.name As Indice, \n"
                + "             i.object_id IddoObjetoIndice,\n"
                + "             fg.name as GrupoDeARQUIVO,\n"
                + "             i.type_desc as TipoDeIndice,\n"
                + "             o.type as TipoTabela\n"
                + "  from sys.indexes as i  \n"
                + "       inner join sys.data_spaces AS ds ON i.data_space_id = ds.data_space_id\n"
                + "       inner join sys.filegroups as fg on fg.data_space_id = ds.data_space_id \n"
                + "       inner join sys.objects as o on o.object_id = i.object_id\n"
                + " where((o.type ='U') and (fg.filegroup_guid IS NULL) and (OBJECT_NAME(i.object_id) <> 'sysdiagrams'))";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conection.prepareStatement(selectNoPrimary);
            rs = stmt.executeQuery();

            IndicesNoPrimary inp = new IndicesNoPrimary();
            //adicionando o que faz o select, ou seja seu script
            listaResultSetString.add(inp.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            listaResultSetString.add(inp.cabecalho());
            System.out.println(inp.nomedoSelect());
            System.out.println(inp.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                inp.setNomeDaTabela(rs.getString("Tabela"));
                inp.setNomeDoIndice(rs.getString("Indice"));
                inp.setIdDoObjeto(rs.getLong("IddoObjetoIndice"));
                inp.setGrupoDeArquivo(rs.getString("GrupoDeARQUIVO"));
                inp.setTipoDeIndice(rs.getString("TipoDeIndice"));
                inp.setTipoDeTabela(rs.getString("TipoTabela"));

                System.out.println(inp.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(inp.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarVariantes() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        String selectVariantes = "USE " + getNomeBanco() + ";"
                + "SELECT distinct\n"
                + "                clmns.column_id AS id,\n"
                + "                clmns.name AS name,\n"
                + "                ISNULL(baset.name, N'') AS systemType,\n"
                + "                ik.type_desc as descricao\n"
                + "                FROM information_schema.tables,\n"
                + "                sys.tables AS tbl\n"
                + "                INNER JOIN sys.all_columns AS clmns ON clmns.object_id=tbl.object_id\n"
                + "                LEFT OUTER JOIN sys.types AS baset ON (baset.user_type_id = clmns.system_type_id and baset.user_type_id = baset.system_type_id) or ((baset.system_type_id = clmns.system_type_id) and (baset.user_type_id = clmns.user_type_id) and (baset.is_user_defined = 0) and (baset.is_assembly_type = 1))\n"
                + "                LEFT OUTER JOIN sys.indexes AS ik ON ik.object_id = clmns.object_id\n"
                + "                LEFT OUTER JOIN sys.index_columns AS cik ON cik.index_id = ik.index_id and cik.column_id = clmns.column_id and cik.object_id = clmns.object_id and 0 = cik.is_included_column\n"
                + "	         WHERE table_type = 'base table' \n"
                + "                and ik.type = 1\n"
                + "                and baset.name in ('nchar','ntext','nvarchar','sql_variant','text','varbinary','varchar')\n"
                + "                ORDER BY\n"
                + "                id ASC;";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conection.prepareStatement(selectVariantes);
            rs = stmt.executeQuery();

            IndicesClusterVariantes icv = new IndicesClusterVariantes();
            listaResultSetString.add(icv.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            listaResultSetString.add(icv.cabecalho());
            System.out.println(icv.nomedoSelect());
            System.out.println(icv.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                icv.setId(rs.getInt("id"));
                icv.setName(rs.getString("name"));
                icv.setSystemType(rs.getString("systemType"));
                icv.setDescricao(rs.getString("descricao"));

                System.out.println(icv.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(icv.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarTabelasHeap() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        String selectHeap = "USE " + getNomeBanco() + ";"
                + "SELECT DISTINCT i.name as  NomeIndice\n"
                + "                     , i.type_desc as Descricao\n"
                + "                     ,is_unique as chaveUnica\n"
                + "                   ,is_primary_key as chavePrimaria\n"
                + "                     FROM sys.indexes AS i\n"
                + "                INNER JOIN sys.data_spaces AS ds ON i.data_space_id = ds.data_space_id\n"
                + "                inner join sys.filegroups as fg on fg.data_space_id = ds.data_space_id \n"
                + "                inner join sys.objects as o on o.object_id = i.object_id\n"
                + "                inner join sys.master_files as smf on smf.data_space_id = ds.data_space_id\n"
                + "                inner join sys.databases as db on db.database_id = smf.database_id\n"
                + "                INNER JOIN information_schema.tables AS b\n"
                + "                 ON (OBJECT_ID(b.table_name) = i.object_id) \n"
                + "               AND b.table_type = 'BASE TABLE'\n"
                + "                WHERE is_hypothetical = 0 AND i.index_id<> 0";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conection.prepareStatement(selectHeap);
            rs = stmt.executeQuery();

            TabelasHeap sHeap = new TabelasHeap();
            listaResultSetString.add(sHeap.nomedoSelect());
            //adicionando o cabeçaho da tabela no array de String posicao get(0)
            listaResultSetString.add(sHeap.cabecalho());
            System.out.println(sHeap.nomedoSelect());
            System.out.println(sHeap.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                sHeap.setNomeIndice(rs.getString("NomeIndice"));
                sHeap.setDescricao(rs.getString("Descricao"));
                sHeap.setChaveUnica(rs.getInt("chaveUnica"));
                sHeap.setChavePrimaria(rs.getInt("chavePrimaria"));

                System.out.println(sHeap.toString());
                //adicionando o corpo da tabela no array de String
                listaResultSetString.add(sHeap.toString());
            }
        } catch (SQLException ex) {
            System.err.println("Erro :" + ex);
        } finally {
            ConnectionFactory.fecharStmtERs(stmt, rs);
        }
        System.out.println("*************************************************");
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel1 = new javax.swing.JPanel();
        jLabelOpcaoPermissao = new javax.swing.JLabel();
        jCheckBoxPermissaoSA = new javax.swing.JCheckBox();
        jCheckBoxPermisssaoEscrita = new javax.swing.JCheckBox();
        jLabelOpcaoIndex = new javax.swing.JLabel();
        jCheckBoxFragNaoCluster = new javax.swing.JCheckBox();
        jCheckBoxFragCluster = new javax.swing.JCheckBox();
        jScrollBar1 = new javax.swing.JScrollBar();
        jCheckBoxFillFactor = new javax.swing.JCheckBox();
        jCheckBoxIndiceNaoUtilizado = new javax.swing.JCheckBox();
        jCheckBoxMaiorIndice = new javax.swing.JCheckBox();
        jSlider3 = new javax.swing.JSlider();
        jCheckBoxTableHeap = new javax.swing.JCheckBox();
        checkFileGroupPrimary = new javax.swing.JCheckBox();
        jCheckBoxIndexClusterTipoVariavel = new javax.swing.JCheckBox();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        txtIndiceNonClustered = new javax.swing.JTextField();
        txtIndiceClustered = new javax.swing.JTextField();
        txtFillFactor = new javax.swing.JTextField();
        jCheckBoxMaiorIndice1 = new javax.swing.JCheckBox();
        jPanelFuncao = new javax.swing.JPanel();
        jBtVoltar = new javax.swing.JButton();
        jBtAvançar = new javax.swing.JButton();
        jBtCancelar = new javax.swing.JButton();
        jBtAjuda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Opções de Script");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione as opções abaixo"));

        jLabelOpcaoPermissao.setText("Opções de Permissionamento:");

        jCheckBoxPermissaoSA.setText("Usuários com permissão de SA");

        jCheckBoxPermisssaoEscrita.setText("Usuários com permissão de escrita");

        jLabelOpcaoIndex.setText("Opções de Indexação");

        jCheckBoxFragNaoCluster.setText("Índices com fragmentação não clusterizado");
        jCheckBoxFragNaoCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFragNaoClusterActionPerformed(evt);
            }
        });

        jCheckBoxFragCluster.setText("Índices com fragmentação clusterizado");
        jCheckBoxFragCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFragClusterActionPerformed(evt);
            }
        });

        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheckBoxFillFactor.setText("Índices com Fillfactor menor ");
        jCheckBoxFillFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFillFactorActionPerformed(evt);
            }
        });

        jCheckBoxIndiceNaoUtilizado.setText("Índices não utilizados");

        jCheckBoxMaiorIndice.setText("Os top 10 - maiores Índices");
        jCheckBoxMaiorIndice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMaiorIndiceActionPerformed(evt);
            }
        });

        jSlider3.setMajorTickSpacing(10);
        jSlider3.setPaintLabels(true);
        jSlider3.setPaintTicks(true);
        jSlider3.setValue(0);

        jCheckBoxTableHeap.setText("Tabelas heap");
        jCheckBoxTableHeap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTableHeapActionPerformed(evt);
            }
        });

        checkFileGroupPrimary.setText("Índices localizado no Filegroup PRIMARY");
        checkFileGroupPrimary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkFileGroupPrimaryActionPerformed(evt);
            }
        });

        jCheckBoxIndexClusterTipoVariavel.setText("Índices clusterizados com tipos de dados variantes");

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(0);

        jSlider2.setMajorTickSpacing(10);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        jSlider2.setValue(0);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSlider1, org.jdesktop.beansbinding.ELProperty.create("${value}"), txtIndiceNonClustered, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtIndiceNonClustered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIndiceNonClusteredActionPerformed(evt);
            }
        });

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSlider2, org.jdesktop.beansbinding.ELProperty.create("${value}"), txtIndiceClustered, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSlider3, org.jdesktop.beansbinding.ELProperty.create("${value}"), txtFillFactor, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jCheckBoxMaiorIndice1.setText("Os top 10 - maiores Índices por tamanho");
        jCheckBoxMaiorIndice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMaiorIndice1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxMaiorIndice)
                            .addComponent(jCheckBoxPermisssaoEscrita)
                            .addComponent(jLabelOpcaoPermissao)
                            .addComponent(jCheckBoxPermissaoSA)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jCheckBoxFillFactor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtFillFactor, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jCheckBoxFragNaoCluster)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(10, 10, 10)
                                .addComponent(txtIndiceNonClustered, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(177, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxMaiorIndice1)
                            .addComponent(jCheckBoxTableHeap)
                            .addComponent(jCheckBoxIndiceNaoUtilizado)
                            .addComponent(jLabelOpcaoIndex)
                            .addComponent(checkFileGroupPrimary)
                            .addComponent(jCheckBoxIndexClusterTipoVariavel)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBoxFragCluster)
                                .addGap(6, 6, 6)
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIndiceClustered, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelOpcaoPermissao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxPermissaoSA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxPermisssaoEscrita)
                .addGap(41, 41, 41)
                .addComponent(jLabelOpcaoIndex)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxFragNaoCluster)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIndiceNonClustered, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIndiceClustered, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBoxFragCluster, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxFillFactor)
                    .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFillFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxIndiceNaoUtilizado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxMaiorIndice, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxMaiorIndice1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkFileGroupPrimary)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxIndexClusterTipoVariavel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jCheckBoxTableHeap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelFuncao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBtVoltar.setText("<< Voltar ");
        jBtVoltar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtVoltarActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtVoltar);

        jBtAvançar.setText("Avançar >>");
        jBtAvançar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtAvançar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAvançarActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtAvançar);

        jBtCancelar.setText("Cancelar");
        jBtCancelar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtCancelarActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtCancelar);

        jBtAjuda.setText("Ajuda");
        jBtAjuda.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanelFuncao.add(jBtAjuda);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelFuncao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtVoltarActionPerformed(java.awt.event.ActionEvent evt) {

        if (conection != null) {
            BasesDinamicas tdb = new BasesDinamicas(conection);
            // Zerando o resumoOpcoes porque pode ser que mude o banco
            BasesDinamicas.resumoOpcoes = new ArrayList<>();
            tdb.setVisible(true);
            this.dispose();
        }
    }
    private void jBtAvançarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtAvançarActionPerformed

        //permissoes de escrita
        if (jCheckBoxPermissaoSA.isSelected()) {
            //adicione no resumoOpcoes
            BasesDinamicas.resumoOpcoes.add(jCheckBoxPermissaoSA.getText());
            //rode o metodo dele
        }
        if (jCheckBoxPermisssaoEscrita.isSelected()) {
            //adicione no resumoOpcoes
            BasesDinamicas.resumoOpcoes.add(jCheckBoxPermisssaoEscrita.getText());
            //rode o metodo dele
        }
        
        //opções de script
        if (jCheckBoxFragNaoCluster.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxFragNaoCluster.getText() + " com fragmentação " + txtIndiceNonClustered.getText());
            selecionarIndicesNonClustered();
        }
        if (jCheckBoxFragCluster.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxFragCluster.getText() + " com fragmentação " + txtIndiceClustered.getText());
            selecionarIndicesClustered();
        }
        if (jCheckBoxFillFactor.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxFillFactor.getText() + " com fragmentação " + txtFillFactor.getText());
            selecionarFillFactor();
        }
        if (jCheckBoxIndiceNaoUtilizado.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxIndiceNaoUtilizado.getText());
            selecionarIndicesNaoUtilizados();
        }
        if (jCheckBoxMaiorIndice.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxMaiorIndice.getText());
            selecionarTop10();
        }
        
        if(jCheckBoxMaiorIndice1.isSelected()){
            BasesDinamicas.resumoOpcoes.add(jCheckBoxMaiorIndice1.getText());
            selecionarTop10IndexesSize();
        }
        if (checkFileGroupPrimary.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(checkFileGroupPrimary.getText());
            selecionarIndicesNoPrimary();
        }
        if (jCheckBoxIndexClusterTipoVariavel.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxIndexClusterTipoVariavel.getText());
            selecionarVariantes();
        }
        if (jCheckBoxTableHeap.isSelected()) {
            BasesDinamicas.resumoOpcoes.add(jCheckBoxTableHeap.getText());
            selecionarTabelasHeap();
        }

        List<String> temporaria = BasesDinamicas.resumoOpcoes;
        for (String opcoes : temporaria) {
            System.out.println(opcoes.toString());
        }

        //ligações entre as telas
        if (getTelaResumo() == null) {//nao foi ainda para tela resumo
            //a tela script apontando para a tela resumo
            //cria nova instancia
            //passando esta tela como parametro
            //A tela script conhece o caminho de ida para a tela resumo
            setTelaResumo(new Tela_Resumo(getListaComTodosSelects()));
            //a tela resumo conhece o caminho de volta para a tela script
            getTelaResumo().setTelaScript(this);
            //adicionar tudo ao painel de resumo
            getTelaResumo().adicionarTudoNaTelaResumo();
        } else {
            //ja foi pra tela resumo e voltou pra essa
            //passa denovo caso eu retire algo da lista ou coloque passando esta nova como parametro
            getTelaResumo().setListacomlistaComTodosSelects(getListaComTodosSelects());
            getTelaResumo().adicionarTudoNaTelaResumo();
        }
        //chama a tela resumo
        getTelaResumo().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jBtAvançarActionPerformed

    private void jBtCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtCancelarActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja Realmente Sair ?");
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jBtCancelarActionPerformed

    private void txtIndiceNonClusteredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIndiceNonClusteredActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIndiceNonClusteredActionPerformed

    private void checkFileGroupPrimaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkFileGroupPrimaryActionPerformed

    }//GEN-LAST:event_checkFileGroupPrimaryActionPerformed

    private void jCheckBoxMaiorIndiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMaiorIndiceActionPerformed

    }//GEN-LAST:event_jCheckBoxMaiorIndiceActionPerformed

    private void jCheckBoxFillFactorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFillFactorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxFillFactorActionPerformed

    private void jCheckBoxFragClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFragClusterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxFragClusterActionPerformed

    private void jCheckBoxFragNaoClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFragNaoClusterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxFragNaoClusterActionPerformed

    private void jCheckBoxTableHeapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTableHeapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxTableHeapActionPerformed

    private void jCheckBoxMaiorIndice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMaiorIndice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxMaiorIndice1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tela_Script.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Script.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Script.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Script.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_Script(conection).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkFileGroupPrimary;
    private javax.swing.JButton jBtAjuda;
    private javax.swing.JButton jBtAvançar;
    private javax.swing.JButton jBtCancelar;
    private javax.swing.JButton jBtVoltar;
    private javax.swing.JCheckBox jCheckBoxFillFactor;
    private javax.swing.JCheckBox jCheckBoxFragCluster;
    private javax.swing.JCheckBox jCheckBoxFragNaoCluster;
    private javax.swing.JCheckBox jCheckBoxIndexClusterTipoVariavel;
    private javax.swing.JCheckBox jCheckBoxIndiceNaoUtilizado;
    private javax.swing.JCheckBox jCheckBoxMaiorIndice;
    private javax.swing.JCheckBox jCheckBoxMaiorIndice1;
    private javax.swing.JCheckBox jCheckBoxPermissaoSA;
    private javax.swing.JCheckBox jCheckBoxPermisssaoEscrita;
    private javax.swing.JCheckBox jCheckBoxTableHeap;
    private javax.swing.JLabel jLabelOpcaoIndex;
    private javax.swing.JLabel jLabelOpcaoPermissao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelFuncao;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JTextField txtFillFactor;
    private javax.swing.JTextField txtIndiceClustered;
    private javax.swing.JTextField txtIndiceNonClustered;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
