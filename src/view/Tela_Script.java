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
import model.bean.IndicesFillFactor;
import model.bean.IndicesNaoUtilizados;
import model.bean.IndicesNoPrimary;
import model.bean.MaioresIndices;
import model.bean.TabelasHeap;
import util.ConnectionFactory;
import view.Tela_Data_Base;

/**
 *
 * @author aldam
 */
public class Tela_Script extends javax.swing.JFrame {

    public static Connection conection;
    public static List<String> selectedBancos;
    private List<Object> listaComTodosSelects = new ArrayList<>();

    public List<Object> getListaComTodosSelects() {
        return listaComTodosSelects;
    }

    public void setListaComTodosSelects(List<Object> listaComTodosSelects) {
        this.listaComTodosSelects = listaComTodosSelects;
    }

    private String idBanco;

    private String getIdBanco() {
        return idBanco;
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

    public Tela_Script(Connection conection, List<String> selectedBancos) {
        this.conection = conection;
        this.selectedBancos = selectedBancos;
        this.idBanco = selectedBancos.get(0);
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

    public void selecionarTop10() {
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
            listaResultSetString.add(mi.cabecalho());
            while (rs.next()) {//enquanto houver próximo;
                mi.setIdDoObjeto(rs.getString("idDoObjeto"));
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
        getListaComTodosSelects().add(listaResultSetString);

    }

    public void selecionarIndicesNoPrimary() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        String selectNoPrimary = "Select distinct OBJECT_NAME(i.object_id) As Tabela,\n"
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
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarFillFactor() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        int parametroFill = Integer.parseInt(jTextField3.getText());
        String selectFill = "SELECT DB_NAME() AS nomeDoBanco, i.name AS nomeDoIndice, \n"
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
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarTabelasHeap() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        String selectHeap = "SELECT DISTINCT i.name as  NomeIndice\n"
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
        //adicionando o resultado do select ao listaComTodosSelects
        getListaComTodosSelects().add(listaResultSetString);
    }

    public void selecionarIndicesNaoUtilizados() {
        List<String> listaResultSetString = new ArrayList<>();
        //pegando a conexao com o banco    
        String selectidxNaoU = "SELECT  OBJECT_NAME(i.[object_id]) AS nomeDaTabela ,\n"
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
        jCheckBoxABC = new javax.swing.JCheckBox();
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
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
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

        jCheckBoxABC.setText("abc");

        jCheckBoxFragNaoCluster.setText("Indeces com fragmentação não clusterizado");

        jCheckBoxFragCluster.setText("Indeces com fragmentação clusterizado");

        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheckBoxFillFactor.setText("Índices com Fillfactor menor ");

        jCheckBoxIndiceNaoUtilizado.setText("Índices não utilizados");

        jCheckBoxMaiorIndice.setText("Os top 10 - maiores indices");
        jCheckBoxMaiorIndice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMaiorIndiceActionPerformed(evt);
            }
        });

        jSlider3.setMajorTickSpacing(10);
        jSlider3.setPaintLabels(true);
        jSlider3.setPaintTicks(true);
        jSlider3.setValue(0);

        jCheckBoxTableHeap.setText(" Tabelas heap");

        checkFileGroupPrimary.setText("Índices localizado no Filegroup PRIMARY");
        checkFileGroupPrimary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkFileGroupPrimaryActionPerformed(evt);
            }
        });

        jCheckBoxIndexClusterTipoVariavel.setText("Ííndices clusterizados com tipos de dados variantes");

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(0);

        jSlider2.setMajorTickSpacing(10);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        jSlider2.setValue(0);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSlider1, org.jdesktop.beansbinding.ELProperty.create("${value}"), jTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSlider2, org.jdesktop.beansbinding.ELProperty.create("${value}"), jTextField2, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSlider3, org.jdesktop.beansbinding.ELProperty.create("${value}"), jTextField3, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

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
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jCheckBoxFragNaoCluster)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(10, 10, 10)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(173, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxTableHeap)
                            .addComponent(jCheckBoxIndiceNaoUtilizado)
                            .addComponent(jCheckBoxABC)
                            .addComponent(jLabelOpcaoIndex)
                            .addComponent(checkFileGroupPrimary)
                            .addComponent(jCheckBoxIndexClusterTipoVariavel)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBoxFragCluster)
                                .addGap(6, 6, 6)
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxABC)
                .addGap(18, 18, 18)
                .addComponent(jLabelOpcaoIndex)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxFragNaoCluster)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBoxFragCluster, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxFillFactor)
                    .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxIndiceNaoUtilizado)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxMaiorIndice)
                .addGap(18, 18, 18)
                .addComponent(checkFileGroupPrimary)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jCheckBoxIndexClusterTipoVariavel)
                .addGap(18, 18, 18)
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
            tdb.setVisible(true);
            this.dispose();
        }
    }
    private void jBtAvançarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtAvançarActionPerformed
        if (jCheckBoxMaiorIndice.isSelected()) {
            selecionarTop10();
        }
        if (checkFileGroupPrimary.isSelected()) {
            selecionarIndicesNoPrimary();
        }
        if (jCheckBoxFillFactor.isSelected()) {
            selecionarFillFactor();
        }
        if (jCheckBoxTableHeap.isSelected()) {
            selecionarTabelasHeap();
        }
        if (jCheckBoxIndiceNaoUtilizado.isSelected()) {
            selecionarIndicesNaoUtilizados();
        }

        if (getTelaResumo() == null) {//nao foi ainda para outra tela
            //cria nova instancia
            //passando esta tela como parametro

            setTelaResumo(new Tela_Resumo(getListaComTodosSelects()));
            //a tela script agora conhece esta tela caso ela precise voltar
            //guardando o caminho de volta
            getTelaResumo().setTelaScript(this);
        }
        getTelaResumo().setListacomlistaComTodosSelects(getListaComTodosSelects());
        //ja passou pela 3 tela e voltou pra essa
        this.getTelaResumo().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jBtAvançarActionPerformed

    private void jBtCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtCancelarActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja Realmente Sair ?");
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jBtCancelarActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void checkFileGroupPrimaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkFileGroupPrimaryActionPerformed

    }//GEN-LAST:event_checkFileGroupPrimaryActionPerformed

    private void jCheckBoxMaiorIndiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMaiorIndiceActionPerformed

    }//GEN-LAST:event_jCheckBoxMaiorIndiceActionPerformed

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
    private javax.swing.JCheckBox jCheckBoxABC;
    private javax.swing.JCheckBox jCheckBoxFillFactor;
    private javax.swing.JCheckBox jCheckBoxFragCluster;
    private javax.swing.JCheckBox jCheckBoxFragNaoCluster;
    private javax.swing.JCheckBox jCheckBoxIndexClusterTipoVariavel;
    private javax.swing.JCheckBox jCheckBoxIndiceNaoUtilizado;
    private javax.swing.JCheckBox jCheckBoxMaiorIndice;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
