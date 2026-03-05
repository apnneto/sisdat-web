/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.siclo.web.webservice;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.frw.base.dominio.base.Usuario;
import com.frw.base.dominio.sisdat.ColetaPesquisa;
import com.frw.base.dominio.sisdat.Foto;
import com.frw.base.dominio.sisdat.Pergunta;
import com.frw.base.dominio.sisdat.Pesquisa;
import com.frw.base.dominio.sisdat.Questionario;
import com.frw.base.dominio.sisdat.Resposta;
import com.frw.base.dominio.sisdat.TipoLov;
import com.frw.base.negocio.UsuarioFacade;
import com.frw.base.negocio.quiz.ColetaPesquisaFacade;
import com.frw.base.negocio.quiz.PerguntaFacade;
import com.frw.base.negocio.quiz.PesquisaFacade;
import com.frw.base.negocio.quiz.QuestionarioFacade;
import com.frw.base.negocio.quiz.RespostaFacade;
import com.frw.base.util.SistemaUtil;
import com.frw.manutencao.dominio.dto.FilterListQuestionarioDTO;
import com.frw.manutencao.dominio.dto.FilterListRespostaPesquisaDTO;
import com.frw.manutencao.dominio.dto.FotoDTO;
import com.frw.manutencao.dominio.dto.RespostaDTO;
import com.frw.manutencao.dominio.dto.RetornoDTO;
import com.frw.siclo.web.webservice.dto.PerguntaWSDTO;
import com.frw.siclo.web.webservice.dto.QuestionarioWSDTO;
import com.frw.siclo.web.webservice.dto.RespostaWSDTO;
import com.frw.siclo.web.webservice.dto.StatusPesquisaWSDTO;
import com.frw.siclo.web.webservice.dto.TipoLovWSDTO;
import com.frw.siclo.web.webservice.dto.UsuarioWSDTO;
import com.google.gson.Gson;

/**
 * 
 * @author Maximiliano
 */

@Path("quiz")
public class AndroidWS {

    private ColetaPesquisaFacade coletaPesquisaFacade;
    private Decoder decoder = Base64.getDecoder();
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private PerguntaFacade perguntaFacade;
    private PesquisaFacade pesquisaFacade;
    private QuestionarioFacade questionarioFacade;

    private RespostaFacade respostaFacade;

    private UsuarioFacade usuarioFacade;

    public AndroidWS() throws NamingException {
        InitialContext context = new InitialContext();
        usuarioFacade = (UsuarioFacade) context.lookup("java:global/sisdat-web/UsuarioFacade");
        pesquisaFacade = (PesquisaFacade) context.lookup("java:global/sisdat-web/PesquisaFacade");
        perguntaFacade = (PerguntaFacade) context.lookup("java:global/sisdat-web/PerguntaFacade");
        respostaFacade = (RespostaFacade) context.lookup("java:global/sisdat-web/RespostaFacade");
        questionarioFacade = (QuestionarioFacade) context.lookup("java:global/sisdat-web/QuestionarioFacade");
        coletaPesquisaFacade = (ColetaPesquisaFacade) context.lookup("java:global/sisdat-web/ColetaPesquisaFacade");

    }

