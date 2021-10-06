package business.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: BilheteIndividual
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name = BilheteIndividual.BILHETES_DISPONIVEIS_NA_DATA, query = "SELECT b FROM Evento e, IN(e.bilhetesIndividuais) AS b WHERE e.id = :"
			+ Evento.EVENTO_ID + " AND b.estado= :" + Bilhete.BILHETE_DISPONIVEL + " AND b.data= :"+ BilheteIndividual.DIA_EVENTO
			+ " ORDER BY b.lugar.letra ASC, b.lugar.numero ASC"),
	@NamedQuery(name = BilheteIndividual.NUM_BILHETES_DISPONIVEIS_NA_DATA, query = "SELECT COUNT(b) FROM Evento e, IN(e.bilhetesIndividuais) AS b WHERE e.id = :"
			+ Evento.EVENTO_ID + " AND b.estado= :" + Bilhete.BILHETE_DISPONIVEL + " AND b.data= :"+ BilheteIndividual.DIA_EVENTO ),
	@NamedQuery(name = BilheteIndividual.BILHETE_NA_DATA_NO_LUGAR, query = "SELECT b FROM Evento e, IN(e.bilhetesIndividuais) AS b WHERE e.id = :" 
			+ Evento.EVENTO_ID + " AND b.estado= :" + Bilhete.BILHETE_DISPONIVEL + " AND b.data= :"+ BilheteIndividual.DIA_EVENTO + " AND b.lugar.letra= :"
			+ Lugar.LETRA + " AND b.lugar.numero= :" + Lugar.NUMERO)
})
public class BilheteIndividual extends Bilhete implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public static final String BILHETES_DISPONIVEIS_NA_DATA = "Bilhete.disponiveisNaData";
	public static final String NUM_BILHETES_DISPONIVEIS_NA_DATA = "Bilhete.numBilhetesDisponiveisNaData";
	public static final String BILHETE_NA_DATA_NO_LUGAR = "Bilhete.getBilheteNaDataNoLugar";
	public static final String DIA_EVENTO = "data";
	
	@JoinColumn(nullable = false)
	private EventoData data;	
	
	private Lugar lugar;

	public BilheteIndividual() {
		super();
	}
	
	public BilheteIndividual(float preco, EventoData data) {
		super(preco);
		this.data = data;
	}
	
	public BilheteIndividual(float preco, EventoData data, Lugar lugar) {
		super(preco);
		this.data = data;
		this.lugar = lugar;
	}
	
	public Lugar getLugar() {
		return lugar;
	}

	
	public EventoData getEventoData() {
		return data;
	}


   
	
}
