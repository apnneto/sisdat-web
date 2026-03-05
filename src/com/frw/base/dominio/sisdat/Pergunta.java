package com.frw.base.dominio.sisdat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.frw.base.dominio.base.EntidadeDominioBase;

@Entity
@Table(name = "pergunta")
public class Pergunta extends EntidadeDominioBase<Pergunta> implements  Comparable<Pergunta>{

	private static final long serialVersionUID = 9028831995300573403L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Column(name = "comentario")
	private String comentario;

	@ManyToOne
	@JoinColumn(name="dep_per_id") 
	private Pergunta dependenciaPerguntaId;
	
	@ManyToOne
	@JoinColumn(name="dep_res_id") 
	private Resposta dependenciaRespostaId;
	 
	@Column(name = "descricao")
	private String descricao;

	@Column(name = "descricao_resumida")
	private String descricaoResumida;

	@Column(name="multiplas_linhas")
	private Boolean multiplasLinhas;
	
	@Column(name = "obrigatoria")
	private Boolean obrigatoria;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "precisao_final")
	private Double precisaoFinal;

	@Column(name = "precisao_inicial")
	private Double precisaoInicial;

	@Column(name = "quadrante")
	private Integer quadrante;

	@ManyToOne
	@JoinColumn(name = "que_id")
	private Questionario questionario;

	@Column(name = "subdescricao")
	private String subdescricao;

	@ManyToOne
	@JoinColumn(name = "tipo")
	private TipoPergunta tipo;
	
	@ManyToOne
	@JoinColumn(name = "data_type")
	private TipoDado tipoDado;
	
	@ManyToOne
	@JoinColumn(name="lov_id")
	private TipoLov tipoLov;

	public Pergunta() {

	}

	public Pergunta(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Pergunta o) {
		return (this.descricao != null? this.descricao.toLowerCase(): "").compareTo(o.descricao != null? o.descricao.toLowerCase(): "");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pergunta other = (Pergunta) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getComentario() {
		return comentario;
	}

	public Pergunta getDependenciaPerguntaId() {
		return dependenciaPerguntaId;
	}

	public Resposta getDependenciaRespostaId() {
		return dependenciaRespostaId;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoResumida() {
		return descricaoResumida;
	}

	public Boolean getMultiplasLinhas() {
		return multiplasLinhas;
	}

	public Boolean getObrigatoria() {
		return obrigatoria;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Double getPrecisaoFinal() {
		return precisaoFinal;
	}

	public Double getPrecisaoInicial() {
		return precisaoInicial;
	}

	public Integer getQuadrante() {
		return quadrante;
	}

	public Questionario getQuestionario() {
		return questionario;
	}

	public String getSubdescricao() {
		return subdescricao;
	}

	public TipoPergunta getTipo() {
		return tipo;
	}

	public TipoDado getTipoDado() {
		return tipoDado;
	}

	public TipoLov getTipoLov() {
		return tipoLov;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Boolean isObrigatoria() {

		if (obrigatoria == null) {
			return false;
		}
		return obrigatoria;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setDependenciaPerguntaId(Pergunta dependenciaPerguntaId) {
		this.dependenciaPerguntaId = dependenciaPerguntaId;
	}

	public void setDependenciaRespostaId(Resposta dependenciaRespostaId) {
		this.dependenciaRespostaId = dependenciaRespostaId;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setDescricaoResumida(String descricaoResumida) {
		this.descricaoResumida = descricaoResumida;
	}

	public void setMultiplasLinhas(Boolean multiplasLinhas) {
		this.multiplasLinhas = multiplasLinhas;
	}

	public void setObrigatoria(Boolean obrigatoria) {
		this.obrigatoria = obrigatoria;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPrecisaoFinal(Double precisaoFinal) {
		this.precisaoFinal = precisaoFinal;
	}

	public void setPrecisaoInicial(Double precisaoInicial) {
		this.precisaoInicial = precisaoInicial;
	}

	public void setQuadrante(Integer quadrante) {
		this.quadrante = quadrante;
	}

	public void setQuestionario(Questionario questionario) {
		this.questionario = questionario;
	}

	public void setSubdescricao(String subdescricao) {
		this.subdescricao = subdescricao;
	}

	public void setTipo(TipoPergunta tipo) {
		this.tipo = tipo;
	}
	
	public void setTipoDado(TipoDado tipoDado) {
		this.tipoDado = tipoDado;
	}

	public void setTipoLov(TipoLov tipoLov) {
		this.tipoLov = tipoLov;
	}

	@Override
	public String toString() {
		return getDescricao();
	}

}
