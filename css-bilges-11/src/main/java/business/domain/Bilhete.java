package business.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity @Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Bilhete implements Serializable{
	
	private static final long serialVersionUID = -6194968589163213531L;

	public static final String BILHETE_DISPONIVEL = "DISPONIVEL";
	
	@Id @GeneratedValue 
	private int id;
	
	@Column(nullable = false)
	private float preco;
	
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private EstadoBilhete estado; 
	
	
	protected Bilhete() {
		super();
	}
	
	protected Bilhete(float preco) {
		this.preco = preco;
		this.estado = EstadoBilhete.DISPONIVEL;
	}
	
	public float getPreco() {
		return preco;
	}
	
	public EstadoBilhete getEstado() {
		return this.estado;
	}
   
	public boolean estaDisponivel() {
		return estado.equals(EstadoBilhete.DISPONIVEL);
	}
	
	public void reserva() {
		this.estado=EstadoBilhete.RESERVADO;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setStatus(EstadoBilhete estado) {
		this.estado = estado;
	}


}
