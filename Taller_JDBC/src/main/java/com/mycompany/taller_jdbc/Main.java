/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.taller_jdbc;

import java.util.Scanner;

/**
 *
 * @author adrian
 */
public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        TallerDAO dao = new TallerDAO();
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            try {
                int opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1 -> ejecutarOpcion1(teclado, dao);
                    case 2 -> ejecutarOpcion2(teclado, dao);
                    case 3 -> ejecutarOpcion3(dao);
                    case 4 -> ejecutarOpcion4(teclado, dao);
                    case 5 -> ejecutarOpcion5(teclado, dao);
                    case 6 -> ejecutarOpcion6(dao);
                    case 7 -> ejecutarOpcion7(teclado, dao);
                    case 8 -> ejecutarOpcion8(teclado, dao);
                    case 9 -> ejecutarOpcion9(teclado, dao);
                    case 10 -> ejecutarOpcion10(teclado, dao);
                    case 11 -> ejecutarOpcion11(teclado, dao);
                    case 0 -> {
                        salir = true;
                        System.out.println("Saliendo");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduce un numero entero");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("1. Alta de cliente");
        System.out.println("2. Registrar reparación");
        System.out.println("3. Listar reparaciones (con cliente y control de nulos)");
        System.out.println("4. Actualizar coste / fecha de salida de reparación");
        System.out.println("5. Eliminar cliente");
        System.out.println("6. Resumen de facturación por cliente");
        System.out.println("7. Importar lote de reparaciones (Batch + Transacción)");
        System.out.println("8. Traspasar reparaciones y dar de baja cliente (Transacción)");
        System.out.println("9. Finalizar reparación (Procedimiento IN)");
        System.out.println("10. Estadísticas de cliente (Procedimiento OUT)");
        System.out.println("11. Aplicar descuento de bono (Procedimiento INOUT)");
        System.out.println("0. SAlir");
        System.out.println("Elige opción a realizar...");
    }

    private static void ejecutarOpcion1(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion2(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion3(TallerDAO dao) {
        
    }

    private static void ejecutarOpcion4(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion5(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion6(TallerDAO dao) {
        
    }

    private static void ejecutarOpcion7(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion8(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion9(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion10(Scanner teclado, TallerDAO dao) {
        
    }

    private static void ejecutarOpcion11(Scanner teclado, TallerDAO dao) {
        
    }
}
