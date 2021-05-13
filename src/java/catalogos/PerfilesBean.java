package catalogos;

import controllers.SAccesosJpaController;
import controllers.SPerfilesAccesosJpaController;
import controllers.SPerfilesJpaController;
import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import entidades.SAccesos;
import entidades.SPerfiles;
import entidades.SPerfilesAccesos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;
import utils.TraeDatoSesion;

/**
 *
 * @author Alberto Salomón Fecha: 2021-05-07
 */
@ManagedBean
@SessionScoped
public class PerfilesBean implements Serializable {

    private List<SPerfiles> listaPerfiles;
    private SPerfiles perfil = new SPerfiles();
    private SPerfiles selectedPerfil;
    private SPerfilesAccesos perfilAcceso = new SPerfilesAccesos();
    private List<SAccesos> selectedAcceso;
    private DualListModel<SAccesos> listaAccesosDual = new DualListModel<>();
    private DualListModel<SAccesos> accesosDLM = new DualListModel<>();
    private List<SAccesos> listaAccesosDisponibles = new ArrayList<>();
    private List<SAccesos> listaAccesosActuales = new ArrayList<>();

    SAccesosJpaController accesosController = new SAccesosJpaController();
    SPerfilesAccesosJpaController perfilesAccesosController
            = new SPerfilesAccesosJpaController();

    public PerfilesBean() {
        llenaPerfiles();
        traerAccesosPerfil();
    }

    /**
     * Trae los datos de S_ACCESOS para llenar el picklist de "Nuevo perfil"
     */
    @PostConstruct
    public void init() {
        accesosController = new SAccesosJpaController();
        List<SAccesos> accesosSource;
        List<SAccesos> accesosTarget = new ArrayList<>();
        accesosSource = accesosController.findSAccesosEntities();

        listaAccesosDual = new DualListModel<>(accesosSource, accesosTarget);
    }

