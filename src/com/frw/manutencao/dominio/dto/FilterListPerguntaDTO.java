/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.manutencao.dominio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.web.pages.seguranca.filter.dto.AbstractFilterDTO;

/**
 * @author Miller Leonardo
 */
public class FilterListPerguntaDTO extends AbstractFilterDTO implements Serializable {

private static final long serialVersionUID = -8401351858652944804L;
	
    private List<Questionario> questionarios = new ArrayList<Questionario>();
	
	public List<Questionario> getQuestionarios() {
		return questionarios;
	}
	public void setQuestionarios(List<Questionario> questionarios) {
		this.questionarios = questionarios;
	}
	
	public String toCriterioString() {
		StringBuilder sb = new StringBuilder();
		getCriterioQuestionarios(sb);
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		return toCriterioString();
	}

	private void getCriterioQuestionarios(StringBuilder sb) {
		Iterator<Questionario> it = questionarios.iterator();
		Questionario q;
		it = questionarios.iterator();
		while (it.hasNext()) {
			q = it.next();
			sb.append("Provas: ");
			sb.append(q.getDescricao());
			if (it.hasNext()) {
				sb.append(", ");
			}else {
				sb.append(".");
			}
		}
		
	}//END
}
