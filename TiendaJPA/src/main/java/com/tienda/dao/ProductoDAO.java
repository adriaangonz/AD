/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.modelo.Producto;
import com.tienda.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author adrian
 */
public class ProductoDAO {

    /**
     * CREATE
     */
    public void insertar(Producto p) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * READ por clave primaria. Devuelve null si no existe.
     */
    public Producto buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }

    }

    /**
     * READ de todos, con JPQL.
     */
    public List<Producto> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p ORDER BY p.nombre", Producto.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * READ con parámetro: productos más caros que un precio dado.
     */
    public List<Producto> buscarMasCarosQue(BigDecimal precioMinimo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Producto p WHERE p.precio > :min ORDER BY p.precio DESC",
                    Producto.class)
                    .setParameter("min", precioMinimo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * UPDATE de un objeto gestionado: basta con modificarlo dentro de la
     * transacción.
     */
    public boolean actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Producto p = em.find(Producto.class, id);
            if (p == null) {
                em.getTransaction().rollback();
                return false;
            }
            p.setPrecio(nuevoPrecio);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * UPDATE de un objeto separado: hay que usar merge().
     */
    public Producto actualizar(Producto p) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Producto gestionado = em.merge(p);
            em.getTransaction().commit();
            return gestionado;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * DELETE. remove() solo admite objetos gestionados: por eso primero find().
     */
    public boolean eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            em.getTransaction().begin();
            Producto p = em.find(Producto.class, id);
            if (p == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(p);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Producto> buscarPorNombres(String texto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(:nombre) ORDER BY p.nombre", Producto.class)
                    .setParameter("nombre", "%" + texto + "%")
                    .getResultList();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public long contar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Producto p")
                    .getFirstResult();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
