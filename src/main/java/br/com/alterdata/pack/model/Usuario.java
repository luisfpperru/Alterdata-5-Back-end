package br.com.alterdata.pack.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "generator_usuario", sequenceName = "sequence_usuario", initialValue = 1, allocationSize = 1)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator_usuario")
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = true)
    private String status;

    @ManyToOne()
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne()
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    public Usuario() {}
    
    public Usuario(String login, String senha, String avatar, String nome, String status, Cargo cargo, Equipe equipe) {
        this.login = login;
        this.senha = senha;
        this.avatar = avatar;
        this.nome = nome;
        this.status = status;
        this.cargo = cargo;
        this.equipe = equipe;
    }

    public Usuario(Long id, String login, String senha, String avatar, String nome, String status, Cargo cargo,
            Equipe equipe) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.avatar = avatar;
        this.nome = nome;
        this.status = status;
        this.cargo = cargo;
        this.equipe = equipe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

}
