/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.negocio.quiz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.frw.base.dao.UsuarioDAO;
import com.frw.base.dao.sisdat.FotoDAO;
import com.frw.base.dao.sisdat.PerguntaDAO;
import com.frw.base.dao.sisdat.PesquisaDAO;
import com.frw.base.dao.sisdat.QuestionarioDAO;
import com.frw.base.dao.sisdat.RespostaDAO;
import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Foto;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.Resposta;
import com.frw.manutencao.dominio.dto.FilterListRespostaPesquisaDTO;

/**
 * 
 * @author Leonardo Barros
 * 
 */
@Stateless
public class PesquisaFacade {
	private static Logger logger = Logger.getLogger(PesquisaFacade.class.getName());
	
	@EJB
	private ColetaPesquisaFacade coletaPesquisaFacade;

	@Inject
	private FotoDAO fotoDAO;

	@Inject
	private PerguntaDAO perguntaDAO;

	@Inject
	private PesquisaDAO pesquisaDAO;

	@Inject
	private QuestionarioDAO questionarioDAO;

	@Inject
	private RespostaDAO respostaDAO;
	
	@Inject
	private UsuarioDAO usuarioDAO;

	public List<Foto> buscarFotosPesquisa(Pesquisa pesquisa) {
		return fotoDAO.buscarFotosPorPesquisa(pesquisa.getId());
	}
	
	public List<Pesquisa> buscarPesquisaPorQuestionario (Questionario questionario){
		return pesquisaDAO.findPesquisaByQuestionario(questionario);
	}
	
	public List<Pesquisa> buscarPesquisaPorQuestionarioUsuario(Questionario questionario, FilterListRespostaPesquisaDTO filterDTO){
		return pesquisaDAO.findPesquisaByQuestionarioUsuario(questionario, filterDTO);
	}

	/**
	 * 
	 */
	public List<Pesquisa> buscarPesquisas() {
		return pesquisaDAO.findAll();
	}

	/**
	 * Gera um zip contendo uma pasta fotos com todas as fotos da pesquisa.
	 * @param fileOrigin
	 * @param zipDestination
	 * @param fotos
	 * @author Miller
	 * @param planilha 
	 * @return
	 */
	public byte[] generateZipFile(List<Foto> fotos, byte[] planilha) {
		
		String namefileEntryZip;
    	
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
			
			logger.severe("Criando zip.");
			
			// add fotos da pesquisa no zip
			for (Foto f : fotos) {
			
				if (f.getPesquisa() != null && f.getFoto() != null) {
					namefileEntryZip = "fotos\\"+ f.getNome();
					
					if (!namefileEntryZip.contains("png")) {
						namefileEntryZip += ".png";
					}
					zipOutputStream.putNextEntry(new ZipEntry(namefileEntryZip));
					zipOutputStream.write(f.getFoto());
					
					logger.severe("Foto adicionada -> " + namefileEntryZip + "(tamanho: "+ f.getFoto().length +")");
				}
				
			}
		
			// add planilha no zip
			if (planilha != null) {
				zipOutputStream.putNextEntry(new ZipEntry("RespostasPesquisa.xls"));
				zipOutputStream.write(planilha);
				
				logger.severe("Xls adicionado -> RespostasPesquisa.xls");
			}
			
    		zipOutputStream.closeEntry();
    		zipOutputStream.close();
 
    		return outputStream.toByteArray();
 
    	}catch(IOException ex){
    	   ex.printStackTrace();
    	   return null;
    	}
	}

	public void gerarNomeFotos(List<Pesquisa> pesquisas) {
    	for (Pesquisa p : pesquisas) {
    		gerarNomeFotos(p);
		}
	}

	public void gerarNomeFotos(Pesquisa p) {
		List<Foto> fotosPesquisa = p.getFotos();
		if (fotosPesquisa != null && !fotosPesquisa.isEmpty()) {
			
			int i = 0;
			for (Foto f : fotosPesquisa) {
				if (f.getNome() == null || f.getNome().trim().isEmpty()) {
					f.setNome("SiSdat-" + p.getDataFechamento().getTime() + i + ".png");
				}
				i++;
			}
			
			salvarFotosPesquisa(fotosPesquisa);
		}
	}
	
	@SuppressWarnings("finally")
	public String getReposta(ColetaPesquisa coletaPesquisa){
		String resposta = "";
    	try {
			if(coletaPesquisa.getResposta() != null ){
				if(coletaPesquisa.getPergunta().getTipo().getId() == 2){
					List<Resposta> listaResposta = getMultiplasRespostas(coletaPesquisa);
					
					for(Resposta resp : listaResposta){
						resposta += resp.getDescricao()+",";
					}
					
					if(resposta.endsWith(",")){
						resposta = resposta.substring(0, resposta.length()-1);
					}
				}else{
					resposta = coletaPesquisa.getResposta().getDescricao();
				}
			}
			if(coletaPesquisa.getCampoLivre() != null && !coletaPesquisa.getCampoLivre().isEmpty()){
				resposta += coletaPesquisa.getCampoLivre(); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return resposta;
		}
	}

	public void salvarFotosPesquisa(List<Foto> fotos) {
		for (Foto foto : fotos) {
			fotoDAO.saveOrUpdate(foto);
		}
	}

	public boolean salvarPesquisa(Pesquisa pesquisa) {

		try {

			// Insere pesquisa no banco local
			pesquisa.setDataAlteracao(new Date());
			pesquisa.setUsuarioAlteracao("importacao");
			pesquisa.setExcluido(false);
			pesquisa.setVisivel(true);

			gerarNomeFotos(pesquisa);

			pesquisaDAO.saveOrUpdate(pesquisa);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public Pesquisa saveOrUpdate(Pesquisa pesquisa) {
		return pesquisaDAO.saveOrUpdate(pesquisa);
	}
	
	private List<Resposta> getMultiplasRespostas(ColetaPesquisa coletaPesquisa) {
		List<ColetaPesquisa> respostas = coletaPesquisaFacade.buscarMultiplasRespostas(coletaPesquisa);
		List<Resposta> respostaXGH = new ArrayList<Resposta>();
		
		for (ColetaPesquisa coleta : respostas) {
			if (coleta.getResposta() != null) {
				if(respostaXGH != null && !respostaXGH.contains(coleta.getResposta())){
					respostaXGH.add(coleta.getResposta());
				}
			}
		}
		return respostaXGH;
	}
}
