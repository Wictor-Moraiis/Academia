package com.wictor.dao;

import com.wictor.Security.PasswordService;
import com.wictor.model.User;
import com.wictor.util.Banco;

import java.sql.*;
import java.time.LocalDate;

public class UserDao {

    public int insert(User user) {

        int idGerado = 0;
        LocalDate data = user.getDatanasc();
        Date date = Date.valueOf(data);

        try (final PreparedStatement stmt = Banco.getConnection().prepareStatement(
                "INSERT INTO User(User_cpf, User_senha,User_nome, User_email1, User_email2, User_tel1, User_tel2, User_sexo, User_cep, User_bairro, User_rua, User_numcasa, User_comp, User_datanasc, User_ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getCpf());
            stmt.setString(2, user.getSenha());
            stmt.setString(3, user.getNome());
            stmt.setString(4, user.getEmail1());
            stmt.setString(5, user.getEmail2());
            stmt.setString(6, user.getTel1());
            stmt.setString(7, user.getTel2());
            stmt.setString(8, user.getSexo());
            stmt.setString(9, user.getCep());
            stmt.setString(10, user.getBairro());
            stmt.setString(11, user.getRua());
            stmt.setString(12, user.getNum());
            stmt.setString(13, user.getComp());
            stmt.setDate(14, date);
            stmt.setBoolean(15, user.getAtivo());

            stmt.executeUpdate();

            ResultSet id = stmt.getGeneratedKeys();
            if (id.next()) {
                idGerado = id.getInt(1);
            }
            return idGerado;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean existeUser() {

        try (PreparedStatement stmt = Banco.getConnection().prepareStatement(
                "SELECT 1 FROM User LIMIT 1");
                ResultSet rs = stmt.executeQuery()) {
//
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User autenticar(String cpfCript, String senhaDigitada) {

        try (PreparedStatement stmt = Banco.getConnection().prepareStatement(
                "SELECT * FROM User WHERE User_cpf = ?")) {

            stmt.setString(1, cpfCript);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String hashDoBanco = rs.getString("User_senha");

                    if (PasswordService.verificarSenha(senhaDigitada, hashDoBanco)) {

                        User user = new User();
                        user.setId(rs.getInt("User_id"));
                        user.setCpf(rs.getString("User_cpf"));
                        user.setNome(rs.getString("User_nome"));
                        user.setEmail1(rs.getString("User_email1"));

                        return user;

                    } else {

                        return null;
                    }

                } else {

                    return null;
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

