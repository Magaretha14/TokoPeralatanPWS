/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.a.TokoPeralatan;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.a.TokoPeralatan.exceptions.NonexistentEntityException;
import ws.a.TokoPeralatan.exceptions.PreexistingEntityException;

/**
 *
 * @author ASUS
 */
public class DaftarbarangJpaController implements Serializable {

    public DaftarbarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ws.a_TokoPeralatan_jar_0.0.1-SNAPSHOTPU");
    
    public DaftarbarangJpaController(){
        
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Daftarbarang daftarbarang) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(daftarbarang);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDaftarbarang(daftarbarang.getId()) != null) {
                throw new PreexistingEntityException("Daftarbarang " + daftarbarang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Daftarbarang daftarbarang) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            daftarbarang = em.merge(daftarbarang);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = daftarbarang.getId();
                if (findDaftarbarang(id) == null) {
                    throw new NonexistentEntityException("The daftarbarang with id " + id + " no longer exists.");
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
            Daftarbarang daftarbarang;
            try {
                daftarbarang = em.getReference(Daftarbarang.class, id);
                daftarbarang.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The daftarbarang with id " + id + " no longer exists.", enfe);
            }
            em.remove(daftarbarang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Daftarbarang> findDaftarbarangEntities() {
        return findDaftarbarangEntities(true, -1, -1);
    }

    public List<Daftarbarang> findDaftarbarangEntities(int maxResults, int firstResult) {
        return findDaftarbarangEntities(false, maxResults, firstResult);
    }

    private List<Daftarbarang> findDaftarbarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Daftarbarang.class));
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

    public Daftarbarang findDaftarbarang(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Daftarbarang.class, id);
        } finally {
            em.close();
        }
    }

    public int getDaftarbarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Daftarbarang> rt = cq.from(Daftarbarang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
