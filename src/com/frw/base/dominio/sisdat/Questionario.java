package com.frw.base.dominio.sisdat;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import com.frw.base.dominio.base.EntidadeDominioBase;

@Entity
@Table(name = "questionario")
@AdditionalCriteria(" this.excluido <> true ")
public class Questionario extends EntidadeDominioBase<Questionario> implements Comparable<Questionario>{

	private static final long serialVersionUID = -3726941419811426563L;

	@Column(name = "captura_cordenada")
	private Boolean capturaCordenada;
	
	@Column(name = "codigo")
	private String codigo;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "editavel")
	private Boolean editavel;

	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(name = "empresa_questionario", 
	  	joinColumns = @JoinColumn(name = "questionarios_id"), 
	  	inverseJoinColumns = @JoinColumn(name = "Empresa_id")
	)
	private List<Empresa> empresas = new ArrayList<Empresa>();

	@Column(name = "foto")
	private Boolean foto;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "orientacao")
	private String orientacao;
	
	
	@Column(name = "resumo")
	private String resumo;
	
	@Column(name = "versao")
	private String versao;


	public Questionario() {

	}

	public Questionario(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Questionario o) {
		return (this.descricao != null? this.descricao.toLowerCase(): "").compareTo(o.descricao != null? o.descricao.toLowerCase(): "");
	}

	public Boolean getCapturaCordenada() {
		return capturaCordenada;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Boolean getEditavel() {
		return editavel;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public Boolean getFoto() {
		return foto;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public String getOrientacao() {
		return orientacao;
	}

	public String getResumo() {
		return resumo;
	}

	public String getVersao() {
		return versao;
	}

	public void setCapturaCordenada(Boolean capturaCordenada) {
		this.capturaCordenada = capturaCordenada;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setEditavel(Boolean editavel) {
		this.editavel = editavel;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}
	
	public void setFoto(Boolean foto) {
		this.foto = foto;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
	
}
