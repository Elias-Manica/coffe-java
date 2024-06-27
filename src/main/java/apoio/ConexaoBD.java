/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apoio;

/**
 *
 * @author Elias
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBD {
    private static ConexaoBD instancia = null;
    private static Connection conexao = null;
    private static Properties prop;

    private ConexaoBD() {
        try {
            prop = new Properties();
            prop.load(new FileInputStream("db.properties"));

            String dbdriver = prop.getProperty("db.driver");
            String dburl = prop.getProperty("db.url");
            String dbuser = prop.getProperty("db.user");
            String dbsenha = prop.getProperty("db.senha");

            Class.forName(dbdriver);

            if (dbuser != null && !dbuser.isEmpty()) {
                conexao = DriverManager.getConnection(dburl, dbuser, dbsenha);
            } else {
                conexao = DriverManager.getConnection(dburl);
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Erro ao inicializar conexão com o banco de dados: " + e.getMessage());
        }
    }

    public static ConexaoBD getInstance() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }
        return instancia;
    }

    public static Connection getConnection() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar conexão com o banco de dados: " + e.getMessage());
        }
        return conexao;
    }

    private static void conectar() throws SQLException {
        try {
            String dbdriver = prop.getProperty("db.driver");
            String dburl = prop.getProperty("db.url");
            String dbuser = prop.getProperty("db.user");
            String dbsenha = prop.getProperty("db.senha");

            Class.forName(dbdriver);

            if (dbuser != null && !dbuser.isEmpty()) {
                conexao = DriverManager.getConnection(dburl, dbuser, dbsenha);
            } else {
                conexao = DriverManager.getConnection(dburl);
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erro ao reconectar com o banco de dados: " + e.getMessage());
            throw new SQLException("Erro ao reconectar com o banco de dados.", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public static void shutDown() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
            instancia = null;
            conexao = null;
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}
