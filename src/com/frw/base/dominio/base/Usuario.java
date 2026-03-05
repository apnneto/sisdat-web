package com.frw.base.dominio.base;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import org.eclipse.persistence.annotations.Customizer;

import com.frw.base.dominio.jpa.EntidadeDominioBaseCustomizer;
import com.frw.base.dominio.sisdat.Empresa;

/**
 * @author Juliano
 * @author Carlos Santos
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
		@NamedQuery(name = "findAllUsuarios", query = "select u from Usuario u where u.excluido <> true"),
		@NamedQuery(name = "findUsuarioByLoginESenha", query = "select u from Usuario u where upper(u.login) = upper(:login) and u.senha=:password and u.ativo=true and u.excluido<>true"),
		@NamedQuery(name = "findUsuarioByLogin", query = "select u from Usuario u where u.login=:login and u.excluido=false"),
		@NamedQuery(name = "findUsuarioByCpf", query = "select u from Usuario u where u.cpf=:cpf and u.excluido=false") })
@Customizer(value = EntidadeDominioBaseCustomizer.class)
public class Usuario extends EntidadeDominioBase<Usuario> implements Comparable<Usuario>{

	private static final long serialVersionUID = -3602914917515474431L;

	@Column(name = "ativo")
	private Boolean ativo = true;

	@Column(name = "cpf")
	private String cpf;

	@ManyToOne
	@JoinColumn(name = "fk_empresa")
	private Empresa empresa;

	@Column(name = "login")
	private String login;

	@Column(name = "nome")
	private String nome;

	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(name = "usuario_perfil")
	private List<Perfil> perfis = new ArrayList<Perfil>();

	@Column(name = "password")
	private String senha;

	@ManyToOne(optional = false)
	@JoinColumn(name = "fk_tipo_usuario")
	private TipoUsuario tipoUsuario;

	public Usuario() {
	}

	public Usuario(Long id) {
		this.id = id;
	}

	public Usuario(String login) {
		this.login = login;
	}

	@Override
	public int compareTo(Usuario o) {
		return (this.nome != null? this.nome.toLowerCase(): "").compareTo(o.nome != null? o.nome.toLowerCase(): "");
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public String getCpf() {
		return cpf;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public String getLogin() {
		return login;
	}

	public String getNome() {
		return nome;
	}

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public String getSenha() {
		return senha;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@Override
	public String toString() {
		return login;
	}

}
