package com.frw.base.negocio.quiz;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.sisdat.ColetaPesquisaDAO;
import com.frw.base.dao.sisdat.FotoDAO;
import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Foto;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;

@Stateless
public class ColetaPesquisaFacade {

    @Inject
    private ColetaPesquisaDAO coletaPesquisaDAO;
    
    @Inject
    private FotoDAO fotoDAO;


    public List<ColetaPesquisa> buscarColetasPorPesquisa(Pesquisa pesquisa) {
		return coletaPesquisaDAO.buscarColetaPesquisaPorPesquisa(pesquisa.getId());
	}
    
    public List <ColetaPesquisa> buscarMultiplasRespostas(ColetaPesquisa coletaPesquisa){
        return coletaPesquisaDAO.buscarMultiplasRespostas(coletaPesquisa);
    }
    
    public ColetaPesquisa buscarResposta(Pesquisa pesquisa, Pergunta pergunta) {
		return coletaPesquisaDAO.buscarResposta(pesquisa, pergunta);
	}
    
    public List<ColetaPesquisa> buscarTodasColetasPesquisa(){
        return coletaPesquisaDAO.findAll();
    }

	public void excluirColetaPesquisa(ColetaPesquisa coletaPesquisa){
    	coletaPesquisa.setExcluido(true);
        coletaPesquisaDAO.saveOrUpdate(coletaPesquisa);
    }
	
	public String getNomeFotos(Pesquisa pojo) {
		String nomesFotos = "";
		Foto f;
		List<Foto> list = fotoDAO.buscarFotosPorPesquisa(pojo.getId());
		
		if (list == null) {
			nomesFotos = "-";
		}else {
			
			for (int i = 0; i < list.size(); i++) {
				f = list.get(i);
				nomesFotos += f.getNome();
				
				if (i < list.size()-1) {
					nomesFotos += ", ";
				}
			}
		}
		return nomesFotos;
	}

	public List<ColetaPesquisa> pesquisarColetasPesquisaPorPesquisa( Pesquisa pesquisa) {
		return coletaPesquisaDAO.buscarColetaPesquisaPorPesquisa(pesquisa.getId());
	}

	public List<ColetaPesquisa> pesquisarColetasPesquisaPorQuestionario( Questionario questionario) {
		return coletaPesquisaDAO.buscarColetaPesquisaPorQuestionario(questionario);
	}

	public ColetaPesquisa salvarColetaPesquisa(ColetaPesquisa coletaPesquisa){
        return coletaPesquisaDAO.saveOrUpdate(coletaPesquisa);
    }

}
