/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.coffeejava;

import apoio.ConexaoBD;
import javax.swing.JOptionPane;
import telas.Login;

/**
 *
 * @author Elias
 */
public class CoffeeJava {

    public static void main(String[] args) {
        if(ConexaoBD.getInstance().getConnection() != null) {
            new Login().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco de dados!");
        }
    }
}