    @GET
    @Path("/buscarColetasPesquisaPorPesquisa")
    @Produces("application/json;charset=utf-8")
    public String buscarColetasPesquisaPorPesquisa(
            @DefaultValue("") @QueryParam("login") String login,
            @DefaultValue("") @QueryParam("senha") String senha, 
            @DefaultValue("") @QueryParam("codigoQuestionario") String codigoQuest,
            @DefaultValue("") @QueryParam("dataInicio") String strDataInicio,
            @DefaultValue("") @QueryParam("dataFim") String strDataFim) {
        JSONObject json = new JSONObject();
        try {
            if (login == null || senha == null || codigoQuest == null 
                    || login.isEmpty() || senha.isEmpty() || codigoQuest.isEmpty()) {
                json.put("mensagem", "Valores informados incompletos");

            } else {

                Usuario usuario = usuarioFacade.buscarUsuarioPorLogin(login);

                if (usuario != null && !usuario.isExcluido() && usuario.getSenha().equals(senha)) {
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

                    FilterListRespostaPesquisaDTO filterDTO = new FilterListRespostaPesquisaDTO(null, null);
                    filterDTO.setUsuario(usuario);
                    try {
                        if(strDataInicio != null && !strDataInicio.trim().isEmpty()){
                            filterDTO.setDataInicio(sdf.parse(strDataInicio));
                        }
                        if(strDataFim != null && !strDataFim.trim().isEmpty()){
                            filterDTO.setDataFim(sdf.parse(strDataFim));
                        }
                    } catch (Exception e) {
                        json.put("mensagem", "Data Inválida - Não compreende ao padrão ddmmyyyy");
                        json.put("info", e.getMessage());
                    }

                    Questionario questionario = questionarioFacade.buscaQuestionarioPorCodigo(codigoQuest);
                    List<Pergunta> listPerguntas = perguntaFacade.buscarPerguntasPorQuestionario(questionario);
                    List<Pesquisa> listPesquisas = pesquisaFacade.buscarPesquisaPorQuestionarioUsuario(questionario,
                            filterDTO);

                    if (questionario != null && questionario.getId() != null) {
                        RetornoDTO retorno = new RetornoDTO();

                        retorno.addPerguntas("Usuário");
                        retorno.addPerguntas("Latitude");
                        retorno.addPerguntas("Longitude");

                        for (Pergunta p : listPerguntas) {
                            retorno.addPerguntas(p.getDescricao());
                        }

                        for (Pesquisa pesquisa : listPesquisas) {
                            RespostaDTO dto = new RespostaDTO();
                            dto.addResposta(pesquisa.getUsuario().getNome().toUpperCase());
                            dto.addResposta(SistemaUtil.formatNumeric(pesquisa.getLatitudeFinal(), 6));
                            dto.addResposta(SistemaUtil.formatNumeric(pesquisa.getLongitudeFinal(), 6));

                            List<Foto> listFotos = pesquisaFacade.buscarFotosPesquisa(pesquisa);
                            List<FotoDTO> fotosDTO = new ArrayList<FotoDTO>();

                            if (listFotos != null && !listFotos.isEmpty()) {
                                for (Foto foto : listFotos) {
                                    FotoDTO fotoDTO = new FotoDTO();
                                    try {
                                        fotoDTO.setBytesFotoToString(foto.getFoto());
                                        fotosDTO.add(fotoDTO);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            dto.setFotos(fotosDTO);

                            for (Pergunta pergunta : listPerguntas) {
                                dto.addResposta(coletaPesquisaFacade.buscarResposta(pesquisa, pergunta).getResposta()
                                        .getDescricao());
                            }
                            retorno.addRespostas(dto);
                        }
                        return new Gson().toJson(retorno);
                    } else {
                        json.put("mensagem", "Questionário não encontrado.");
                    }
                } else {
                    json.put("mensagem", "Usuário ou Senha incorreto.");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            try {
                json.put("mensagem", "Erro interno do servidor.Contate o administrador do sistema.");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return json.toString();

    }

    @GET
    @Path("/buscarPerguntas")
    @Produces("application/json")
    public List<PerguntaWSDTO> buscarPerguntas() {

        List<PerguntaWSDTO> listWSDTO = new ArrayList<PerguntaWSDTO>();

        List<Pergunta> list = perguntaFacade.buscarTodasPerguntas();

        for (Pergunta p : list) {
            PerguntaWSDTO wsdto = new PerguntaWSDTO();
            if (p.getQuestionario() != null) {
                wsdto.setCodigo(p.getId());
                wsdto.setDescricao(p.getDescricao());
                wsdto.setCodigoQuestionario(p.getQuestionario().getId());
                wsdto.setOrdem(p.getOrdem());
                wsdto.setSubDescricao(p.getSubdescricao());
                wsdto.setTipo(p.getTipo().getId());
                wsdto.setDataType(p.getTipoDado().getId());
                wsdto.setPrecisaoInicial(p.getPrecisaoInicial());
                wsdto.setPrecisaoFinal(p.getPrecisaoFinal());
                wsdto.setComentario(p.getComentario());
                wsdto.setObrigatoria(p.isObrigatoria());
                wsdto.setDescricaoResumida(p.getDescricaoResumida());

                listWSDTO.add(wsdto);
            }
        }

        return listWSDTO;
    }

    @GET
    @Path("/buscarQuestionarios")
    @Produces("application/json")
    public List<QuestionarioWSDTO> buscarQuestionario() {

        List<QuestionarioWSDTO> listWSDTO = new ArrayList<QuestionarioWSDTO>();

        List<Questionario> list = questionarioFacade.buscarTodosQuestionarios();

        for (Questionario q : list) {
            QuestionarioWSDTO wsdto = new QuestionarioWSDTO();
            wsdto.setId(q.getId());
            wsdto.setDescricao(q.getDescricao());
            wsdto.setOrdem(q.getOrdem());
            wsdto.setResumo(q.getResumo());
            wsdto.setOrientacao(q.getOrientacao());
            wsdto.setVersao(q.getVersao());
            wsdto.setCodigo(q.getCodigo());

            listWSDTO.add(wsdto);
        }

        return listWSDTO;
    }

    @GET
    @Path("/buscarQuestionariosValidos")
    @Produces("application/json")
    public Response buscarQuestionarioValidos(@DefaultValue("") @QueryParam("idUsuario") String idUsuario) {
        
        if(idUsuario != null && !idUsuario.trim().isEmpty()){
            try {
                Usuario usuario = usuarioFacade.buscarPorId(Long.parseLong(idUsuario));
                
                List<QuestionarioWSDTO> listWSDTO = new ArrayList<QuestionarioWSDTO>();
                
                FilterListQuestionarioDTO filterDTO = new FilterListQuestionarioDTO();
                filterDTO.setUsuario(usuario);
                
                List<Questionario> list = questionarioFacade.pesquisarQuestionarioPorFiltro(filterDTO);
                for (Questionario q : list) {
                    QuestionarioWSDTO wsdto = new QuestionarioWSDTO();
                    wsdto.setId(q.getId());
                    wsdto.setDescricao(q.getDescricao());
                    wsdto.setOrdem(q.getOrdem());
                    wsdto.setResumo(q.getResumo());
                    wsdto.setOrientacao(q.getOrientacao());
                    wsdto.setVersao(q.getVersao());
                    wsdto.setCodigo(q.getCodigo());
                    
                    listWSDTO.add(wsdto);
                }
                return Response.ok().entity(new Gson().toJson(listWSDTO)).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("/buscarRespostas")
    @Produces("application/json")
    public List<RespostaWSDTO> buscarRespostas() {

        List<RespostaWSDTO> listWSDTO = new ArrayList<RespostaWSDTO>();

        List<Resposta> list = respostaFacade.buscarTodasRespostas();

        for (Resposta r : list) {
            RespostaWSDTO wsdto = new RespostaWSDTO();
            if (r.getPergunta() != null) {
                wsdto.setCodigo(r.getId());
                wsdto.setDescricao(r.getDescricao());
                wsdto.setCodigoPergunta(r.getPergunta().getId());
                wsdto.setOrdem(r.getOrdem());
                if (r.getOrdem() != null) {
                    wsdto.setValor(r.getOrdem().toString());
                }
                wsdto.setCorreta(r.getCorreta());

                listWSDTO.add(wsdto);
            }
        }

        return listWSDTO;
    }
    
    @GET
    @Path("/buscarTiposLov")
    @Produces("application/json")
    public List<TipoLovWSDTO> buscarTiposLov() {

        List<TipoLovWSDTO> listWSTDO = new ArrayList<TipoLovWSDTO>();

        List<TipoLov> list = questionarioFacade.buscarTodosTiposLov();

        for (TipoLov r : list) {
            TipoLovWSDTO wsdto = new TipoLovWSDTO();
            wsdto.setCodigo(r.getId());
            wsdto.setDescricao(r.getDescricao());

            listWSTDO.add(wsdto);
        }
        return listWSTDO;
    }

    @GET
    @Path("/buscarUsuarios")
    @Produces("application/json")
    public List<UsuarioWSDTO> buscarUsuarios() {

        List<UsuarioWSDTO> listWSDTO = new ArrayList<UsuarioWSDTO>();

        List<Usuario> list = usuarioFacade.buscarUsuarios();

        for (Usuario u : list) {
            UsuarioWSDTO usuarioWSDTO = new UsuarioWSDTO();
            usuarioWSDTO.setCodigo(u.getId());
            usuarioWSDTO.setNome(u.getNome());
            usuarioWSDTO.setLogin(u.getLogin());
            usuarioWSDTO.setPassword(u.getSenha());

            listWSDTO.add(usuarioWSDTO);
        }
        
        System.out.println("Great");

        return listWSDTO;
    }

    @POST
    @Path("/enviarPesquisa")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public StatusPesquisaWSDTO enviarPesquisa(@DefaultValue("") @FormParam("json") String strJSON) {
        System.out.println("JSON RECEBIDO " + strJSON);

        System.out.println("RECEBIDA PESQUISA PARA SINCRONIZACAO");

        StatusPesquisaWSDTO statusPesquisaWSDTO = new StatusPesquisaWSDTO();

        try {

            JSONObject jsonObject = new JSONObject(strJSON);

            System.out.println("DATA ABERTURA:" + jsonObject.getString("dataAbertura"));

            // Cria o objeto Pesquisa
            Pesquisa pesquisa = new Pesquisa();

            pesquisa.setQuestionario(questionarioFacade.buscarQuestionario(jsonObject.getLong("codigoQuestionario")));
            pesquisa.setDataAbertura(df.parse(jsonObject.getString("dataAbertura")));
            pesquisa.setUsuario(usuarioFacade.buscarPorId(jsonObject.getLong("usuario")));
            pesquisa.setDataFechamento(df.parse(jsonObject.getString("dataFehamento")));

            if (jsonObject.has("latitudeInicial"))
                pesquisa.setLatitudeInicial(jsonObject.getDouble("latitudeInicial"));

            if (jsonObject.has("longitudeInicial"))
                pesquisa.setLongitudeInicial(jsonObject.getDouble("longitudeInicial"));

            if (jsonObject.has("latitudeFinal"))
                pesquisa.setLatitudeFinal(jsonObject.getDouble("latitudeFinal"));

            if (jsonObject.has("longitudeFinal"))
                pesquisa.setLongitudeFinal(jsonObject.getDouble("longitudeFinal"));

            pesquisa.setDevice(jsonObject.getString("device"));
            pesquisa.setFechamento(jsonObject.getString("fechamento"));

            if (jsonObject.has("fotos")) {
                JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("fotos").toString());
                pesquisa.setFotos(new ArrayList<Foto>());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectFoto = jsonArray.getJSONObject(i);
                    Foto foto = new Foto();
                    try {
                        // Use Java 8 Base64 decoder instead of internal sun.misc.BASE64Decoder
                        foto.setFoto(decoder.decode(jsonObjectFoto.getString("foto")));
                        foto.setPesquisa(pesquisa);
                        pesquisa.getFotos().add(foto);
                    } catch (IllegalArgumentException e) {
                        // Thrown when the input is not valid Base64
                        e.printStackTrace();
                    }

                }
            }

            // Popula data de sincronizacao da pesquisa
            pesquisa.setDataSincronizacao(new Date());

            System.out.println("QUESTIONARIO: " + pesquisa.getQuestionario().getId());

            // Popula dados específicos da pesquisa

            // Cria as coletas
            JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("coletas").toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectColeta = jsonArray.getJSONObject(i);

                if (jsonObjectColeta.has("pergunta")) {

                    ColetaPesquisa coleta = new ColetaPesquisa();
                    coleta.setPergunta(perguntaFacade.buscarPorId(jsonObjectColeta.getLong("pergunta")));

                    String resposta = jsonObjectColeta.getString("resposta");
                    if (resposta != null && !resposta.isEmpty())
                        coleta.setResposta(respostaFacade.buscarPorId(Long.parseLong(resposta)));

                    if (jsonObjectColeta.getString("campoLivre") != null
                            && !jsonObjectColeta.getString("campoLivre").isEmpty())
                        coleta.setCampoLivre(jsonObjectColeta.getString("campoLivre"));

                    if (!pesquisa.getColetasPesquisa().contains(coleta)) {
                        pesquisa.getColetasPesquisa().add(coleta);
                        coleta.setPesquisa(pesquisa);
                    }
                }
            }

            pesquisaFacade.salvarPesquisa(pesquisa);

            // Monta status de retorno com sucesso para pesquisa sincronizada
            statusPesquisaWSDTO.setStatus("SINCRONIZADO");
            statusPesquisaWSDTO.setDataSincronizacao(df.format(pesquisa.getDataSincronizacao()));

        } catch (Exception e) {
            // Erro na sincronização da pesquisa
            e.printStackTrace();
            statusPesquisaWSDTO.setStatus("NAO SINCRONIZADO");
        }

        return statusPesquisaWSDTO;
    }
}