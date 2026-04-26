package com.politecnico.ventas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase encargada de generar archivos de prueba para el sistema de ventas.
 * 
 * Genera: - Archivo de productos (productos.txt) - Archivo de vendedores
 * (vendedores.txt) - Archivos de ventas individuales por vendedor
 * (ventas_X.txt)
 * 
 * Los archivos se almacenan en la carpeta "data/" dentro del proyecto.
 * 
 * Estos archivos serán utilizados como entrada para la segunda parte del
 * proyecto.
 * 
 * @author SubGrupo 1 - CONCEPTOS FUNDAMENTALES DE PROGRAMACIÓN
 * @version 1.1
 * @since 2026
 */
public class GenerateInfoFiles {

	/** Generador de números aleatorios para datos de prueba */
	private static final Random random = new Random();

	/** Carpeta donde se almacenarán los archivos generados */
	private static final String OUTPUT_FOLDER = "data/";

	/**
	 * Método principal que ejecuta la generación de todos los archivos necesarios.
	 * 
	 * Se encarga de: - Crear la carpeta de salida si no existe - Generar productos
	 * - Generar vendedores - Generar ventas por vendedor
	 * 
	 * @param args argumentos de línea de comandos (no utilizados)
	 */
	public static void main(String[] args) {

		try {

			// Crear carpeta si no existe
			createOutputDirectory();

			// Genera archivo de productos
			createProductsFile(10);

			// Genera archivo de vendedores
			createSalesManInfoFile(5);

			// Genera archivos de ventas para cada vendedor
			for (int i = 1; i <= 5; i++)
				createSalesMenFile(10, i);

		} catch (Exception e) {

			System.out.println("Error al generar archivos: " + e.getMessage());

		}

	}

	/**
	 * Crea la carpeta de salida si no existe e imprime su ruta absoluta.
	 */
	private static void createOutputDirectory() {

		File folder = new File(OUTPUT_FOLDER);

		if (!folder.exists())
			folder.mkdirs();

		// Imprimir ruta absoluta
		System.out.println("Archivos generados en: " + folder.getAbsolutePath());

	}

	/**
	 * Genera un archivo de productos con información pseudoaleatoria.
	 * 
	 * Formato: IDProducto;NombreProducto;PrecioProducto
	 * 
	 * @param productsCount cantidad de productos a generar
	 * @throws IOException si ocurre un error de escritura
	 */
	public static void createProductsFile(int productsCount) throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FOLDER + "productos.txt"))) {

			for (int i = 1; i <= productsCount; i++) {

				String line = i + ";Producto" + i + ";" + (1000 + random.nextInt(100));
				writer.write(line);
				writer.newLine();

			}

		}

	}

	/**
	 * Genera un archivo con la información de los vendedores.
	 * 
	 * Formato: TipoDocumento;NumeroDocumento;Nombre;Apellido
	 * 
	 * @param salesmanCount cantidad de vendedores a generar
	 * @throws IOException si ocurre un error de escritura
	 */
	public static void createSalesManInfoFile(int salesmanCount) throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FOLDER + "vendedores.txt"))) {

			String[] nombres = { "JUAN ANDRES", "DUVANT", "MARIA SUSANA", "CHRISTIAN DAVID", "DIEGO" };

			String[] apellidos = { "LEANDRO RODRIGUEZ", "FANDIÑO REYES", "OSPINA VANEGAS", "SANDOVAL PERALTA", "ROA" };

			for (int i = 0; i < salesmanCount && i < nombres.length && i < apellidos.length; i++) {

				String line = "CC;" + (i + 1) + ";" + nombres[i] + ";" + apellidos[i];
				writer.write(line);
				writer.newLine();

			}

		}

	}

	/**
	 * Genera un archivo de ventas para un vendedor específico.
	 * 
	 * Nombre del archivo: ventas_ID.txt
	 * 
	 * Formato: Línea 1: CC;NumeroDocumento
	 * 
	 * Líneas siguientes: IDProducto;Cantidad;
	 * 
	 * @param randomSalesCount cantidad de ventas a generar
	 * @param id               número de documento del vendedor
	 * @throws IOException si ocurre un error de escritura
	 */
	public static void createSalesMenFile(int randomSalesCount, long id) throws IOException {

		String fileName = OUTPUT_FOLDER + "ventas_" + id + ".txt";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

			// Encabezado
			writer.write("CC;" + id);
			writer.newLine();

			// Ventas
			for (int i = 0; i < randomSalesCount; i++) {

				int productId = 1 + random.nextInt(10);
				int cantidad = 1 + random.nextInt(5);

				writer.write(productId + ";" + cantidad + ";");
				writer.newLine();

			}

		}

	}

}