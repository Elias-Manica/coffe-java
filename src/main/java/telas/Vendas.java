package telas;

import apoio.ConexaoBD;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dmate
 */
public class Vendas extends javax.swing.JFrame {

    ImageIcon iconBack = new ImageIcon("icon-volta.png");

    public Vendas() {
        initComponents();
        setIconImage(iconVolta, iconBack);
        carregarProdutos();

        inputProduto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String produtoSelecionado = (String) inputProduto.getSelectedItem();
                    carregarValorProduto(produtoSelecionado);
                }
            }
        });
    }

    private void setIconImage(javax.swing.JLabel label, ImageIcon icon) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
        label.setText("");
    }

    private void carregarProdutos() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "SELECT nome FROM produtos";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            inputProduto.removeAllItems();
            inputProduto.addItem("Selecione:");

            while (rs.next()) {
                String nomeProduto = rs.getString("nome");
                inputProduto.addItem(nomeProduto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void carregarValorProduto(String nomeProduto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "SELECT preco FROM produtos WHERE nome = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nomeProduto);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                double precoProduto = rs.getDouble("preco");
                inputValor.setText("R$: " + String.valueOf(precoProduto));
            } else {
                inputValor.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            inputValor.setText("Erro ao obter preço do produto");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCodigoProduto(String nomeProduto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "SELECT id FROM produtos WHERE nome = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nomeProduto);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Produto não encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; 
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void atualizarCampoDeTexto(String produtoSelecionado) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "SELECT preco FROM produtos WHERE nome = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, produtoSelecionado); 

            rs = pstmt.executeQuery();

            if (rs.next()) {
                double valorProduto = rs.getDouble("preco");
                inputValor.setText("R$: " + String.valueOf(valorProduto));
            } else {
                inputValor.setText("Valor não encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            inputValor.setText("Erro ao obter valor do produto");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verificarEstoque(int codigoProduto, int quantidade) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "SELECT quantidade FROM estoque WHERE produto_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codigoProduto); 

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int quantidadeProduto = rs.getInt("quantidade");
                return quantidadeProduto >= quantidade;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean removerDoEstoque(int codigoProduto, int quantidade) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "UPDATE estoque SET quantidade = quantidade - ? WHERE produto_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantidade);
            pstmt.setInt(2, codigoProduto);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void adicionarCarrinho() {
        if (inputProduto.getSelectedItem().equals("Selecione:")) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String produtoSelecionado = (String) inputProduto.getSelectedItem();

        int codigoProduto = getCodigoProduto(produtoSelecionado);

        String quantidadeStr = String.valueOf(inputQtde.getValue());
        int quantidade = Integer.parseInt(quantidadeStr);

        String valorStr = inputValor.getText().replace("R$: ", "");
        double valor = Double.parseDouble(valorStr);

        double subtotal = quantidade * valor;

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tableCarrinho.getModel();

        if (verificarEstoque(codigoProduto, quantidade)) {
            System.out.println("CODIGO PRODUTO: " + codigoProduto);
            model.addRow(new Object[]{codigoProduto, produtoSelecionado, quantidade, "R$: " + valor, "R$: " + subtotal});
            removerDoEstoque(codigoProduto, quantidade);
            JOptionPane.showMessageDialog(null, "Item adicionado no Carrinho");
        } else {
            JOptionPane.showMessageDialog(this, "Quantidade não disponível no estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        inputProduto.setSelectedIndex(0);
        inputQtde.setValue(1);
        inputValor.setText("");
    }

    public class ItemVenda {

        private int produtoId;
        private int quantidade;
        private double precoUnitario;

        public ItemVenda(int produtoId, int quantidade, double precoUnitario) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
        }

        public int getProdutoId() {
            return produtoId;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getPrecoUnitario() {
            return precoUnitario;
        }
    }

    public int criarVenda(double total) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int vendaId = -1;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "INSERT INTO vendas (total) VALUES (?) RETURNING id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, total);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                vendaId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vendaId;
    }

    public boolean inserirItemVenda(int vendaId, int produtoId, int quantidade, double precoUnitario) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBD.getInstance().getConnection();

            String sql = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, vendaId);
            pstmt.setInt(2, produtoId);
            pstmt.setInt(3, quantidade);
            pstmt.setDouble(4, precoUnitario);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    ConexaoBD.getInstance().closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labelProduto = new javax.swing.JLabel();
        labelValor = new javax.swing.JLabel();
        labelVendedor = new javax.swing.JLabel();
        inputValor = new javax.swing.JTextField();
        inputVendedor = new javax.swing.JTextField();
        labelQtde = new javax.swing.JLabel();
        buttonAdicionar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCarrinho = new javax.swing.JTable();
        buttonFinalizar = new javax.swing.JButton();
        iconVolta = new javax.swing.JLabel();
        inputProduto = new javax.swing.JComboBox<>();
        inputQtde = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 204, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 0));
        jLabel1.setText("Vendas");

        labelProduto.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelProduto.setForeground(new java.awt.Color(255, 102, 0));
        labelProduto.setText("Produto");

        labelValor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelValor.setForeground(new java.awt.Color(255, 102, 0));
        labelValor.setText("Valor");

        labelVendedor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelVendedor.setForeground(new java.awt.Color(255, 102, 0));
        labelVendedor.setText("Vendedor");

        inputValor.setEditable(false);
        inputValor.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        inputValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputValorActionPerformed(evt);
            }
        });
        inputValor.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                inputValorVetoableChange(evt);
            }
        });

        inputVendedor.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        inputVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputVendedorActionPerformed(evt);
            }
        });

        labelQtde.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelQtde.setForeground(new java.awt.Color(255, 102, 0));
        labelQtde.setText("Quantidade");

        buttonAdicionar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        buttonAdicionar.setForeground(new java.awt.Color(204, 102, 0));
        buttonAdicionar.setText("Adicionar");
        buttonAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAdicionarActionPerformed(evt);
            }
        });

        tableCarrinho.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        tableCarrinho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Produto", "Quantidade", "Valor", "Subtotal"
            }
        ));
        tableCarrinho.setGridColor(new java.awt.Color(0, 0, 0));
        tableCarrinho.setRowHeight(25);
        jScrollPane1.setViewportView(tableCarrinho);

        buttonFinalizar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        buttonFinalizar.setForeground(new java.awt.Color(204, 102, 0));
        buttonFinalizar.setText("Finalizar");
        buttonFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFinalizarActionPerformed(evt);
            }
        });

        iconVolta.setText("iconVolta");
        iconVolta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconVoltaMouseClicked(evt);
            }
        });

        inputQtde.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        inputQtde.setValue(1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(buttonAdicionar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelProduto)
                            .addComponent(inputValor, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                            .addComponent(labelValor)
                            .addComponent(iconVolta)
                            .addComponent(inputProduto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelQtde)
                            .addComponent(inputVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelVendedor)
                            .addComponent(inputQtde, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonFinalizar)
                        .addGap(192, 192, 192))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(404, 404, 404))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(iconVolta))
                .addGap(68, 68, 68)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelVendedor)
                            .addComponent(labelProduto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(inputProduto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelValor)
                            .addComponent(labelQtde))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputValor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputQtde, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addComponent(buttonAdicionar)
                        .addGap(112, 112, 112)))
                .addGap(39, 39, 39)
                .addComponent(buttonFinalizar)
                .addGap(91, 91, 91))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonFinalizarActionPerformed

        DefaultTableModel model = (DefaultTableModel) tableCarrinho.getModel();
        int rowCount = model.getRowCount();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "O carrinho está vazio.");
            return;
        }

        double total = 0.0;
        ArrayList<ItemVenda> itensVenda = new ArrayList<>();


        // Calcular o total e criar a lista de itens
        for (int i = 0; i < rowCount; i++) {
            String produtoNome = (String) model.getValueAt(i, 1);
            double preco = Double.parseDouble(((String) model.getValueAt(i, 3)).replace("R$: ", "").replace(",", "."));
            int quantidade = (int) model.getValueAt(i, 2);

            total += preco * quantidade;

            int produtoId = getCodigoProduto(produtoNome);
            itensVenda.add(new ItemVenda(produtoId, quantidade, preco));
        }

        // Criar a venda
        int vendaId = criarVenda(total);
        if (vendaId == -1) {
            JOptionPane.showMessageDialog(null, "Erro ao criar a venda.");
            return;
        }

        // Inserir os itens da venda
        boolean sucesso = true;
        for (ItemVenda item : itensVenda) {
            if (!inserirItemVenda(vendaId, item.getProdutoId(), item.getQuantidade(), item.getPrecoUnitario())) {
                sucesso = false;
                break;
            }

            // Remover do estoque
            //if (!removerDoEstoque(item.getProdutoId(), item.getQuantidade())) {;;;;
            //    sucesso = false;
            //    break;
            //}
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(null, "Venda realizada com sucesso!");
            model.setRowCount(0); // Limpar o carrinho
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao processar a venda.");
        }


    }//GEN-LAST:event_buttonFinalizarActionPerformed

    private void inputVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputVendedorActionPerformed

    private void inputValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputValorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputValorActionPerformed

    private void iconVoltaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconVoltaMouseClicked
        new FramePrincipal().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_iconVoltaMouseClicked

    private void inputValorVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_inputValorVetoableChange
        inputProduto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String produtoSelecionado = (String) inputProduto.getSelectedItem();
                    System.out.println("Produto selecionado: " + produtoSelecionado);
                    atualizarCampoDeTexto(produtoSelecionado);
                }
            }
        });
    }//GEN-LAST:event_inputValorVetoableChange

    private void buttonAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAdicionarActionPerformed
        adicionarCarrinho();
    }//GEN-LAST:event_buttonAdicionarActionPerformed

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
            java.util.logging.Logger.getLogger(Vendas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vendas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vendas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vendas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vendas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdicionar;
    private javax.swing.JButton buttonFinalizar;
    private javax.swing.JLabel iconVolta;
    private javax.swing.JComboBox<String> inputProduto;
    private javax.swing.JSpinner inputQtde;
    private javax.swing.JTextField inputValor;
    private javax.swing.JTextField inputVendedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelProduto;
    private javax.swing.JLabel labelQtde;
    private javax.swing.JLabel labelValor;
    private javax.swing.JLabel labelVendedor;
    private javax.swing.JTable tableCarrinho;
    // End of variables declaration//GEN-END:variables
}
