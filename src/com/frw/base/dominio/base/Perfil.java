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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Customizer;

import com.frw.base.dominio.jpa.EntidadeDominioBaseCustomizer;

/**
 * @author juliano
 */
@Entity
@Table(name="perfil")
@NamedQueries({
    @NamedQuery(name = "findPerfilByTipoUsuario",
                query = "select p from Perfil p where p.tipoUsuario=:tipo and p.excluido<>true"),
    @NamedQuery(name = "findPerfisUsuarioMaster",
                query = "select p from Perfil p where p.tipoUsuario.id not in (:administrador, :master) and p.excluido<>true")
})
@Customizer(value=EntidadeDominioBaseCustomizer.class)
public class Perfil extends EntidadeDominioBase<Perfil>  implements Comparable<Perfil>{

    public static final Long ADM = 1l;
    
    public static final Long PESQUISADOR = 2l;
    private static final long serialVersionUID = 1L;


    @ManyToMany
    @JoinTable(name="perfil_funcionalidade")
    private List<Funcionalidade> funcionalidades=new ArrayList<Funcionalidade>();

    @Column(name="Nome", nullable = false)
    @NotNull
    private String nome;

    @Transient
    private String perfilComparable;

    @ManyToOne
    @JoinColumn(name="fk_tipo_usuario")
    @NotNull
    private TipoUsuario tipoUsuario;
    
    @ManyToMany(mappedBy = "perfis")
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    @Override
	public int compareTo(Perfil o) {
		return (this.nome != null? this.nome.toLowerCase(): "").compareTo(o.nome != null? o.nome.toLowerCase(): "");
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Perfil other = (Perfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    public List<Funcionalidade> getFuncionalidades() {
        return funcionalidades;
    }

    public String getNome() {
        return nome;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

    public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

	public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

	@Override
    public String toString() {
       return nome;
    }

	

}
