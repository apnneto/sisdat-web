package com.frw.base.dominio.sisdat;

import javax.persistence.MappedSuperclass;

import com.frw.base.dominio.base.EntidadeDominioBase;

@MappedSuperclass
public abstract class AbstractColeta<T extends EntidadeDominioBase> extends EntidadeDominioBase<T> {
	
	private static final long serialVersionUID = 1L;
	
}
