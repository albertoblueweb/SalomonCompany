/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catalogos;

import controllers.SAccesosJpaController;
import entidades.SAccesos;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alberto Salomon fecha: 2021-05-07
 */
@ManagedBean
@SessionScoped
public class AccesosBean implements Serializable {

    private List<SAccesos> listaAccesos;
    private SAccesos acceso = new SAccesos();
    private SAccesos selectedAcceso = new SAccesos();

    public AccesosBean() {
        llenaAccesos();
    }

    public final void llenaAccesos() {
        try {
            SAccesosJpaController accesosController = new SAccesosJpaController();
            listaAccesos = accesosController.findSAccesosEntities();

        } catch (Exception ex) {
            Logger.getLogger(DistribuidorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardaAcceso() {
        try {
            SAccesosJpaController accesosController = new SAccesosJpaController();
            acceso.setFechaServidor(new Date());
            accesosController.create(acceso);
            llenaAccesos();
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado", acceso.getNombreAcceso());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            Logger.getLogger(DistribuidorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificaAcceso() {
        try {
            SAccesosJpaController accesosController = new SAccesosJpaController();
            accesosController.edit(selectedAcceso);
            llenaAccesos();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado",
                    selectedAcceso.getNombreAcceso());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            Logger.getLogger(DistribuidorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaAcceso() {
        try {
            SAccesosJpaController accesosController = new SAccesosJpaController();
            accesosController.destroy(selectedAcceso.getIdAcceso());
            llenaAccesos();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado registro",
                    selectedAcceso.getNombreAcceso());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            Logger.getLogger(DistribuidorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SAccesos> getListaAccesos() {
        return listaAccesos;
    }

    public void setListaAccesos(List<SAccesos> listaAccesos) {
        this.listaAccesos = listaAccesos;
    }

    public SAccesos getAcceso() {
        return acceso;
    }

    public void setAcceso(SAccesos acceso) {
        this.acceso = acceso;
    }

    public SAccesos getSelectedAcceso() {
        return selectedAcceso;
    }

    public void setSelectedAcceso(SAccesos selectedAcceso) {
        this.selectedAcceso = selectedAcceso;
    }

}
