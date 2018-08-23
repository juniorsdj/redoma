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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConnectionFactory {

    private static Connection connection;
    private static String nomeServidor;
    private static String usuario;
    private static String senha;
    private static String database;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        ConnectionFactory.connection = connection;
    }

    public static String getNomeServidor() {
        return nomeServidor;
    }

    public static void setNomeServidor(String nomeServidor) {
        ConnectionFactory.nomeServidor = nomeServidor;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        ConnectionFactory.usuario = usuario;
    }

    public static String getSenha() {
        return senha;
    }

    public static void setSenha(String senha) {
        ConnectionFactory.senha = senha;
    }

    public static String getDatabase() {
        return database;
    }

    public static void setDatabase(String database) {
        ConnectionFactory.database = database;
    }
    
    
    public Connection getConnection(String servidor, String user, String  password) {
        nomeServidor = servidor;
        usuario = user;
        senha = password;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro de comunicação com o servidor de banco de dados "
                    + "\n porque esta faltando um driver");
        }
        String url = "jdbc:sqlserver://" + nomeServidor + ";user=" + usuario + ";password=" + senha;
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
    
    public static Connection getConnection(String servidor, String user, String password, String base) {
        nomeServidor = servidor;
        usuario = user;
        senha = password;
        database = base;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro de comunicação com o servidor de banco de dados "
                    + "\n porque esta faltando um driver");
        }
        String url = "jdbc:sqlserver://" + nomeServidor + ";user=" + usuario + ";password=" + senha + ";databaseName="+database;
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
    
    public static void fecharStmtERs(PreparedStatement stmt, ResultSet rs){
        //primeiro -- verificar se con esta aberto ou nao
        //se for null e pq ta fechada
        if (stmt != null && rs !=null) {
            try {
                //fechar o statement
                stmt.close();
                rs.close();
            } catch (SQLException ex) {
                //printar no console em vermelho err
                System.err.println("Captura do Erro" + ex);
            }
        }
    }

    public static void close() {
        try {
            System.out.println("Fechando a conexão!");
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
