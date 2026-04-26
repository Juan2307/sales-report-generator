package com.politecnico.ventas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	private static final String INPUT_FOLDER = "data/";

	public static void main(String[] args) {

		try {

			ArrayList<Producto> productos = cargarProductos();
			ArrayList<Vendedor> vendedores = cargarVendedores();
			ArrayList<Venta> ventas = cargarVentas(productos, vendedores);

			Map<Long, Double> ventasPorVendedor = calcularVentasPorVendedor(ventas);
			Map<Integer, Integer> productosVendidos = calcularProductosVendidos(ventas);

			generarReporteVendedores(ventasPorVendedor, vendedores);
			generarReporteProductos(productosVendidos, productos);

			System.out.println("Reportes generados correctamente.");

		} catch (Exception e) {

			System.out.println("Error en ejecución: " + e.getMessage());

		}

	}

	// =========================
	// CARGA DE ARCHIVOS
	// =========================

	private static ArrayList<Producto> cargarProductos() throws Exception {

		ArrayList<Producto> lista = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(INPUT_FOLDER + "productos.txt"));

		String linea;

		while ((linea = reader.readLine()) != null) {

			String[] partes = linea.split(";");

			lista.add(new Producto(Integer.parseInt(partes[0]), partes[1], Integer.parseInt(partes[2])));

		}

		reader.close();
		return lista;

	}

	private static ArrayList<Vendedor> cargarVendedores() throws Exception {

		ArrayList<Vendedor> lista = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(INPUT_FOLDER + "vendedores.txt"));

		String linea;

		while ((linea = reader.readLine()) != null) {

			String[] partes = linea.split(";");

			lista.add(new Vendedor(Long.parseLong(partes[1]), partes[2] + " " + partes[3]));

		}

		reader.close();
		return lista;

	}

	private static ArrayList<Venta> cargarVentas(ArrayList<Producto> productos, ArrayList<Vendedor> vendedores)
			throws Exception {

		ArrayList<Venta> lista = new ArrayList<>();
		File folder = new File(INPUT_FOLDER);

		for (File file : folder.listFiles()) {

			if (file.getName().startsWith("ventas_")) {

				BufferedReader reader = new BufferedReader(new FileReader(file));

				String linea = reader.readLine();
				long idVendedor = Long.parseLong(linea.split(";")[1]);

				Vendedor vendedor = buscarVendedor(vendedores, idVendedor);

				while ((linea = reader.readLine()) != null) {

					String[] partes = linea.split(";");

					Producto producto = buscarProducto(productos, Integer.parseInt(partes[0]));

					if (producto != null && vendedor != null)
						lista.add(new Venta(vendedor, producto, Integer.parseInt(partes[1])));

				}

				reader.close();

			}

		}

		return lista;

	}

	// =========================
	// PROCESAMIENTO
	// =========================

	private static Map<Long, Double> calcularVentasPorVendedor(ArrayList<Venta> ventas) {

		Map<Long, Double> totales = new HashMap<>();

		for (Venta v : ventas) {

			double total = v.cantidad * v.producto.precio;

			totales.put(v.vendedor.id, totales.getOrDefault(v.vendedor.id, 0.0) + total);

		}

		return totales;

	}

	private static Map<Integer, Integer> calcularProductosVendidos(ArrayList<Venta> ventas) {

		Map<Integer, Integer> cantidades = new HashMap<>();

		for (Venta v : ventas) {

			cantidades.put(v.producto.id, cantidades.getOrDefault(v.producto.id, 0) + v.cantidad);

		}

		return cantidades;

	}

	// =========================
	// GENERACIÓN DE REPORTES
	// =========================

	private static void generarReporteVendedores(Map<Long, Double> ventas, ArrayList<Vendedor> vendedores)
			throws Exception {

		List<Map.Entry<Long, Double>> lista = new ArrayList<>(ventas.entrySet());

		// Ordenar de mayor a menor
		lista.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

		BufferedWriter writer = new BufferedWriter(new FileWriter(INPUT_FOLDER + "reporte_vendedores.txt"));

		for (Map.Entry<Long, Double> entry : lista) {

			Vendedor v = buscarVendedor(vendedores, entry.getKey());

			writer.write(v.nombre + ";" + entry.getValue());
			writer.newLine();

		}

		writer.close();

	}

	private static void generarReporteProductos(Map<Integer, Integer> productosVendidos, ArrayList<Producto> productos)
			throws Exception {

		List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(productosVendidos.entrySet());

		// Ordenar por cantidad (descendente)
		lista.sort((a, b) -> b.getValue() - a.getValue());

		BufferedWriter writer = new BufferedWriter(new FileWriter(INPUT_FOLDER + "reporte_productos.txt"));

		for (Map.Entry<Integer, Integer> entry : lista) {

			Producto p = buscarProducto(productos, entry.getKey());

			writer.write(p.nombre + ";" + p.precio);
			writer.newLine();

		}

		writer.close();

	}

	// =========================
	// MÉTODOS AUXILIARES
	// =========================

	private static Producto buscarProducto(ArrayList<Producto> lista, int id) {

		for (Producto p : lista) {

			if (p.id == id)
				return p;

		}

		return null;

	}

	private static Vendedor buscarVendedor(ArrayList<Vendedor> lista, long id) {

		for (Vendedor v : lista) {

			if (v.id == id)
				return v;

		}

		return null;

	}

}