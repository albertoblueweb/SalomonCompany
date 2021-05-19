/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import controllers.SAplicacionesJpaController;
import entidades.SAplicaciones;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Alberto Salomon fecha: 2021-05-18
 */
@ManagedBean
@RequestScoped
public class menuModel {

    private MenuModel model;

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        SAplicacionesJpaController aplicacionController = new SAplicacionesJpaController();
        List<SAplicaciones> listaAplicacion = aplicacionController.traerAplicacion();

        DefaultSubMenu submenu = new DefaultSubMenu();
        DefaultMenuItem itemSubmenu = new DefaultMenuItem();

        for (SAplicaciones item : listaAplicacion) {
            if (item.getIdAplicacion() == 0) {
                submenu.setLabel(item.getNombreAplicacion());
                submenu.setIcon(item.getIcono());

                String tituloMenu = item.getNombreAplicacion();
                String tituloConfirma = "";

                for (SAplicaciones element : listaAplicacion) {
                    if (element.getIdAplicacion() == 0) {
                        tituloConfirma = element.getNombreAplicacion();
                    }
                    if (tituloMenu == tituloConfirma && element.getIdAplicacion() != 0) {
                        itemSubmenu.setValue(element.getNombreAplicacion());
                        itemSubmenu.setUrl("/../SalomonCompany" + element.getUrl());
                        itemSubmenu.setIcon(element.getIcono());
                        submenu.getElements().add(itemSubmenu);
                        itemSubmenu = new DefaultMenuItem();
                    }
                }
                model.getElements().add(submenu);
                submenu = new DefaultSubMenu();
            }
        }
    }

    public MenuModel getModel() {
        return model;
    }

    public void redirect() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath());
    }

    public void save() {
        addMessage("Save", "Data saved");
    }

    public void update() {
        addMessage("Update", "Data updated");
    }

    public void delete() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete", "Data deleted");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
