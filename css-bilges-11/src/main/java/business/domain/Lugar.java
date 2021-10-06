package business.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Lugar
 *
 */
@Entity
@NamedQuery(name = Lugar.LUGARES_DISPONIVEIS_NA_DATA, query = "SELECT b.lugar FROM Evento e, IN(e.bilhetesIndividuais) AS b WHERE e.id = :"
		+ Evento.EVENTO_ID + " AND b.estado= :" + Bilhete.BILHETE_DISPONIVEL + " AND b.data= :"+ BilheteIndividual.DIA_EVENTO
		+ " ORDER BY b.lugar.letra ASC, b.lugar.numero") 
public class Lugar implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4712348101973360308L;
	
	
	public static final String LUGARES_DISPONIVEIS_NA_DATA = "Lugar.disponiveisNaData";

	
	@Id @GeneratedValue private int id;

	@Column(nullable = false)
	private String letra;
	
	@Column(nullable = false)
	private int numero;
	
	public static final String LETRA = "letra";
	public static final String NUMERO = "numero";
	
	Lugar() {
	}
	
	@Override
	public String toString() {
		return letra + "-" + numero;
	}

	public String getLetra() {
		return letra;
	}

	public int getNum() {
		return numero;
	}
   
}
