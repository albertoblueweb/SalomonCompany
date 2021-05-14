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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import objetos.ReporteCliente;

/**
 *
 * @author Alberto Salomon Fecha: 2021-05-14
 */
@ManagedBean
@SessionScoped
public class reporteClientesBean implements Serializable {
    private List<ReporteCliente> listaClientes;
    private SUsuarios usuario = new SUsuarios();
    SUsuariosJpaController usuariosController = new SUsuariosJpaController();

    public reporteClientesBean() {
        this.listaClientes = new ArrayList<>();
    }

    /**  
     * Mediante el controller de uausario, utilza la función traerClientes, para
     * generadas mediante la consulta de la tablaregresar los clientes modificados 
     * por el usuario ingresado
     */
    public final void llenaReporteClientes() {
        try {
            listaClientes = usuariosController.traerClientes(usuario.getIdUsuario());

        } catch (Exception ex) {
            Logger.getLogger(AccesosBean.class.getName()).log(Level.SEVERE, null, ex);
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
