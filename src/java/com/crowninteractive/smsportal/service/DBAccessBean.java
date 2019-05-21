package com.crowninteractive.smsportal.service;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

public class DBAccessBean {

    private static final Logger L = Logger.getLogger(DBAccessBean.class);
    //private EntityManager em;
    private EntityManagerFactory emf;
    private EntityTransaction transaction;

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(EntityTransaction transaction) {
        this.transaction = transaction;
    }

    private DBAccessBean() {
    }

    public DBAccessBean(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
//        em = emf.createEntityManager();
//        transaction = em.getTransaction();
    }

    public void create(Object t) {
        EntityManager em = emf.createEntityManager();
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.persist(t);
            em.flush();
            em.getTransaction().commit();
            em.getEntityManagerFactory().getCache().evictAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public <T> T merge(T t) {
        EntityManager em = emf.createEntityManager();
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            T merge = em.merge(t);
            em.flush();
            em.getTransaction().commit();
            em.getEntityManagerFactory().getCache().evictAll();
            return merge;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public <T> void delete(T t) {
        EntityManager em = emf.createEntityManager();
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.remove(t);
            em.flush();
            em.getTransaction().commit();
            em.getEntityManagerFactory().getCache().evictAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private <T> void unsafeMerge(T t) {
        EntityManager em = emf.createEntityManager();
        em.merge(t);
        em.close();
    }

    public <T> void createMany(T... t) {
        EntityManager em = emf.createEntityManager();
        try {

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            for (T object : t) {
                em.persist(object);
            }
            em.flush();
            em.clear();
            em.getTransaction().commit();
            em.getEntityManagerFactory().getCache().evictAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public <T> void mergeMany(T... t) {
        EntityManager em = emf.createEntityManager();
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            for (T object : t) {
                em.merge(object);
            }
            em.flush();
            em.clear();
            em.getTransaction().commit();
            em.getEntityManagerFactory().getCache().evictAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public <T> T get(Class<T> tClass, Object t) {
        EntityManager em = emf.createEntityManager();
        try {
            return (T) em.find(tClass, t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> T findSingle(Class<T> clazz, Object t)
            throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            T find = em.find(clazz, t);
            if (find == null) {
                throw new EntityNotFoundException("No " + clazz.getSimpleName() + " found by id - " + t);
            }
            return find;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> T getReference(Class<T> tClass, Object t)
            throws EntityNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            T find = em.getReference(tClass, t);
            if (find == null) {
                throw new EntityNotFoundException("No " + tClass.getSimpleName() + " found by id - " + t);
            }
            return find;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> List<T> findAll(Class<T> tClass, int page, int size) {
        EntityManager em = emf.createEntityManager();
        try {
            int start = page * size;
            return em.createQuery("SELECT t FROM " + tClass.getSimpleName() + " t", tClass)
                    .setFirstResult(start * page).setMaxResults(size).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> List<T> findAll(Class<T> tClass) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM " + tClass.getSimpleName() + " t", tClass).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Integer findCount(Class tClass) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM " + tClass.getSimpleName() + " t", tClass).getResultList().size();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> TypedQuery<T> namedQuery(String namedQuery, Class<T> tClass) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery(namedQuery, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> TypedQuery<T> createQuery(String query, Class<T> tClass) {
        EntityManager em = emf.createEntityManager();
        try {
            return emf.createEntityManager().createQuery(query, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> Query createNativeQuery(String nativeQuery, Class<T> tClass) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNativeQuery(nativeQuery, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> Query createNativeQuery(String nativeQuery) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNativeQuery(nativeQuery);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Query createQuery(String query) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> TypedQuery<T> createNamedQuery(String query, Class<T> clazz) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery(query, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Query createQuery(String query, int page, int size) {
        EntityManager em = emf.createEntityManager();
        try {
            int start = (page - 1) * size;
            return em.createQuery(query).setFirstResult(start).setMaxResults(size);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> findAll(String query, int page, int size) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = createQuery(query, page, size);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> List<T> findAll(String query) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Query createQuery(String query, Map<String, Object> parameters) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createQuery(query);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String string = (String) entry.getKey();
                Object object = entry.getValue();
                q.setParameter(string, object);
            }
            return q;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> findAll(String query, Map<String, Object> parameters) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = createQuery(query, parameters);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> List<T> findAllWithSingleParameters(Class<T> tClass, String query, String key, Object value) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = createQuery(query, tClass);
            q.setParameter(key, value);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Object findSingleWithSingleParameters(String query, String key, Object value) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = createQuery(query);
            q.setParameter(key, value);
            return q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Long getCount(Class clazz) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Object> rt = cq.from(clazz);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return (Long) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public CriteriaBuilder getCriteriaBuilder() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.getCriteriaBuilder();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> cq) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(cq);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
