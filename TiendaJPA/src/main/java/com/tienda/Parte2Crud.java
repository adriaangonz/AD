/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda;

/**
 *
 * @author adrian
 */
import com.tienda.dao.ProductoDAO;
import com.tienda.modelo.Producto;
import com.tienda.util.JPAUtil;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Parte2Crud {

    public static void main(String[] args) {
        ProductoDAO dao = new ProductoDAO();
        System.out.println("\n=== 1. Listar todos ===");
        dao.listarTodos().forEach(System.out::println);
        System.out.println("\n=== 2. Buscar por id ===");
        System.out.println("Id 1: " + dao.buscarPorId(1L));
        System.out.println("Id 999: " + dao.buscarPorId(999L));
        System.out.println("\n=== 3. Productos de más de 50 € ===");
        dao.buscarMasCarosQue(new BigDecimal("50")).forEach(System.out::println);
        System.out.println("\n=== 4. Cambiar el precio del producto 2 (objeto gestionado) ===");
        boolean ok = dao.actualizarPrecio(2L, new BigDecimal("17.50"));
        System.out.println("Actualizado: " + ok + " -> " + dao.buscarPorId(2L));
        System.out.println("\n=== 5. Modificar un objeto separado (detached) ===");
        Producto p = dao.buscarPorId(1L); // el EntityManager ya está cerrado: p está separado
        p.setStock(0);
        System.out.println("En memoria: " + p);
        System.out.println("En la BD: " + dao.buscarPorId(1L));
        dao.actualizar(p); // merge() copia los cambios a la BD
        System.out.println("Tras merge: " + dao.buscarPorId(1L));
        System.out.println("\n=== 6. Insertar y eliminar ===");
        Producto temporal = new Producto("Alfombrilla", new BigDecimal("6.99"), 100, LocalDate.now());
        dao.insertar(temporal);
        System.out.println("Insertado con id " + temporal.getId());
        System.out.println("Eliminado: " + dao.eliminar(temporal.getId()));
        System.out.println("Eliminar id 999: " + dao.eliminar(999L));
        System.out.println("\n=== 7. Estado final ===");
        dao.listarTodos().forEach(System.out::println);
        JPAUtil.cerrar();
    }
}