    /**
     * Los datos ingresados por el usuario se guardan en el objeto perfil,
     * mediante el controller invocamos la función create para guardar en
     * S_PERFILES
     */
    public void guardaPerfil() {
        if (perfil.getNombrePerfil().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo vacío", "Ingrese nombre");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {
                SPerfilesJpaController model = new SPerfilesJpaController();
                perfil.setActivo(true);
                perfil.setFechaAlta(new Date());
                perfil.setFechaServidor(new Date());
                perfil.setIdUsuarioModifica(TraeDatoSesion.traerIdUsuario());
                model.create(perfil);
                selectedAcceso = listaAccesosDual.getTarget();
                guardaPerfilAccesos(perfil, selectedAcceso);
                llenaPerfiles();
                FacesMessage msg;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado",
                        perfil.getNombrePerfil());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().executeScript("PF('dlg').hide()");
            } catch (Exception ex) {
                Logger.getLogger(PerfilesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Los datos ingresados por el usuario se guardan en la lista
     * selectedAcceso, mediante el controller invocamos la función create para
     * guardar en S_PERFILES
     *
     * @param perfil trae los datos del perfil en S_PERFIL_ACCESOS
     * @param listaAccesos trae los datos del targetList del PickList
     */
    public void guardaPerfilAccesos(SPerfiles perfil, List<SAccesos> listaAccesos) {
        try {
            for (Object id : listaAccesos) {
                SAccesos acceso = accesosController.findSAccesos(Integer.parseInt(id.toString()));
                perfilAcceso.setSPerfiles(perfil);
                perfilAcceso.setSAccesos(acceso);
                perfilAcceso.setFechaServidor(new Date());
                perfilAcceso.setIdUsuarioModifica(TraeDatoSesion.traerIdUsuario());
                perfilesAccesosController.create(perfilAcceso);
            }
        } catch (Exception ex) {
            Logger.getLogger(PerfilesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Mediante el controlador llama a la función findSPerfilesEntities para
     * consultar la tabla S_PERFILES y traer los datos de la misma
     */
    public final void llenaPerfiles() {
        try {
            SPerfilesJpaController perfilesController = new SPerfilesJpaController();
            listaPerfiles = perfilesController.findSPerfilesEntities();

        } catch (Exception ex) {
            Logger.getLogger(PerfilesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Trae los datos de S_ACCESOS_PERFILES para llenar el picklist de
     * "Modificar perfil" con los accesos actuales del perfil
     */
    public final void traerAccesosPerfil() {
        accesosController = new SAccesosJpaController();
        accesosDLM = new DualListModel<>();
        listaAccesosDisponibles = new ArrayList<>();
        listaAccesosActuales = new ArrayList<>();

        listaAccesosActuales = accesosController.traerAccesosActuales(selectedPerfil);
        listaAccesosDisponibles = accesosController.traerAccesosDisponibles(selectedPerfil);
        accesosDLM = new DualListModel<>(getListaAccesosDisponibles(), getListaAccesosActuales());
    }

    /**
     * Compara un listado con los accesos seleccionados y los accesos con los
     * que ya contaba el perfil, creando un nuevo perfil-acceso en caso de
     * añadir un acceso en el picklist o eliminando en caso de quitarlo
     */
    public void modificaPerfil() {
        if (selectedPerfil.getNombrePerfil().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo vacío", "Ingrese nombre");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {
                List<SAccesos> oldAccesos = accesosActuales();
                List<SAccesos> newAccesos = new ArrayList<>();
                List<SAccesos> addAccesos = new ArrayList<>();

                for (Object acc : accesosDLM.getTarget()) {
                    SAccesos acceso = accesosController.findSAccesos(Integer.parseInt(acc.toString()));
                    newAccesos.add(acceso);
                    addAccesos.add(acceso);
                }

                //Mantiene los accesos que voy a añadir
                addAccesos.removeAll(oldAccesos);
                System.out.println("Los que voy a añadir:" + addAccesos);

                //Mantiene los accesos que voy a eliminar (que quito del picklist)
                oldAccesos.removeAll(newAccesos);
                System.out.println("Los que voy a eliminar:" + oldAccesos);

                if (!addAccesos.isEmpty()) {
                    SPerfilesAccesos nuevoPA = new SPerfilesAccesos();
                    for (SAccesos element : addAccesos) {
                        nuevoPA.setSAccesos(element);
                        nuevoPA.setSPerfiles(selectedPerfil);
                        nuevoPA.setFechaServidor(new Date());
                        nuevoPA.setIdUsuarioModifica(TraeDatoSesion.traerIdUsuario());
                        perfilesAccesosController.create(nuevoPA);
                    }
                }

                //Cree una nueva función traerAccesosByPerfilAndAccesos para eliminar
                //directamente en la tabla S_PERFILES_ACCESOS
                if (!oldAccesos.isEmpty()) {
                    for (SAccesos element : oldAccesos) {
                        SPerfilesAccesos foundPA = perfilesAccesosController.traerAccesosByPerfilAndAccesos(selectedPerfil, element);
                        perfilesAccesosController.destroy(foundPA.getSPerfilesAccesosPK());
                    }
                }

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha modificado", selectedPerfil.getNombrePerfil());
                FacesContext.getCurrentInstance().addMessage(null, msg);

                PrimeFaces.current().executeScript("PF('dlg2').hide()");

            } catch (Exception ex) {
                Logger.getLogger(PerfilesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Buscamos el acceso-perfil segun el perfil seleccionado, y mediante su id
     * eliminamos todos sus accesos y su registro en la tabla S_PERFILES
     */
    public void eliminarPerfil() {
        SPerfilesJpaController perfilesController = new SPerfilesJpaController();

        try {
            List<SPerfilesAccesos> eliminarListaPA = accesosController.traerAccesosByPerfil(selectedPerfil);
            for (SPerfilesAccesos item : eliminarListaPA) {
                perfilesAccesosController.destroy(item.getSPerfilesAccesosPK());
            }
            perfilesController.destroy(selectedPerfil.getIdPerfil());
            llenaPerfiles();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha eliminado", selectedPerfil.getNombrePerfil());
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(PerfilesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * A partir de los id de los perfil-accesos traidos con el id del perfil
     * seleccionado creamos una lista con los accesos
     *
     * @return regresa una lista de tipo SAccesos a partir de los Accesos del
     * perfil
     */
    public List<SAccesos> accesosActuales() {
        List<SAccesos> accesos = new ArrayList<>();
        List<SPerfilesAccesos> accesosPerfil = accesosController.traerAccesosByPerfil(selectedPerfil);

        for (SPerfilesAccesos item : accesosPerfil) {
            int idAcceso = item.getSAccesos().getIdAcceso();
            SAccesos acceso = accesosController.findSAccesos(idAcceso);
            accesos.add(acceso);
        }
        return accesos;
    }

    //<editor-fold defaultstate="collapsed" desc="Gets y Sets">
    public List<SPerfiles> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(List<SPerfiles> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    public SPerfiles getPerfil() {
        return perfil;
    }

    public void setPerfil(SPerfiles perfil) {
        this.perfil = perfil;
    }

    public SPerfiles getSelectedPerfil() {
        return selectedPerfil;
    }

    public void setSelectedPerfil(SPerfiles selectedPerfil) {
        this.selectedPerfil = selectedPerfil;
    }

    public SPerfilesAccesos getPerfilAcceso() {
        return perfilAcceso;
    }

    public void setPerfilAcceso(SPerfilesAccesos perfilAcceso) {
        this.perfilAcceso = perfilAcceso;
    }

    public List<SAccesos> getSelectedAcceso() {
        return selectedAcceso;
    }

    public void setSelectedAcceso(List<SAccesos> selectedAcceso) {
        this.selectedAcceso = selectedAcceso;
    }

    public DualListModel<SAccesos> getListaAccesosDual() {
        return listaAccesosDual;
    }

    public void setListaAccesosDual(DualListModel<SAccesos> listaAccesosDual) {
        this.listaAccesosDual = listaAccesosDual;
    }

    public DualListModel<SAccesos> getAccesosDLM() {
        return accesosDLM;
    }

    public void setAccesosDLM(DualListModel<SAccesos> accesosDLM) {
        this.accesosDLM = accesosDLM;
    }

    public List<SAccesos> getListaAccesosDisponibles() {
        return listaAccesosDisponibles;
    }

    public void setListaAccesosDisponibles(List<SAccesos> listaAccesosDisponibles) {
        this.listaAccesosDisponibles = listaAccesosDisponibles;
    }

    public List<SAccesos> getListaAccesosActuales() {
        return listaAccesosActuales;
    }

    public void setListaAccesosActuales(List<SAccesos> listaAccesosActuales) {
        this.listaAccesosActuales = listaAccesosActuales;
    }
//</editor-fold>

}
