package com.wictor.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Banco {

    private static final String URL =
            "jdbc:mysql://localhost:3306/academia?useSSL=false&serverTimezone=UTC";

    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private static Connection connection;

    private Banco() {}

    public static Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com MySQL realizada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco:");
            e.printStackTrace();
        }

        return connection;
    }
}