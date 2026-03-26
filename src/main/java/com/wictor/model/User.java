package com.wictor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_id")
    private Integer id;

    @Column(name = "User_cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "User_senha", nullable = false)
    private String senha;

    @Column(name = "User_nome", nullable = false)
    private String nome;

    @Column(name = "User_email1", nullable = false, unique = true)
    private String email1;

    @Column(name = "User_email2")
    private String email2;

    @Column(name = "User_tel1", nullable = false, unique = true)
    private String tel1;

    @Column(name = "User_tel2")
    private String tel2;

    @Column(name = "User_sexo", nullable = false, length = 1)
    private String sexo;

    @Column(name = "User_cep", nullable = false, length = 8)
    private String cep;

    @Column(name = "User_bairro", nullable = false)
    private String bairro;

    @Column(name = "User_rua", nullable = false)
    private String rua;

    @Column(name = "User_numcasa", nullable = false)
    private String num;

    @Column(name = "User_comp")
    private String comp;

    @Column(name = "User_foto")
    private String foto;

    @Column(name = "User_datanasc", nullable = false)
    private LocalDate datanasc;

    @Column(name = "User_ativo", nullable = false)
    private boolean ativo;
}