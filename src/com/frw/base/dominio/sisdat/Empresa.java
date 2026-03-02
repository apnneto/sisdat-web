package com.frw.base.dominio.sisdat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.frw.base.dominio.base.EntidadeDominioBase;

@Entity
@Table(name = "empresa")
public class Empresa extends EntidadeDominioBase<Empresa> implements Comparable<Empresa>{

	public static long PELLI_SISTEMAS_KEY = 1l;

	private static final long serialVersionUID = -200389875497933820L;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "cep")
	private String cep;

	@Column(name = "cidade")
	private String cidade;

	@Column(name = "cnpj")
	private String cnpj;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "crea")
	private String crea;

	@Column(name = "email")
	private String email;

	@Column(name = "logradouro")
	private String logradouro;

	@Column(name = "nome_fantasia")
	private String nomeFantasia;

	@Column(name = "numero")
	private String numero;

	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(name = "empresa_questionario", 
			  	joinColumns = @JoinColumn(name = "Empresa_id"), 
			  	inverseJoinColumns = @JoinColumn(name = "questionarios_id"))
	private List<Questionario> questionarios = new ArrayList<Questionario>();

	@Column(name = "razao_social")
	private String razaoSocial;

	@Column(name = "sigla_estado")
	private String siglaEstado;
	
	@Column(name = "telefone")
	private String telefone;

	@Override
	public int compareTo(Empresa o) {
		return (this.razaoSocial != null? this.razaoSocial.toLowerCase(): "").compareTo(o.getRazaoSocial() != null? o.getRazaoSocial().toLowerCase(): "");
	}

	public String getBairro() {
		return bairro;
	}

	public String getCep() {
		return cep;
	}

	public String getCidade() {
		return cidade;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getCrea() {
		return crea;
	}

	public String getEmail() {
		return email;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public String getNumero() {
		return numero;
	}

	public List<Questionario> getQuestionarios() {
		if(questionarios == null)
			questionarios = new ArrayList<Questionario>();
		return questionarios;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setCrea(String crea) {
		this.crea = crea;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setQuestionarios(List<Questionario> questionarios) {
		this.questionarios = questionarios;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Override
	public String toString() {
		return nomeFantasia;
	}
}
