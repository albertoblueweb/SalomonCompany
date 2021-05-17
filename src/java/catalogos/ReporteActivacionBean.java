/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catalogos;

import controllers.HActivacionJpaController;
import entidades.HActivacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alberto Salomón Fecha: 2021-05-17
 */
@ManagedBean
@SessionScoped
public class ReporteActivacionBean implements Serializable {

    private List<HActivacion> listaActivacion;
    private Date fechaInicial;
    private Date fechaFinal;
    private List<Date> invalidDates;
    private List<Integer> invalidDays;
    private Date minDate;
    private Date maxDate;
    private Date minTime;
    private Date maxTime;
    private Date minDateTime;
    private Date maxDateTime;
    HActivacionJpaController activacionController = new HActivacionJpaController();

    public ReporteActivacionBean() {
        this.listaActivacion = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        
    }
    /**
     * Mediante el controller de activacion, utilza la función traerListaPeticion, 
     * para regresar los datos de la misma que se encuentren entre dos fechas
     */
    public final void llenaReporteActivacion() {
        try {
            if (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {
                Calendar tmp = Calendar.getInstance();
                tmp.setTime(fechaFinal);
                tmp.add(Calendar.DAY_OF_MONTH, 1);
                fechaFinal = tmp.getTime();

                listaActivacion = activacionController.traerListaPeticion(fechaInicial, fechaFinal);
                FacesMessage msg;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Consulta exitosa",
                "Consulta exitosa");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de consulta",
                "Fecha inicial mayor que fecha final");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReporteActivacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<HActivacion> getListaActivacion() {
        return listaActivacion;
    }

    public void setListaActivacion(List<HActivacion> listaActivacion) {
        this.listaActivacion = listaActivacion;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Date getMinDateTime() {
        return minDateTime;
    }

    public void setMinDateTime(Date minDateTime) {
        this.minDateTime = minDateTime;
    }

    public Date getMaxDateTime() {
        return maxDateTime;
    }

    public void setMaxDateTime(Date maxDateTime) {
        this.maxDateTime = maxDateTime;
    }

}
