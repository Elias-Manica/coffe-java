/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Elias
 */

import apoio.ConexaoBD;
import apoio.IDAOT;
import entidades.Usuario;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioDAO implements IDAOT<Usuario> {

    @Override
    public String salvar(Usuario o) {
        try {
            Statement st = ConexaoBD.getInstance().getConnection().createStatement();
            
            // Verifica se o email já está cadastrado
            if (!consultar("email = '" + o.getEmail() + "'").isEmpty()) {
                return "Email já cadastrado!";
            }
            
            String sql = "insert into usuarios " + "values" + "(default, " + "'" + o.getEmail()+ "', " + "md5 ('" + o.getSenha() + "'))";
            
            System.err.println("Sql: " + sql);
            
            int retorno = st.executeUpdate(sql);
            
            return null;
        } catch(Exception e) {
            System.out.println("Erro ao inserir o cliente" + e);
            return e.toString();
        }
    }

    @Override
    public String atualizar(Usuario o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String excluir(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Usuario> consultarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  // Função para consultar usuários no banco de dados
    @Override
    public ArrayList<Usuario> consultar(String criterio) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            Statement st = ConexaoBD.getInstance().getConnection().createStatement();
            String sql = "SELECT * FROM usuarios WHERE " + criterio;
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                usuarios.add(u);
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar o usuário: " + e);
        }
        return usuarios;
    }

    @Override
    public Usuario consultarId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean autenticar(Usuario u) {

        try {
            String sql
                    = "SELECT email, senha "
                    + "FROM usuarios "
                    + "WHERE "
                    + "email = '" + u.getEmail()+ "'  and "
                    + "senha = md5 ('" + u.getSenha() + "')";

            System.out.println("SQL: " + sql);

            ResultSet resultadoQ = ConexaoBD.getInstance().getConnection().createStatement().executeQuery(sql);

            if (resultadoQ.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Erro ao autenticar usuário: " + e);
            return false;
        }
    }

}

