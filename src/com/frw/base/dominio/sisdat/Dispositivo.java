package com.frw.base.dominio.sisdat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.frw.base.dominio.base.EntidadeDominioBase;
import com.frw.base.dominio.base.Usuario;

/**
 * @author Leonardo Barros
 * 
 */
@Entity
@Table(name = "dispositivo")
public class Dispositivo extends EntidadeDominioBase<Dispositivo> implements Comparable<Dispositivo>{

	private static final long serialVersionUID = 1L;

	@Column(name = "device")
	private String device;
	
	@ManyToOne
	@JoinColumn(name = "usu_id")
	private Usuario usuario;
	
	public Dispositivo() {
	}


	@Override
	public int compareTo(Dispositivo o) {
		return (this.device != null ? this.device : "").compareToIgnoreCase(o.getDevice());
	}

	public String getDevice() {
		return device;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return getDevice();
	}

}
