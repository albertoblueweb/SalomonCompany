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
import org.primefaces.PrimeFaces;

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
    SAccesosJpaController accesosController = new SAccesosJpaController();

    public AccesosBean() {
        llenaAccesos();
    }

    /**
     * Mediante el controller de la tablas SAccesos llama a todas las entidades
     * generadas mediante la consulta de la tabla
     */
    public final void llenaAccesos() {
        try {
            listaAccesos = accesosController.findSAccesosEntities();

        } catch (Exception ex) {
            Logger.getLogger(AccesosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * El objeto acceso guarda toda la información ingresada por el usuario en
     * los inputs del dialogo "Nuevo Acceso" y mediante la funcion create añade
     * un nuevo campo en la tabla S_ACCESOS, luego arroja un mensaje en caso de
     * éxito
     */
    public void guardaAcceso() {
        if (acceso.getNombreAcceso().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo vacío", "Ingrese nombre");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {
                acceso.setActivo(true);
                acceso.setFechaServidor(new Date());
                accesosController.create(acceso);
                llenaAccesos();
                FacesMessage msg;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado", acceso.getNombreAcceso());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().executeScript("PF('dlgAcc').hide()");
                acceso = new SAccesos();
            } catch (Exception ex) {
                Logger.getLogger(AccesosBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * El objeto selectedAacceso guarda toda la información del dato
     * seleccionado por el usuario en la datatable desplegada, luego los muestra
     * en los inputs del dialogo "Modificar Acceso" para modificarse y mediante
     * la funcion edit actualiza los campos del dato en la tabla S_ACCESOS,
     * luego arroja un mensaje en caso de éxito
     */
    public void modificaAcceso() {
        if (selectedAcceso.getNombreAcceso().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo vacío", "Ingrese nombre");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {
                accesosController.edit(selectedAcceso);
                llenaAccesos();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha modificado", selectedAcceso.getNombreAcceso());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().executeScript("PF('dlgAcc2').hide()");
                selectedAcceso = new SAccesos();
            } catch (Exception ex) {
                Logger.getLogger(AccesosBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * El objeto selectedAacceso guarda toda la información del dato
     * seleccionado por el usuario en la datatable desplegada y mediante la
     * funcion destroy borra los campos del dato en la tabla S_ACCESOS, luego
     * arroja un mensaje en caso de éxito
     */
    public void eliminaAcceso() {
        try {
            accesosController.destroy(selectedAcceso.getIdAcceso());
            llenaAccesos();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha eliminado registro", selectedAcceso.getNombreAcceso());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            Logger.getLogger(AccesosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Gets y Sets">
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
//</editor-fold>

}
