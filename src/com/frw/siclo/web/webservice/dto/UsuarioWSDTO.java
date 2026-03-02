package com.frw.siclo.web.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="UsuarioWSDTO")
public class UsuarioWSDTO {

	private Long codigo;
	private String login;
	private String nome;
	private String password;
	
	public Long getCodigo() {
		return codigo;
	}
	public String getLogin() {
		return login;
	}
	public String getNome() {
		return nome;
	}
	public String getPassword() {
		return password;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
