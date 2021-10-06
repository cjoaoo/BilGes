package business.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.*;


@Entity
@NamedQueries({
@NamedQuery(name = EventoData.DATAS_COM_BILHETES, query = "SELECT DISTINCT b.data FROM Evento e, IN(e.bilhetesIndividuais) AS b WHERE e.id = :"
		+ Evento.EVENTO_ID + " AND b.estado= :" + Bilhete.BILHETE_DISPONIVEL),
@NamedQuery(name = EventoData.DIAS_DO_EVENTO, query = "SELECT d.dia FROM Evento e, IN(e.datas) AS d WHERE e.id = :"
		+ Evento.EVENTO_ID + " ORDER BY d.dia ASC")
})
public class EventoData implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public static final String DIA_EVENTO = "dia";
	public static final String DATAS_COM_BILHETES= "EventoData.datasComBilhetes";
	public static final String DIAS_DO_EVENTO= "EventoData.datasDoEvento";
	
	@Id @GeneratedValue 
	private int id;

	@Convert(converter = converters.LocalDateAttributeConverter.class) @Column(nullable = false)
	private LocalDate dia;
	@Convert(converter = converters.LocalTimeAttributeConverter.class) @Column(nullable = false)
	private LocalTime inicio;
	@Convert(converter = converters.LocalTimeAttributeConverter.class) @Column(nullable = false)
	private LocalTime fim;
	
	public EventoData() {
		super();
	}
		
	public EventoData(LocalDate dia, LocalTime inicio, LocalTime fim) {
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


}
