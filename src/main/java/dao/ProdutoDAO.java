/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import apoio.ConexaoBD;
import apoio.IDAOT;
import entidades.Produto;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author vavum
 */

public class ProdutoDAO implements IDAOT<Produto>{

    @Override
   public String salvar(Produto p) {
    try {
        Statement st = ConexaoBD.getInstance().getConnection().createStatement();
        
        // verifica se o nome do produto já está cadastrado
        
        if (!consultar("nome = '" + p.getNome() + "'").isEmpty()) {
            return "Produto já cadastrado!";
        }
        String sql = "INSERT INTO produtos (id, nome, categoria, preco) " 
                   + "VALUES (default, '" + p.getNome() + "', '" + p.getCategoria() + "', " + p.getPreco() + ")";
        
        System.err.println("Sql: " + sql);
        
        int retorno = st.executeUpdate(sql);
       
        return null;
    } catch(Exception e) {
        System.out.println("Erro ao inserir o produto: " + e);
        return e.toString();
    }
}

    

    @Override
    public String atualizar(Produto o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String excluir(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Produto> consultarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Produto> consultar(String criterio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Produto consultarId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
