/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import apoio.ConexaoBD;
import apoio.IDAOT;
import entidades.ItensVendas;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Elias
 */
public class ItensVendasDAO implements IDAOT<ItensVendas> {

    @Override
    public String salvar(ItensVendas o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String atualizar(ItensVendas o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String excluir(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<ItensVendas> consultarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<ItensVendas> consultar(String criterio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ItensVendas consultarId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public void popularTabela(JTable tabela, int idVenda) {
    Object[][] dadosTabela = null;
    Object[] cabecalho = {"ID", "Produto", "Categoria", "Quantidade", "Preço Unitário"};

    try {
        Connection connection = ConexaoBD.getInstance().getConnection();
        Statement st = connection.createStatement();

        // Contar o número de itens na venda específica
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM itens_venda WHERE venda_id = " + idVenda);
        rs.next();
        dadosTabela = new Object[rs.getInt(1)][5];

        // Consultar os itens da venda com o JOIN para trazer os dados do produto
        rs = st.executeQuery("SELECT iv.id, p.nome AS produto, p.categoria, iv.quantidade, iv.preco_unitario "
                           + "FROM itens_venda iv "
                           + "JOIN produtos p ON iv.produto_id = p.id "
                           + "WHERE iv.venda_id = " + idVenda);

        int lin = 0;
        while (rs.next()) {
            dadosTabela[lin][0] = rs.getInt("id");
            dadosTabela[lin][1] = rs.getString("produto");
            dadosTabela[lin][2] = rs.getString("categoria");
            dadosTabela[lin][3] = rs.getInt("quantidade");
            dadosTabela[lin][4] = rs.getFloat("preco_unitario");
            lin++;
        }
    } catch (Exception e) {
        System.out.println("Erro ao consultar itens da venda: " + e);
    }

    tabela.setModel(new DefaultTableModel(dadosTabela, cabecalho) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    });
    }

}
