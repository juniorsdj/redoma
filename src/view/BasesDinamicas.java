/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import util.Bases;
import util.ConnectionFactory;

public class BasesDinamicas extends JFrame {

    //variavel que armazena consulta as bases de dados existentes no banco
    private final String consulta = "Select name,database_id From sys.databases;";
    private JPanel contentPane;
    private List<JCheckBox> checkboxes = new ArrayList<>();
    private List<String> opcoesSelected = new ArrayList<>();
    public int count;
    //declaracao de botoes 
    private javax.swing.JButton jBtAjuda = new javax.swing.JButton();
    private javax.swing.JButton jBtAvancar = new javax.swing.JButton();
    private javax.swing.JButton jBtCancelar = new javax.swing.JButton();
    private javax.swing.JScrollBar jScrollBar1 = new javax.swing.JScrollBar();
    private javax.swing.JLabel jLbdsistemico = new javax.swing.JLabel();
    private javax.swing.JLabel jLbdUsuario = new javax.swing.JLabel();
    private javax.swing.JPanel jPanelFuncao = new javax.swing.JPanel();
    private javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
    private javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
    private Connection con;

    /**
     * Launch the application.
     *
     * @param args
     */
    public BasesDinamicas() {

    }

    private void jBtAvancarActionPerformed(java.awt.event.ActionEvent evt) {

        int count = 0;
        String database = null;
        for (int i = 0; i < this.checkboxes.size(); i++) {
            JCheckBox checkBox = this.checkboxes.get(i);
            if (checkBox.isSelected()) {
                this.opcoesSelected.add(checkBox.getName());
                database = checkboxes.get(i).getText();
            }
        }
        
        //chama de novo a conexao agora passando o nome do banco
        
        Connection novaConexao = ConnectionFactory.getConnection(ConnectionFactory.getNomeServidor(), ConnectionFactory.getUsuario(), ConnectionFactory.getSenha(), database);
        Tela_Script telascript = new Tela_Script(novaConexao, this.opcoesSelected);
        telascript.setVisible(true);
        this.dispose();
    }

    private void jBtCancelarActionPerformed(java.awt.event.ActionEvent evt) {

        int resposta = JOptionPane.showConfirmDialog(null, "Deseja Sair Realmente ?");
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void jBtAjudaActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * Create the frame.
     *
     * @param con
     */
    //monta uma tela para inclusão dos checkboxes
    public BasesDinamicas(Connection con) {
        this.con = con;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        setTitle("DataBase");
        contentPane = new JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        contentPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione o(s) DataBase(s)"));
        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBtAvancar.setText("Avançar >>");
        jBtAvancar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtCancelar.setText("Cancelar");
        jBtCancelar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtAjuda.setText("Ajuda");
        jBtAjuda.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanelFuncao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBtAvancar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAvancarActionPerformed(evt);
            }
        });

        jBtCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtCancelarActionPerformed(evt);
            }
        });

        jBtAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAjudaActionPerformed(evt);
            }
        });

        jPanelFuncao.add(jBtAvancar);
        jPanelFuncao.add(jBtCancelar);
        jPanelFuncao.add(jBtAjuda);

        ListarCheckbox(getDb(con));

        for (int i = 0; i < this.checkboxes.size(); i++) {
            JCheckBox checkBox = this.checkboxes.get(i);
            checkBox.setBounds(10, i, 10, 10);
            jPanel2.add(checkBox);
        }

        // adicionar lista de checkbox em JPnael2 e ta ok
        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(jPanel1Layout);

        jLbdUsuario.setText("Banco de Dados de Usuários");
        jLbdsistemico.setText("Banco de Dados Sistêmico");

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLbdsistemico)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                                .addComponent(jLbdUsuario))
                                        .addContainerGap(334, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLbdsistemico)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jPanelFuncao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLbdsistemico)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addGap(10, 10, 180)
                        .addComponent(jLbdUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                        .addComponent(jPanelFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, Short.MAX_VALUE))
        );

        setContentPane(contentPane);

    }

    public List<String> getOpcoesSelected() {
        return opcoesSelected;
    }

    //transforma a lista de bases em uma lista de checkbox
    private void ListarCheckbox(List<Bases> bases) {

        for (Bases base : bases) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setName(Integer.toString(base.getId()));
            checkBox.setText(base.getNome());
            //adiciona o checkbox a lista de checkbox
            this.checkboxes.add(checkBox);
        }

    }

    //realiza uma consulta sobre as bases existentes no servidor
    private List<Bases> getDb(Connection con) {
        List<Bases> listaBases = new ArrayList();
        Statement stmt;

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(consulta);
            rs.next();
            listaBases = dbToList(rs);

        } catch (SQLException ex) {
            //Logger.getLogger(BasesDinamicas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("erro no getdb");
            ex.printStackTrace();

        }
        return listaBases;
    }

    //transforma o resultado de uma consulta em uma lista de objetos do tipo Bases
    private List dbToList(ResultSet rs) {
        List bases = new ArrayList();
        try {
            while (rs.next()) {
                Bases base = new Bases();
                base.setNome(rs.getString("name"));
                base.setId(rs.getInt("database_id"));
                bases.add(base);
            }
        } catch (SQLException ex) {
            //tratamento casso ocorra algum erro ao percorrer o Resultset (resultado da consulta)
            System.out.println("erro no dbtolist");
            Logger.getLogger(BasesDinamicas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bases;
    }
}
