package com.frw.base.dominio.sisdat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import com.frw.base.dominio.base.EntidadeDominioBase;
import com.frw.base.dominio.base.Usuario;

@Entity
@Table(name = "pesquisa")
public class Pesquisa extends EntidadeDominioBase<Pesquisa> implements Comparable<Pesquisa> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "pesquisa", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<ColetaPesquisa> coletasPesquisa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_abertura")
	private Date dataAbertura;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fechamento")
	private Date dataFechamento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_sincronizacao")
	private Date dataSincronizacao;

	@Column(name = "device")
	private String device;

	@Column(name = "fechamento")
	private String fechamento;

	@OneToMany(mappedBy = "pesquisa", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Foto> fotos;

	@Column(name = "latitude_final")
	private Double latitudeFinal;

	@Column(name = "latitude_inicial")
	private Double latitudeInicial;

	@Column(name = "longitude_final")
	private Double longitudeFinal;

	@Column(name = "longitude_inicial")
	private Double longitudeInicial;

	@Column(name = "numero")
	private String numero;

	@Column(name = "observacao")
	private String observacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "que_id")
	private Questionario questionario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usu_id")
	private Usuario usuario;

	@Column(name = "visivel")
    private Boolean visivel = true;

	public Pesquisa() {
	}

	public Pesquisa(long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Pesquisa o) {
		return (this.questionario != null ? this.questionario.compareTo(o.getQuestionario()) : -1);
	}

	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	public Set<ColetaPesquisa> getColetasPesquisa() {
		if(coletasPesquisa == null)
			coletasPesquisa = new HashSet<ColetaPesquisa>();
		return coletasPesquisa;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public Date getDataSincronizacao() {
		return dataSincronizacao;
	}

	public String getDevice() {
		return device;
	}

	public String getFechamento() {
		return fechamento;
	}

	public List<Foto> getFotos() {
		if(fotos == null)
			fotos = new ArrayList<Foto>();
		return fotos;
	}

	public Double getLatitudeFinal() {
		return latitudeFinal;
	}

	public Double getLatitudeInicial() {
		return latitudeInicial;
	}

	public Double getLongitudeFinal() {
		return longitudeFinal;
	}

	public Double getLongitudeInicial() {
		return longitudeInicial;
	}

	@Transient
	public String getNomeFotos() {
		String nomesFotos = "";
		Foto f;
		List<Foto> list = getFotos();
		
		if (list == null) {
			nomesFotos = "-";
		}else {
			
			for (int i = 0; i < list.size(); i++) {
				f = list.get(i);
				nomesFotos += f.getNome();
				
				if (i < list.size()-1) {
					nomesFotos += ", ";
				}
			}
		}
		
		return nomesFotos;
	}

	public String getNumero() {
		return numero;
	}

	public String getObservacao() {
		return observacao;
	}

	public Questionario getQuestionario() {
		return questionario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Boolean getVisivel() {
		return visivel;
	}

	public void setColetasPesquisa(Set<ColetaPesquisa> coletasPesquisa) {
		this.coletasPesquisa = coletasPesquisa;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public void setDataSincronizacao(Date dataSincronizacao) {
		this.dataSincronizacao = dataSincronizacao;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public void setFechamento(String fechamento) {
		this.fechamento = fechamento;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}

	public void setLatitudeFinal(Double latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	public void setLatitudeInicial(Double latitudeInicial) {
		this.latitudeInicial = latitudeInicial;
	}

	public void setLongitudeFinal(Double longitudeFinal) {
		this.longitudeFinal = longitudeFinal;
	}

	public void setLongitudeInicial(Double longitudeInicial) {
		this.longitudeInicial = longitudeInicial;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public void setQuestionario(Questionario questionario) {
		this.questionario = questionario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getId());
	}

}
