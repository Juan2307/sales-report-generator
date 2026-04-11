package com.politecnico.ventas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String INPUT_FOLDER = "data/";

    public static void main(String[] args) {

        try {

            ArrayList<Producto> productos = cargarProductos();
            ArrayList<Vendedor> vendedores = cargarVendedores();
            ArrayList<Venta> ventas = cargarVentas(productos, vendedores);

            calcularVentasPorVendedor(ventas);

            System.out.println("\nProceso finalizado correctamente.");

        } catch (Exception e) {
            System.out.println("Error en ejecución: " + e.getMessage());
        }
    }

    /**
     * Carga los productos desde el archivo productos.txt
     */
    private static ArrayList<Producto> cargarProductos() throws Exception {

        ArrayList<Producto> lista = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new FileReader(INPUT_FOLDER + "productos.txt"));

        String linea;

        while ((linea = reader.readLine()) != null) {

            String[] partes = linea.split(";");

            int id = Integer.parseInt(partes[0]);
            String nombre = partes[1];
            int precio = Integer.parseInt(partes[2]);

            lista.add(new Producto(id, nombre, precio));
        }

        reader.close();
        return lista;
    }

    /**
     * Carga los vendedores desde el archivo vendedores.txt
     */
    private static ArrayList<Vendedor> cargarVendedores() throws Exception {

        ArrayList<Vendedor> lista = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new FileReader(INPUT_FOLDER + "vendedores.txt"));

        String linea;

        while ((linea = reader.readLine()) != null) {

            String[] partes = linea.split(";");

            long id = Long.parseLong(partes[1]);
            String nombre = partes[2] + " " + partes[3];

            lista.add(new Vendedor(id, nombre));
        }

        reader.close();
        return lista;
    }

    /**
     * Carga todas las ventas leyendo los archivos ventas_X.txt
     */
    private static ArrayList<Venta> cargarVentas(ArrayList<Producto> productos,
                                                 ArrayList<Vendedor> vendedores) throws Exception {

        ArrayList<Venta> lista = new ArrayList<>();

        File folder = new File(INPUT_FOLDER);

        for (File file : folder.listFiles()) {

            if (file.getName().startsWith("ventas_")) {

                BufferedReader reader = new BufferedReader(
                        new FileReader(file));

                String linea;

                // Leer encabezado (CC;id)
                linea = reader.readLine();
                String[] encabezado = linea.split(";");
                long idVendedor = Long.parseLong(encabezado[1]);

                Vendedor vendedor = buscarVendedor(vendedores, idVendedor);

                // Leer ventas
                while ((linea = reader.readLine()) != null) {

                    String[] partes = linea.split(";");

                    int idProducto = Integer.parseInt(partes[0]);
                    int cantidad = Integer.parseInt(partes[1]);

                    Producto producto = buscarProducto(productos, idProducto);

                    if (producto != null && vendedor != null) {
                        lista.add(new Venta(vendedor, producto, cantidad));
                    }
                }

                reader.close();
            }
        }

        return lista;
    }

    /**
     * Calcula e imprime el total vendido por cada vendedor
     */
    private static void calcularVentasPorVendedor(ArrayList<Venta> ventas) {

        Map<Long, Double> totales = new HashMap<>();

        for (Venta v : ventas) {

            double totalVenta = v.cantidad * v.producto.precio;

            totales.put(
                v.vendedor.id,
                totales.getOrDefault(v.vendedor.id, 0.0) + totalVenta
            );
        }

        System.out.println("\n--- TOTAL DE VENTAS POR VENDEDOR ---");

        for (Map.Entry<Long, Double> entry : totales.entrySet()) {
            System.out.println("Vendedor ID " + entry.getKey() +
                    " -> $" + entry.getValue());
        }
    }

    private static Producto buscarProducto(ArrayList<Producto> lista, int id) {

        for (Producto p : lista) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }

    private static Vendedor buscarVendedor(ArrayList<Vendedor> lista, long id) {

        for (Vendedor v : lista) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
    }

}