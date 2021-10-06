package business.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import business.utils.MockPagamento;

/**
 * Entity implementation class for Entity: Venda
 *
 */
@Entity

public class Reserva implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 89231299297645495L;

	/**
	 * primary key da venda
	 */
	@Id @GeneratedValue private int id;

	private float total;
	private String email;
	private int entidade;
	private int referencia;
	
	@Convert(converter = converters.LocalDateAttributeConverter.class)
	private LocalDate dataReserva;
	
	@OneToMany @JoinColumn(name="RESERVA_ID")
	private List<BilhetePasse> bilhetesPasse= new ArrayList<>();
	
	@OneToMany @JoinColumn(name="RESERVA_ID")
	private List<BilheteIndividual> bilhetesIndividuais= new ArrayList<>();

	public Reserva() {
	}

	
	public Reserva(LocalDate data) {
		this.dataReserva = data;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void getDadosPagamento() {
		for (Bilhete b : bilhetesPasse) {
			total+=b.getPreco();
		}
		for (Bilhete b : bilhetesIndividuais) {
			total+=b.getPreco();
		}
		MockPagamento dados = MockPagamento.getDadosPagamento(total);
		this.entidade = dados.getEntidade();
		this.referencia = dados.getReferencia();
	}


	public String getTotalEntidadeReferencia() {
		return "Total: "+this.total+"€ \nDados pagamento:\n"+
				"Entidade: "+ entidade + "\nReferencia: " + referencia;

	}


	public void incrementaTotal(float precoBilhete) {
		this.total += precoBilhete;

	}


	public void addBilhetePasse(BilhetePasse bilhete) {
		bilhetesPasse.add(bilhete);	
	}
	
	public void addBilheteIndividual(BilheteIndividual bilhete) {
		bilhetesIndividuais.add(bilhete);	
	}


}
