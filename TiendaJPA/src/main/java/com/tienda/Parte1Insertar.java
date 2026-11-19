/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda;

import com.tienda.modelo.Producto;
import com.tienda.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author adrian
 */

public class Parte1Insertar {

    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();
        Producto p1 = new Producto("Teclado mecánico", new BigDecimal("59.90"), 25, LocalDate.of(2026,
                9, 15));
        Producto p2 = new Producto("Ratón inalámbrico", new BigDecimal("19.95"), 40, LocalDate.of(2026,
                10, 1));
        Producto p3 = new Producto("Monitor 27 pulgadas", new BigDecimal("229.00"), 8,
                LocalDate.now());
        System.out.println("Antes de persist, id de p1 = " + p1.getId());
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.getTransaction().commit();
        System.out.println("Después de persist, id de p1 = " + p1.getId());
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        em.close();
        JPAUtil.cerrar();
    }

}
