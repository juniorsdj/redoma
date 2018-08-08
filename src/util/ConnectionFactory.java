/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Valmir Andrade; esta classe visa eliminar da tela principal de login a
 * logica utilizada para fazer login na aplicação deixando a tela mais limpa e
 * focada nos componetes de visualização do usuario.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConnectionFactory {

    private Connection connection;
    private String nomeServidor;
    private String usuario;
    private String senha;

    public Connection getConnection(String servidor, String usuario, String senha) {
        this.nomeServidor = servidor;
        this.usuario = usuario;
        this.senha = senha;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro de comunicação com o servidor de banco de dados "
                    + "\n porque esta faltando um driver");
        }
        String url = "jdbc:sqlserver://" + this.nomeServidor + ";user=" + this.usuario + ";password=" + this.senha + ";";
        try {
            connection = DriverManager.getConnection(url);
            System.out.println(connection);
        } // Erro caso o driver JDBC não foi instalado
        catch (SQLException e) {
            // Erro caso haja problemas para se conectar ao banco de dados
            //   JOptionPane.showMessageDialog(null, "Ocorreu um erro ao tentar conectar-se ao servidor verifique se o nome do servidor "
            //         + "\n e os dados de usuario e senha estão corretos.");

            e.printStackTrace();
        }
        return connection;
    }

    public void Close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
