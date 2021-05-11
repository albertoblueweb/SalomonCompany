
package catalogos;

import controllers.SAccesosJpaController;
import controllers.SPerfilesAccesosJpaController;
import controllers.SPerfilesJpaController;
import entidades.SAccesos;
import entidades.SPerfiles;
import entidades.SPerfilesAccesos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Alberto Salomón 
 * Fecha: 2021-05-07
 */
@ManagedBean
@SessionScoped
public class PerfilesBean implements Serializable {
    private List<SPerfiles> listaPerfiles;
    private SPerfiles perfil = new SPerfiles();
    private SPerfiles selectedPerfil;
    private SAccesos selectedAcceso;
    private DualListModel<SAccesos> listaAccesosDual;
    List<SAccesos> AccesosTarget = new ArrayList<>();
    private DualListModel<SAccesos> accesosDLM;
    private List<SAccesos> listaAccesosDisponibles;
    private List<SAccesos> listaAccesosActuales;

         
    public PerfilesBean() {
        llenaPerfiles();
    }
    
    @PostConstruct
    public void init() {
        //Cities
        List<SAccesos> AccesosSource = new ArrayList<>();
        
         
        SAccesosJpaController accesosController = new SAccesosJpaController();
        AccesosSource = accesosController.findSAccesosEntities();
         
        listaAccesosDual = new DualListModel<>(AccesosSource, AccesosTarget);
                         
    }
    
    public void guardaPerfil(){
    }
    
    public void onTransfer(TransferEvent event) {

        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    

         
    public final void llenaPerfiles() {
        try {
            SPerfilesJpaController perfilesController = new SPerfilesJpaController();
            listaPerfiles = perfilesController.findSPerfilesEntities();

        } catch (Exception ex) {
            Logger.getLogger(DistribuidorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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

    public SAccesos getSelectedAcceso() {
        return selectedAcceso;
    }

    public void setSelectedAcceso(SAccesos selectedAcceso) {
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

    
    
    
    
}
