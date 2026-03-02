/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.dominio.base;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 *
 * @author Marcelo Alves
 */
@MappedSuperclass
public abstract class EntidadeDominioBaseVersionada extends EntidadeDominioBase {

	private static final long serialVersionUID = 1L;

	@Version
    protected Long versao;

}
