package business.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: BilhetePasse
 *
 */
@Entity

public class BilhetePasse extends Bilhete implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToMany @JoinTable(inverseJoinColumns=@JoinColumn(name="BilheteIndividual_ID")) 
	private List<BilheteIndividual> bilhetes;
	
	public BilhetePasse() {
		super();
	}

	public BilhetePasse(float preco) {
		super(preco);
		bilhetes = new ArrayList<>();
	}

	public void atribuiBilheteIndividual(BilheteIndividual bilheteIndividual) {
		bilhetes.add(bilheteIndividual);
	}
	
	
   
}
