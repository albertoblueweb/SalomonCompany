/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import entidades.SAplicaciones;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import utils.LocalEntityManagerFactory;
import utils.TraeDatoSesion;

/**
 *
 * @author Blueweb
 */
public class SAplicacionesJpaController implements Serializable {
    
    List<SAplicaciones> lista;

    public SAplicacionesJpaController() {
        this.emf = LocalEntityManagerFactory.getEntityManagerFactory();
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SAplicaciones SAplicaciones) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(SAplicaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SAplicaciones SAplicaciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SAplicaciones = em.merge(SAplicaciones);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = SAplicaciones.getIdAplicacion();
                if (findSAplicaciones(id) == null) {
                    throw new NonexistentEntityException("The sAplicaciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SAplicaciones SAplicaciones;
            try {
                SAplicaciones = em.getReference(SAplicaciones.class, id);
                SAplicaciones.getIdAplicacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The SAplicaciones with id " + id + " no longer exists.", enfe);
            }
            em.remove(SAplicaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SAplicaciones> findSAplicacionesEntities() {
        return findSAplicacionesEntities(true, -1, -1);
    }

    public List<SAplicaciones> findSAplicacionesEntities(int maxResults, int firstResult) {
        return findSAplicacionesEntities(false, maxResults, firstResult);
    }

    private List<SAplicaciones> findSAplicacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SAplicaciones.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public SAplicaciones findSAplicaciones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SAplicaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getSAplicacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SAplicaciones> rt = cq.from(SAplicaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<SAplicaciones> traerAplicacion() {
        lista = new ArrayList<>();
        List<Object[]> resultList= new ArrayList<>(); 
        EntityManager em = getEntityManager();
        try {
            em = getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("stp_CargaMenu")
                    .registerStoredProcedureParameter(1, String.class,
                            ParameterMode.IN)
                    .setParameter(1, TraeDatoSesion.traerUsuario());

            query.execute();

            resultList = query.getResultList();
            for (Object[] item : resultList){
                SAplicaciones aplicacion = new SAplicaciones();
                aplicacion.setIdAplicacion(Integer.parseInt(item[0].toString()));
                aplicacion.setNombreAplicacion(item[1].toString());
                aplicacion.setIcono(item[2].toString());
                aplicacion.setUrl(item[3].toString());
                aplicacion.setOrden(Short.valueOf(item[4].toString()));
                lista.add(aplicacion);
            }
            
        } catch (Exception ex) {

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;

    }
}
