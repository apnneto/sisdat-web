package com.frw.base.dominio.base;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


/**
 * @author Carlos Santos
 */
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario extends EntidadeBase implements Comparable<TipoUsuario> {

    public static final long ADM = 1l;
    
    public static final long PESQUISADOR = 2l; 
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "Descricao", nullable = false)
    private String descricao;

    @ManyToMany(mappedBy = "tiposUsuario")
    private List<Funcionalidade> funcionalidades = new ArrayList<Funcionalidade>();

    public TipoUsuario() {
    }

    public TipoUsuario(Integer id) {
        this.setId(id.longValue());
    }

    public TipoUsuario(long id) {
        this.setId(id);
    }

    @Override
	public int compareTo(TipoUsuario o) {
		return (this.descricao != null? this.descricao.toLowerCase(): "").compareTo(o.descricao != null? o.descricao.toLowerCase(): "");
	}
    public String getDescricao() {
        return descricao;
    }

    public List<Funcionalidade> getFuncionalidades() {
        return funcionalidades;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

	@Override
    public String toString() {
        return descricao;
    }
}
