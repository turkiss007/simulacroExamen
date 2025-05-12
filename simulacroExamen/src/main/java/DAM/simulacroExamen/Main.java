package DAM.simulacroExamen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Configurar el logger
		try {
			FileHandler fh = new FileHandler("src/main/resources/aplicacion.log", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setUseParentHandlers(true);
		} catch (IOException e) {
			System.out.println("No se pudo configurar el logger: " + e.getMessage());
		}

		ArrayList<Eventos> listaEventos = new ArrayList<>();

		// Leer archivo eventos.txt
		try (BufferedReader bufferReader = new BufferedReader(new FileReader("src/main/resources/eventos.txt"))) {
			String linea;
			while ((linea = bufferReader.readLine()) != null) {
				String[] partes = linea.split(",", 4);
				if (partes.length == 4) {
					String nombre = partes[0];
					LocalDateTime fecha = LocalDateTime.parse(partes[1]);
					String ubicacion = partes[2];
					String descripcion = partes[3];
					listaEventos.add(new Eventos(nombre, fecha, ubicacion, descripcion));
				}
			}

			logger.info("Archivo 'eventos.txt' leído correctamente.");

		} catch (IOException e) {
			System.out.println("Error leyendo el archivo" + e.getMessage());
		}

		Eventos evento1 = new Eventos();
		evento1.setNombre("Concierto Twenty One Pilots");
		evento1.setFecha(LocalDateTime.of(2025, 4, 21, 21, 00));
		evento1.setUbicacion("Madrid");
		evento1.setDescripcion("Clancy Tour");

		Eventos evento2 = new Eventos();
		evento2.setNombre("Concierto Billie Eilish");
		evento2.setFecha(LocalDateTime.of(2025, 6, 14, 21, 00));
		evento2.setUbicacion("Barcelona");
		evento2.setDescripcion("Hard and Soft Tour");

		Eventos evento3 = new Eventos();
		evento3.setNombre("Concierto Fito & Los Fitipaldis");
		evento3.setFecha(LocalDateTime.of(2025, 12, 29, 20, 30));
		evento3.setUbicacion("Madrid");
		evento3.setDescripcion("Rock & Roll Tour");

		listaEventos.add(evento1);
		listaEventos.add(evento2);
		listaEventos.add(evento3);

		// System.out.println(listaEventos.toString());

		// Guardar todos los datos en salida_eventos.txt y crearlo
		try (BufferedWriter bufferWriter = new BufferedWriter(
				new FileWriter("src/main/resources/salida_eventos.txt"))) {
			for (Eventos e : listaEventos) {
				bufferWriter
						.write(e.getNombre() + "," + e.getFecha() + "," + e.getUbicacion() + "," + e.getDescripcion());
				bufferWriter.newLine();
			}
			System.out.println("Archivo 'salida_eventos.txt' generado correctamente.");
			logger.info("Archivo 'salida_eventos.txt' generado correctamente.");
		} catch (IOException e) {
			System.out.println("Error escribiendo el archivo: " + e.getMessage());
		}

		// Crear excell guardar los datos en tablas
		// Crear libro
		XSSFWorkbook libro = new XSSFWorkbook();

		// Crear hoja
		XSSFSheet hoja = libro.createSheet("eventos");

		// Crear filas y celdas

		XSSFRow fila0 = hoja.createRow(0);
		fila0.createCell(0).setCellValue("Nombre");
		fila0.createCell(1).setCellValue("Fecha");
		fila0.createCell(2).setCellValue("Ubicación");
		fila0.createCell(3).setCellValue("Descripción");

		int numFilas = 1;
		for (Eventos e : listaEventos) {
			XSSFRow fila = hoja.createRow(numFilas++);
			fila.createCell(0).setCellValue(e.getNombre());
			fila.createCell(1).setCellValue(e.getFecha().toString());
			fila.createCell(2).setCellValue(e.getUbicacion());
			fila.createCell(3).setCellValue(e.getDescripcion());
		}

		try (FileOutputStream out = new FileOutputStream("src/main/resources/eventos.xlsx")) {
			libro.write(out);
			libro.close();
			System.out.println("Excel 'eventos.xlsx' generado correctamente.");
			logger.info("Archivo 'eventos.xlsx' generado correctamente.");
		} catch (IOException e) {
			System.out.println("Error escribiendo el Excel: " + e.getMessage());
		}

		try {
			String ruta = "src/main/resources/resumen_eventos.pdf";
			PdfWriter PDFwriter = new PdfWriter(ruta);
			PdfDocument pdf = new PdfDocument(PDFwriter);
			Document document = new Document(pdf);

			// Título
			Paragraph titulo = new Paragraph("Resumen de Eventos").setFontSize(20).setBold()
					.setFontColor(ColorConstants.BLUE).setTextAlignment(TextAlignment.CENTER).setMarginBottom(20);
			document.add(titulo);

			// Eventos
			for (Eventos e : listaEventos) {
				String eventoTexto = "Nombre: " + e.getNombre() + "\n" + "Fecha: " + e.getFecha().toString() + "\n"
						+ "Ubicación: " + e.getUbicacion() + "\n" + "Descripción: " + e.getDescripcion() + "\n\n";

				Paragraph parrafoEvento = new Paragraph(eventoTexto).setFontSize(12).setMarginBottom(10);
				document.add(parrafoEvento);
			}

			document.close();
			System.out.println("PDF 'resumen_eventos.pdf' generado correctamente.");
			logger.info("Archivo 'resumen_eventos.pdf' generado correctamente.");

		} catch (IOException e) {
			System.out.println("Error generando el PDF: " + e.getMessage());
		}

		// Obtener y mostrar eventos futuros
		System.out.println("\nEventos futuros:");
		ArrayList<Eventos> eventosFuturos = obtenerEventosFuturos(listaEventos);
		for (Eventos e : eventosFuturos) {
			System.out.println(e.getNombre() + " - " + e.getFecha());
		}
	}

	public static ArrayList<Eventos> obtenerEventosFuturos(ArrayList<Eventos> lista) {
		ArrayList<Eventos> futuros = new ArrayList<>();
		LocalDateTime ahora = LocalDateTime.now();
		for (Eventos e : lista) {
			if (e.getFecha().isAfter(ahora)) {
				futuros.add(e);
			}
		}
		return futuros;
	}

}
