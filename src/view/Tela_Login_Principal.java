/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import util.ConnectionFactory;

/**
 *
 * @author aldam
 */
public class Tela_Login_Principal extends javax.swing.JFrame {

    /**
     * Creates new form telaLogin
     */
    private Connection con;

    public void recuperarNomeServidorLocal() {
        String nomeServidor = null;
        try {
            nomeServidor = InetAddress.getLocalHost().getHostName();
            //  JOptionPane.showMessageDialog(null, "nome servidor local :" + nomeServidor.toUpperCase());
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(null, "erro ao recuperar o nome do servidor local" + ex);
        }
        this.jTextFieldNomServidor.setText(nomeServidor.toUpperCase());
    }

    public Tela_Login_Principal() {
        initComponents();
        //  Metodo para recuperar o nome do servidor local e ja deixar o campo 
        //  preenchido.
        recuperarNomeServidorLocal();
        jBtConectar.requestFocus();
    }

    public void autenticar() {
        if (jComboBoxAutenticar.getSelectedIndex() == 0) {
            // Autenticação usando usuario e senha.
            con = ConnectionFactory.getConnection(jTextFieldNomServidor.getText(), jTextFieldNomUsuario.getText(), new String(jPasswordField1.getPassword()));
        } else {
            // Autenticação pelo windows.
            this.con = ConnectionFactory.getConnectionWindows(jTextFieldNomServidor.getText());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPrincipal = new javax.swing.JPanel();
        jBtCredito = new javax.swing.JButton();
        jLsimblo = new javax.swing.JLabel();
        jLNomeServidor = new javax.swing.JLabel();
        jLautenticacao = new javax.swing.JLabel();
        jLnomUsuario = new javax.swing.JLabel();
        jLsenha = new javax.swing.JLabel();
        jTextFieldNomServidor = new javax.swing.JTextField();
        jComboBoxAutenticar = new javax.swing.JComboBox<String>();
        jTextFieldNomUsuario = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanelFuncao = new javax.swing.JPanel();
        jBtConectar = new javax.swing.JButton();
        jBtSair = new javax.swing.JButton();
        jBtAjuda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TelaLogin");
        setName("Gerenciador de dados"); // NOI18N
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanelPrincipal.setBorder(javax.swing.BorderFactory.createTitledBorder("Redoma - Tela de Login"));
        jPanelPrincipal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanelPrincipalKeyPressed(evt);
            }
        });

        jBtCredito.setText("Créditos");
        jBtCredito.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtCredito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jBtCreditoMousePressed(evt);
            }
        });
        jBtCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtCreditoActionPerformed(evt);
            }
        });

        jLsimblo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/download.jpg"))); // NOI18N

        jLNomeServidor.setText("Nome do Servidor: ");

        jLautenticacao.setText("Autenticação: ");

        jLnomUsuario.setText("Nome do Usuário:");

        jLsenha.setText("Senha:");

        jTextFieldNomServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomServidorActionPerformed(evt);
            }
        });
        jTextFieldNomServidor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextFieldNomServidorPropertyChange(evt);
            }
        });

        jComboBoxAutenticar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Autenticação do SQL Server", "Autenticação do Windows" }));
        jComboBoxAutenticar.setToolTipText("");
        jComboBoxAutenticar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAutenticarActionPerformed(evt);
            }
        });
        jComboBoxAutenticar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxAutenticarKeyPressed(evt);
            }
        });

        jTextFieldNomUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomUsuarioActionPerformed(evt);
            }
        });

        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLsimblo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLNomeServidor, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLautenticacao, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(jComboBoxAutenticar, 0, 276, Short.MAX_VALUE))
                                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldNomServidor))))
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLnomUsuario)
                                    .addComponent(jLsenha))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldNomUsuario)
                                    .addComponent(jPasswordField1))))
                        .addContainerGap())))
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLsimblo)
                    .addComponent(jBtCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLNomeServidor)
                    .addComponent(jTextFieldNomServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLautenticacao)
                    .addComponent(jComboBoxAutenticar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNomUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLnomUsuario))
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLsenha))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(82, 82, 82))
        );

        jPanelFuncao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBtConectar.setText("Conectar");
        jBtConectar.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtConectarActionPerformed(evt);
            }
        });
        jBtConectar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtConectarKeyPressed(evt);
            }
        });
        jPanelFuncao.add(jBtConectar);

        jBtSair.setText("Sair");
        jBtSair.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtSairActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtSair);

        jBtAjuda.setText("Ajuda");
        jBtAjuda.setPreferredSize(new java.awt.Dimension(100, 30));
        jBtAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAjudaActionPerformed(evt);
            }
        });
        jPanelFuncao.add(jBtAjuda);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelFuncao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addComponent(jPanelFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtConectarActionPerformed
        this.autenticar();
        if (con != null) {//existe uma conexao aberta
            //va para tela escolher bancos dinamicamente
            BasesDinamicas tdb = new BasesDinamicas(con);
            tdb.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jBtConectarActionPerformed

    private void jBtSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtSairActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja Sair Realmente ?");
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                //erro ao fechar a conexão 
                Logger.getLogger(Tela_Login_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jBtSairActionPerformed

    private void jBtAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtAjudaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtAjudaActionPerformed

    private void jBtCreditoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtCreditoMousePressed
    }//GEN-LAST:event_jBtCreditoMousePressed

    private void jTextFieldNomServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomServidorActionPerformed

    }//GEN-LAST:event_jTextFieldNomServidorActionPerformed

    private void jTextFieldNomServidorPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextFieldNomServidorPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomServidorPropertyChange

    private void jTextFieldNomUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomUsuarioActionPerformed

    private void jComboBoxAutenticarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAutenticarActionPerformed
        if (jComboBoxAutenticar.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Autenticação via Sql Server!");
            // Deixar campos habilitados entao enable recebe true;
            jTextFieldNomUsuario.setEnabled(true);
            jPasswordField1.setEnabled(true);
        }
        if (jComboBoxAutenticar.getSelectedIndex() == 1) {
            // Deixar campos desabilitados enato enable recebe false;
            JOptionPane.showMessageDialog(null, "Autenticação via Windows!");
            jTextFieldNomUsuario.setEnabled(false);
            jPasswordField1.setEnabled(false);
        }
    }//GEN-LAST:event_jComboBoxAutenticarActionPerformed

    private void jBtConectarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtConectarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.autenticar();
            if (con != null) {//existe uma conexao aberta
                //va para tela escolher bancos dinamicamente
                BasesDinamicas tdb = new BasesDinamicas(con);
                tdb.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jBtConectarKeyPressed

    private void jPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1KeyPressed
        //para entrar quando clicar enter no campo senha
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.autenticar();
            if (con != null) {//existe uma conexao aberta
                //va para tela escolher bancos dinamicamente
                BasesDinamicas tdb = new BasesDinamicas(con);
                tdb.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jPasswordField1KeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void jPanelPrincipalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanelPrincipalKeyPressed

    }//GEN-LAST:event_jPanelPrincipalKeyPressed

    private void jComboBoxAutenticarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxAutenticarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.autenticar();
            if (con != null) {//existe uma conexao aberta
                //va para tela escolher bancos dinamicamente
                BasesDinamicas tdb = new BasesDinamicas(con);
                tdb.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jComboBoxAutenticarKeyPressed

    private void jBtCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtCreditoActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Creditos().setVisible(true);
            }
        });
    }//GEN-LAST:event_jBtCreditoActionPerformed

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
            java.util.logging.Logger.getLogger(Tela_Login_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Login_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Login_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Login_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_Login_Principal().setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtAjuda;
    private javax.swing.JButton jBtConectar;
    private javax.swing.JButton jBtCredito;
    private javax.swing.JButton jBtSair;
    private javax.swing.JComboBox<String> jComboBoxAutenticar;
    private javax.swing.JLabel jLNomeServidor;
    private javax.swing.JLabel jLautenticacao;
    private javax.swing.JLabel jLnomUsuario;
    private javax.swing.JLabel jLsenha;
    private javax.swing.JLabel jLsimblo;
    private javax.swing.JPanel jPanelFuncao;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextFieldNomServidor;
    private javax.swing.JTextField jTextFieldNomUsuario;
    // End of variables declaration//GEN-END:variables
}
