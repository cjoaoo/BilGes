package business.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import business.domain.EventoData;
import facade.dto.DataDTO;

public class DataUtils {
	
	private DataUtils() {
		//vazio
	}
	
	/*
public static Date convertLocalDateTimeToDate(LocalDate dia, LocalTime hora) {
	return Date.from(hora.atDate(dia).atZone(ZoneId.systemDefault()).toInstant());
}

public static LocalDate convertDateToLocalDate(Date dia) {
	return dia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}

public static LocalDateTime convertDateToLocalDateTime(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
}
*/

public static boolean dataNoFuturo(LocalDate dia) {
	return dia.isAfter(MockData.getCurrentDate());
}

public static boolean dataNoPassado(LocalDate dia) {
	return dia.isBefore(MockData.getCurrentDate());
}

public static boolean horasValidas(LocalTime horaInicio, LocalTime horaFim) {
	return horaInicio.isBefore(horaFim) || (horaFim.equals(LocalTime.of(0,0))); 
}

public static boolean datasConsecutivas(List<DataDTO>datas) {
	for(int i = 0; i < datas.size()-1; i++) {
		LocalDate d = datas.get(i).getDia();	
		if(!d.plusDays(1).equals(datas.get(i+1).getDia())) {
			return false;
		}
	}
	return true;
}




public static ArrayList<EventoData> convertDTODataToData(List<DataDTO> datas){
	ArrayList<EventoData >datasConvertidas = new ArrayList<>();
	for(DataDTO data: datas) {
		datasConvertidas.add(new EventoData(data.getDia(), data.getInicio(), data.getFim()));
	}
	return datasConvertidas;
	}


public static Date convertLocalDateToDate(LocalDate dia) {
	return Date.from(LocalTime.of(0, 0).atDate(dia).atZone(ZoneId.systemDefault()).toInstant());
}
}


