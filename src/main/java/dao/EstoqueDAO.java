/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import apoio.ConexaoBD;
import apoio.IDAOT;
import entidades.Estoque;
import entidades.Produto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import telas.items;


/**
 *
 * @author vavum
 */
public class EstoqueDAO implements IDAOT<Estoque> {  
    
    public void popularTabela(JTable tabela) {
    ResultSet resultadoQ;

    // dados da tabela
    Object[][] dadosTabela = null;

    // cabecalho da tabela
    Object[] cabecalho = new Object[6];
    cabecalho[0] = "Produto ID";
    cabecalho[1] = "Estoque ID";
    cabecalho[2] = "Quantidade";
    cabecalho[3] = "Nome";
    cabecalho[4] = "Categoria";
    cabecalho[5] = "Preço";

    // cria matriz de acordo com nº de registros da tabela
    try {
        resultadoQ = ConexaoBD.getInstance().getConnection().createStatement().executeQuery(""
            + "SELECT count(*) "
            + "FROM estoque e "
            + "JOIN produtos p ON e.produto_id = p.id");

        resultadoQ.next();

        dadosTabela = new Object[resultadoQ.getInt(1)][6];

    } catch (Exception e) {
        System.out.println("Erro ao consultar estoque: " + e);
    }

    int lin = 0;

    // efetua consulta na tabela
    try {
        resultadoQ = ConexaoBD.getInstance().getConnection().createStatement().executeQuery(""
            + "SELECT e.produto_id, e.id AS estoque_id, e.quantidade, p.nome, p.categoria, p.preco "
            + "FROM estoque e "
            + "JOIN produtos p ON e.produto_id = p.id ORDER BY e.id");

        while (resultadoQ.next()) {
            dadosTabela[lin][0] = resultadoQ.getInt("produto_id");
            dadosTabela[lin][1] = resultadoQ.getInt("estoque_id");
            dadosTabela[lin][2] = resultadoQ.getInt("quantidade");
            dadosTabela[lin][3] = resultadoQ.getString("nome");
            dadosTabela[lin][4] = resultadoQ.getString("categoria");
            dadosTabela[lin][5] = resultadoQ.getDouble("preco");
            lin++;
        }
    } catch (Exception e) {
        System.out.println("Problemas para popular tabela...");
        System.out.println(e);
    }

    tabela.setModel(new DefaultTableModel(dadosTabela, cabecalho) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    });

    // permite selecao de apenas uma linha da tabela
    tabela.setSelectionMode(0);   
    
}

public String excluirProdutoEEstoque(int produtoId) {
    try {
        Statement st = ConexaoBD.getInstance().getConnection().createStatement();
        
        // Exclui o produto do estoque
        String sqlEstoque = "DELETE FROM estoque WHERE produto_id = " + produtoId;
        System.err.println("Sql Estoque: " + sqlEstoque);
        int retornoEstoque = st.executeUpdate(sqlEstoque);

        // Exclui o produto da tabela produtos
        String sqlProduto = "DELETE FROM produtos WHERE id = " + produtoId;
        System.err.println("Sql Produto: " + sqlProduto);
        int retornoProduto = st.executeUpdate(sqlProduto);
        
        return null;
    } catch(Exception ex) {
        System.out.println("Erro ao excluir o produto e estoque: " + ex);
        return ex.toString();
    }
}

    @Override
    public String salvar(Estoque o) {
        try {
        Statement st = ConexaoBD.getInstance().getConnection().createStatement();
        
        // Verifica se o produto já está no estoque
        
        
        String sql = "INSERT INTO estoque (produto_id, quantidade) " 
                   + "VALUES (" + o.getProduto_id()+ ", " + o.getQuantidade() + ")";
        
        System.err.println("Sql: " + sql);
        
        int retorno = st.executeUpdate(sql);
        
        return null;
    } catch(Exception e) {
        System.out.println("Erro ao inserir no estoque: " + e);
        return e.toString();
    }}

    @Override
    public String atualizar(Estoque o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public String salvarProdutoEAdicionarAoEstoque(Produto p, Estoque e) {
    try {
        Statement st = ConexaoBD.getInstance().getConnection().createStatement();
        
        System.out.println("teste 1");
        
        // Insere o produto na tabela produtos (omitindo a coluna id para permitir auto-incremento)
        String sqlProduto = "INSERT INTO produtos (nome, categoria, preco) " 
                          + "VALUES ('" + p.getNome() + "', '" + p.getCategoria() + "', " + p.getPreco() + ")";
        
        System.err.println("Sql: " + sqlProduto);
        
        int retornoProduto = st.executeUpdate(sqlProduto, Statement.RETURN_GENERATED_KEYS);
        
        // Obtém o ID gerado para o produto
        ResultSet rs = st.getGeneratedKeys();
        int produtoId = 0;
        if (rs.next()) {
            produtoId = rs.getInt(1);
        }
        
        // Insere o produto no estoque
        String sqlEstoque = "INSERT INTO estoque (produto_id, quantidade) " 
                          + "VALUES (" + produtoId + ", " + e.getQuantidade() + ")";
        
        System.err.println("Sql: " + sqlEstoque);
        
        int retornoEstoque = st.executeUpdate(sqlEstoque);
        
        return null;
    } catch(Exception ex) {
        System.out.println("Erro ao inserir o produto e adicionar ao estoque: " + ex);
        return ex.toString();
    }
}


    @Override
    public String excluir(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Estoque> consultarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Estoque> consultar(String criterio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Estoque consultarId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

    
