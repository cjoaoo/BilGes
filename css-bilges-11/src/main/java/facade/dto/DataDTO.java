package facade.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DataDTO {
	
	private LocalDate dia;
	private LocalTime inicio;
	private LocalTime fim;
	
	public DataDTO(LocalDate dia, LocalTime inicio, LocalTime fim) {
		this.dia = dia;
		this.inicio = inicio;
		this.fim = fim;
	}
	
	public LocalDate getDia() {
		return dia;
	}
	
	public LocalTime getInicio() {
		return inicio;
	}
	
	public LocalTime getFim() {
		return fim;
	}
	
	@Override
	public String toString() {
		return "Dia: " + dia.toString() + ", hora inicio: " + inicio.toString() + ", hora fim: " + fim.toString();
	}

}
