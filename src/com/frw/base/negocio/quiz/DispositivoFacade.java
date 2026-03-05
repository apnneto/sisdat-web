package com.frw.base.negocio.quiz;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.frw.base.dao.sisdat.DispositivoDAO;
import com.frw.base.dominio.sisdat.Dispositivo;

@Stateless
public class DispositivoFacade {

    @Inject
    private DispositivoDAO dispositivoDAO;


    public List<Dispositivo> buscarTodosDispositivo(){
        return dispositivoDAO.findAll();
    }
    
    public void excluirDispositivo(Dispositivo dispositivo){
    	dispositivo.setExcluido(true);
        dispositivoDAO.saveOrUpdate(dispositivo);
    }
    
    public Dispositivo salvarDispositivo(Dispositivo dispositivo){
        return dispositivoDAO.saveOrUpdate(dispositivo);
    }

}
