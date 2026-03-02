package com.frw.base.dominio.sisdat;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.frw.base.dominio.base.EntidadeDominioBase;

/**
 * @author Leonardo Barros
 */
@Entity
@Table(name = "foto")
public class Foto extends EntidadeDominioBase<Foto> implements Comparable<Foto> {
	
	private static final long serialVersionUID = 1L;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto")
    private byte[] foto;

    @Column(name="nome")
    private String nome;
    
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "fk_pesquisa")
    private Pesquisa pesquisa;
    
    public Foto() {
    }
    
    @Override
	public int compareTo(Foto o) {
		return (nome != null ? nome : "").compareToIgnoreCase(o.getNome());
	}

	public byte[] getFoto() {
		return foto;
	}

	public String getNome() {
		return nome;
	}

	public Pesquisa getPesquisa() {
		return pesquisa;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPesquisa(Pesquisa pesquisa) {
		this.pesquisa = pesquisa;
	}

	@Override
	public String toString() {
		return id.toString();
	}
}