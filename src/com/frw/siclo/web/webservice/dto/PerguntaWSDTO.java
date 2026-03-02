package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PerguntaWSDTO")
public class PerguntaWSDTO {

	private Long codigo;
	private Long codigoQuestionario;
	private String comentario;
	private Long dataType;
	private String descricao;
	private String descricaoResumida;
	private Boolean obrigatoria;
	private Integer ordem;
	private Double precisaoFinal;
	private Double precisaoInicial;
	private String subDescricao;
	private Long tipo;

	public Long getCodigo() {
		return codigo;
	}

	public Long getCodigoQuestionario() {
		return codigoQuestionario;
	}

	public String getComentario() {
		return comentario;
	}

	public Long getDataType() {
		return dataType;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoResumida() {
		return descricaoResumida;
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


	public String getSubDescricao() {
		return subDescricao;
	}

	public Long getTipo() {
		return tipo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setCodigoQuestionario(Long codigoQuestionario) {
		this.codigoQuestionario = codigoQuestionario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setDescricaoResumida(String descricaoResumida) {
		this.descricaoResumida = descricaoResumida;
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

	public void setSubDescricao(String subDescricao) {
		this.subDescricao = subDescricao;
	}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}

}
