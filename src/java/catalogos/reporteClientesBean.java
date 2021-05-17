/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catalogos;

import controllers.SUsuariosJpaController;
import entidades.SUsuarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import objetos.ReporteCliente;

/**
 *
 * @author Alberto Salomon Fecha: 2021-05-14
 */
@ManagedBean
@SessionScoped
public class ReporteClientesBean implements Serializable {

    private List<ReporteCliente> listaClientes;
    private SUsuarios usuario = new SUsuarios();
    SUsuariosJpaController usuariosController = new SUsuariosJpaController();

    public ReporteClientesBean() {
        this.listaClientes = new ArrayList<>();
    }

    /**
     * Mediante el controller de usuario, utilza la función traerClientes para
     * buscar los datos del cliente segun el usuario modifica, y luego
     * llenar la listaCLientes y mostrarla
     * modificados por el usuario ingresado
     */
    public final void llenaReporteClientes() {
        try {
            listaClientes = usuariosController.traerClientes(usuario.getIdUsuario());
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Consulta exitosa", 
                    usuario.getNombreUsuario());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            Logger.getLogger(ReporteClientesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ReporteCliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<ReporteCliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public SUsuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(SUsuarios usuario) {
        this.usuario = usuario;
    }

}
