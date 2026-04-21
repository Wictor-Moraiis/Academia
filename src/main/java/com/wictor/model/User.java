package com.wictor.model;

import com.wictor.enums.Role;
import com.wictor.enums.Sexo;
import com.wictor.enums.Tipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }
    @Override
    public String getPassword() {
        return this.senha;
    }
    @Override
    public String getUsername() {
        return this.cpf;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }

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

    @Enumerated(EnumType.STRING)
    @Column(name = "User_sexo", nullable = false)
    private Sexo sexo;

    @Column(name = "User_cep", nullable = false, length = 8)
    private String cep;

    @Column(name = "User_bairro", nullable = false)
    private String bairro;

    @Column(name = "User_rua", nullable = false)
    private String rua;

    @Column(name = "User_numcasa", nullable = false)
    private Integer numeroCasa;

    @Column(name = "User_comp")
    private String comp;

    @Column(name = "User_foto")
    private String foto;

    @Column(name = "User_datanasc", nullable = false)
    private LocalDate datanasc;

    @Enumerated(EnumType.STRING)
    @Column(name = "User_rule", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "User_tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "User_ativo", nullable = false)
    private boolean ativo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Aluno aluno;
}