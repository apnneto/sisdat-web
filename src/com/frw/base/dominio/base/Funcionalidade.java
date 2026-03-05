package com.frw.base.dominio.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author juliano
 */
@Entity
@Table(name="funcionalidade")
@NamedQueries({
    @NamedQuery(name = "findFuncionalidadesByUsuarioAndModulo",
                query = "select distinct f from Usuario u inner join u.perfis as p inner join p.funcionalidades as f inner join f.modulo as m where u=:u and f.modulo=:m order by m.ordemExibicao, f.ordemExibicao"),
    @NamedQuery(name = "findFuncionalidadesByTipoUsuario",
                query = "select distinct f from Funcionalidade f join f.tiposUsuario tu where tu.id = :tipo order by f.modulo.nome, f.ordemExibicao"),
    @NamedQuery(name = "findAllFuncionalidades",
                query = "select distinct f from Funcionalidade f join f.modulo as m order by m.ordemExibicao, f.ordemExibicao"),
    @NamedQuery(name = "findFuncionalidadesByTipoUsuarioEModulo",
                query = "select distinct f from Funcionalidade f join f.tiposUsuario tu where tu.id = :tipo and f.modulo.id = :modulo order by f.modulo.nome, f.ordemExibicao")
})
public class Funcionalidade extends EntidadeBase implements Comparable<Funcionalidade> {

    private static final long serialVersionUID = 1L;

    @Column(name="descricao", nullable = false)
    @NotNull
    private String descricao;

    @ManyToOne
    @JoinColumn(name="fk_modulo")
    @NotNull
    private Modulo modulo;

    @Column(name="ordem_exibicao", nullable = false)
    @NotNull
    private Integer ordemExibicao;

    @ManyToMany(mappedBy="funcionalidades")
    private List<Perfil> perfis=new ArrayList<Perfil>();

    @ManyToMany
    @JoinTable(name="funcionalidade_tipo_usuario",
        joinColumns = @JoinColumn(name = "funcionalidades_id"),
        inverseJoinColumns = @JoinColumn(name = "tiposusuario_id"))
    private List<TipoUsuario> tiposUsuario=new ArrayList<TipoUsuario>();

    @Override
	public int compareTo(Funcionalidade o) {
		return (this.descricao != null? this.descricao.toLowerCase(): "").compareTo(o.descricao != null? o.descricao.toLowerCase(): "");
	}

    public String getDescricao() {
        return descricao;
    }


    public Modulo getModulo() {
        return modulo;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public List<TipoUsuario> getTiposUsuario() {
        return tiposUsuario;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    public void setTiposUsuario(List<TipoUsuario> tiposUsuario) {
        this.tiposUsuario = tiposUsuario;
    }

	@Override
    public String toString() {
        return descricao;
    }

}
