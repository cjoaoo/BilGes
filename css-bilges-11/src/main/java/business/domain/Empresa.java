package business.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Empresa
 *
 */
@Entity
@NamedQuery(name=Empresa.FIND_BY_NUMREGISTO, query= "SELECT e FROM Empresa e WHERE e.numRegisto = :" + Empresa.NUM_REGISTO)
public class Empresa implements Serializable {
	
	// constantes para a named query
	public static final String FIND_BY_NUMREGISTO = "Empresa.findByNumRegisto";
	public static final String NUM_REGISTO = "numRegisto";

	private static final long serialVersionUID = 1638038044994702773L;

	@Id @GeneratedValue 
	private int id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private int numRegisto;
	
	@ElementCollection @Enumerated(EnumType.STRING) @Column(name="TIPOEVENTO")
	private List<TipoEvento> certificados;
	
	
	public Empresa() {
	}


	public boolean temCertificado(TipoEvento tipoEvento) {
		return certificados.contains(tipoEvento);
	}
   
}
