package com.frw.base.dominio.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Customizer;

import com.frw.base.dominio.jpa.EntidadeDominioBaseCustomizer;

/**
 *
 * @author juliano
 */
@Entity
@Table(name="modulo")
@NamedQueries({
    @NamedQuery(name = "findModulosByUsuario",
                query = "select distinct f.modulo from Usuario as u inner join u.perfis as  p inner join p.funcionalidades as f inner join f.modulo as m where u=:u order by m.ordemExibicao")
})
@Customizer(value=EntidadeDominioBaseCustomizer.class)
public class Modulo extends EntidadeBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy="modulo")
    private List<Funcionalidade> funcionalidades;

    private String nome;

    @Column(name="ordem_exibicao", nullable = false)
    @NotNull
    private Integer ordemExibicao;

    public List<Funcionalidade> getFuncionalidades() {
        return funcionalidades;
    }

    public String getNome() {
        return nome;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }
    

    public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    @Override
    public String toString() {
        return nome;
    }

}
