package DAM.simulacroExamen;

import java.time.LocalDateTime;

public class Eventos {

	private String nombre;
	private LocalDateTime fecha;
	private String ubicacion;
	private String descripcion;

	public Eventos() {
	}

	public Eventos(String nombre, LocalDateTime fecha, String ubicacion, String descripcion) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.ubicacion = ubicacion;
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Evento{" + "nombre='" + nombre + '\'' + ", fecha=" + fecha + ", ubicacion='" + ubicacion + '\''
				+ ", descripcion='" + descripcion + '\'' + '}';
	}
}
