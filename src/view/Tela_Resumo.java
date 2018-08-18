/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import connection.ConnectionFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.IndicesNoPrimary;

/**
 *
 * @author aldam
 */
public class Tela_Resumo extends javax.swing.JFrame {

    /**
     * Creates new form Tela_Resumo
     */
    public Tela_Resumo() {
        initComponents();
    }
    private Tela_Script telaScript;

    public Tela_Script getTelaScript() {
        return telaScript;
    }

    public void setTelaScript(Tela_Script telaScript) {
        this.telaScript = telaScript;
    }
    private boolean diretorioCriado;
    private boolean arquivoCriado;
    private boolean textoAdicionado;
    private File filewriter, diretorio, arquivo;

    public boolean isTextoAdicionado() {
        return textoAdicionado;
    }

    public void setTextoAdicionado(boolean textoAdicionado) {
        this.textoAdicionado = textoAdicionado;
    }

    public File getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(File diretorio) {
        this.diretorio = diretorio;
    }

    public File getArquivo() {
        return arquivo;
    }

    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    public File getFilewriter() {
        return filewriter;
    }

    public void setFilewriter(File filewriter) {
        this.filewriter = filewriter;
    }

    public boolean isDiretorioCriado() {
        return diretorioCriado;
    }

    public void setDiretorioCriado(boolean diretorioCriado) {
        this.diretorioCriado = diretorioCriado;
    }

    public boolean isArquivoCriado() {
        return arquivoCriado;
    }

    public void setArquivoCriado(boolean arquivoCriado) {
        this.arquivoCriado = arquivoCriado;
    }

    //retorna o diretorio criado
    public boolean criarDiretorio() {
        //criando diretorio
        File novoDiretorio = new File("C:/Redoma");
        boolean diretorioFoiCriado = novoDiretorio.mkdir();//comando para criar diretorio     
        setDiretorioCriado(false);
        setDiretorio(novoDiretorio);
        return diretorioFoiCriado;
    }

    public boolean criarArquivoTxt(File diretorio, String nomeDoArquivo) {
        //criando arquivo no diretorio
        //se o diretorio ainda nao foi criado
        boolean arquivoFoiCriado = false;
        File infoSobreIndices = new File(diretorio, nomeDoArquivo);
        try {
            arquivoFoiCriado = infoSobreIndices.createNewFile();
            setArquivoCriado(true);
            setArquivo(infoSobreIndices);
        } catch (IOException ex) {
            Logger.getLogger(Tela_Resumo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arquivoFoiCriado;
    }

    public void excluirArquivo() {
        getArquivo().delete();
    }

    public void salvarEmTxt(String linha, File arquivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true))) {
            bw.write(linha);
            bw.newLine();
            bw.flush();//pegar toda a string do tunelamento
            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(Tela_Resumo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection pegarConexao() {
        return getTelaScript().conection;
    }

    //List<Object> lista ;
    public List<String> selecionarIndicesNoPrimary() {
        Connection con = ConnectionFactory.getConnection();

        String sql = "Select    OBJECT_NAME(i.object_id) As Tabela,\n"
                + "             i.name As Indice, \n"
                + "             i.object_id IddoObjetoIndice,\n"
                + "             fg.name as GrupoDeARQUIVO,\n"
                + "             i.type_desc as TipoDeIndice,\n"
                + "             o.type as TipoTabela\n"
                + "  from sys.indexes as i  \n"
                + "       inner join sys.data_spaces AS ds ON i.data_space_id = ds.data_space_id\n"
                + "       inner join sys.filegroups as fg on fg.data_space_id = ds.data_space_id \n"
                + "       inner join sys.objects as o on o.object_id = i.object_id\n"
                + "	  inner join sys.master_files as smf on smf.data_space_id = ds.data_space_id\n"
                + "	  inner join sys.databases as db on db.database_id = smf.database_id\n"
                + " where((o.type ='U') and (fg.filegroup_guid IS NULL) and (OBJECT_NAME(i.object_id) <> 'sysdiagrams') and db.database_id = 7)";
        //abrir conexao;
        //Connection minhaConexao = pegarConexao();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<String> listaResultSetString = new ArrayList<>();

        try {
            stmt = con.prepareStatement(sql);
            //PEGANDO O ID
            //   where((o.type ='U') and (fg.filegroup_guid IS NULL) and (OBJECT_NAME(i.object_id) <> 'sysdiagrams') and db.database_id = ?)";
            //   stmt.setInt(1, getIdDoBanco());
            rs = stmt.executeQuery();
            //para percorrer o resultSet
            IndicesNoPrimary inp = new IndicesNoPrimary();
            //adicionando o corpo da tabela no array de String posicao get(0)
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
            //fechando a conexao com o banco de dados
            ConnectionFactory.closeConnection(con);
        }
        return listaResultSetString;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanelFuncao = new javax.swing.JPanel();
        jBtVoltar = new javax.swing.JButton();
        jBtConcluir = new javax.swing.JButton();
        jBtCancelar = new javax.swing.JButton();
        jBtAjuda = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneDadosSelecionados = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Resumo");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Opções Selecionadas"));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));

        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);

        jPanelFuncao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBtVoltar.setText("<< Voltar ");
        jBtVoltar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtVoltarActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtVoltar);

        jBtConcluir.setText("Concluir");
        jBtConcluir.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtConcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtConcluirActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtConcluir);

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
        jBtAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAjudaActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtAjuda);

        jTextPaneDadosSelecionados.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setViewportView(jTextPaneDadosSelecionados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanelFuncao, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtVoltarActionPerformed
        this.getTelaScript().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jBtVoltarActionPerformed

    private void jBtConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtConcluirActionPerformed
        //saber se o diretorio foi criado
        boolean diretorio = criarDiretorio();
        if (diretorio == true) {
            System.out.println("O diretorio foi criado pela primeira vez");
            System.out.println("O diretorio foi criado em-->" + getDiretorio().getAbsolutePath());
            //o diretorio ja foi criado
        } else if (diretorio == false) {
            System.out.println("O diretorio já existe!");
            System.out.println("O diretorio já existe em-->" + getDiretorio().getAbsolutePath());
        }

        boolean arquivo = criarArquivoTxt(getDiretorio(), "indicesNoPrimary.txt");
        if (arquivo == true) {
            System.out.println("O arquivo foi criado pela primeira vez");
            System.out.println("O arquivo foi criado em-->" + getArquivo().getAbsolutePath());
            //o diretorio ja foi criado
        } else if (arquivo == false) {
            System.out.println("O arquivo já existe!");
            System.out.println("O arquivo já existe em-->" + getArquivo().getAbsolutePath());
            getArquivo().delete();//deleto para sobrescrever
            criarArquivoTxt(getDiretorio(), "indicesNoPrimary.txt");
        }
        //adicionando ao arquivo.txt
        for (String linha : selecionarIndicesNoPrimary()) {
            salvarEmTxt(linha, getArquivo());
        }
        System.exit(0);
    }//GEN-LAST:event_jBtConcluirActionPerformed

    private void jBtCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtCancelarActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja Realmente Sair ?");
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jBtCancelarActionPerformed

    private void jBtAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtAjudaActionPerformed


    }//GEN-LAST:event_jBtAjudaActionPerformed

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
            java.util.logging.Logger.getLogger(Tela_Resumo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Resumo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Resumo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Resumo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_Resumo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtAjuda;
    private javax.swing.JButton jBtCancelar;
    private javax.swing.JButton jBtConcluir;
    private javax.swing.JButton jBtVoltar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelFuncao;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPaneDadosSelecionados;
    // End of variables declaration//GEN-END:variables
}
