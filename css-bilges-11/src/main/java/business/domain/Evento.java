package business.domain;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Evento
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name = Evento.EXISTS_BY_NAME, query = "SELECT COUNT(e) FROM Evento e WHERE e.nome = :"
			+ Evento.NOME_EVENTO),
	@NamedQuery(name = Evento.FIND_BY_NAME, query = "SELECT e FROM Evento e WHERE e.nome = :" + Evento.NOME_EVENTO),
})
public class Evento implements Serializable {

	// contantes para as NamedQueries
	public static final String EXISTS_BY_NAME = "Evento.existsByName";
	public static final String FIND_BY_NAME = "Evento.findByName";
	public static final String NOME_EVENTO = "nome";
	public static final String EVENTO_ID = "id";

	private static final long serialVersionUID = 7674492361431385285L;

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private int dimAssistencia;
	
	@Column(nullable = false)
	private boolean lugaresMarcados;
	
	@JoinColumn(nullable = false)
	private Empresa empresa;
	
	private float precoPasse;
	private Instalacao instalacao;
	
	@Convert(converter = converters.LocalDateAttributeConverter.class)
	private LocalDate dataVendaBilhetes;

	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private TipoEvento tipo;

	@OneToMany(cascade = ALL, orphanRemoval = true)
	@JoinColumn(name = "EVENTO_ID")
	private List<EventoData> datas = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "EVENTO_ID")
	private List<BilheteIndividual> bilhetesIndividuais = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "EVENTO_ID")
	private List<BilhetePasse> bilhetesPasse = new ArrayList<>();
  
	public Evento() {
	}

	public Evento(String nome, TipoEvento tipoEvento, int dimAssistencia, boolean lugaresMarcados, Empresa empresa,
			List<EventoData> datas) {
		this.tipo = tipoEvento;
		this.nome = nome;
		this.lugaresMarcados = lugaresMarcados;
		this.dimAssistencia = dimAssistencia;
		this.empresa = empresa;
		this.datas = datas;
		this.instalacao = null;
		this.precoPasse = -1;
	}

	public int getId() {
		return id;
	}
	
	public int getDimAssistencia() {
		return dimAssistencia;
	}
	
	public String getNome() {
		return nome;
	}
	
	public LocalDate getDataVendaBilhetes(){
		return this.dataVendaBilhetes;
	}

	public List<EventoData> getDatas() {
		ArrayList<EventoData> datasEvento = new ArrayList<>();
		for (EventoData d : datas) {
			datasEvento.add(d);
		}
		return datasEvento;
	}

	public void setPrecoPasse(float precoPasse) {
		this.precoPasse = precoPasse;
	}

	public void geraBilhetesIndividuais(float precoIndividual) {
		if (lugaresMarcados) {
			for (EventoData data : datas) {
				ArrayList<Lugar> lugares = instalacao.getLugares();
				for (Lugar lugar : lugares) {
					bilhetesIndividuais.add(new BilheteIndividual(precoIndividual, data, lugar));
				}
			}

		} else {
			for (EventoData data : datas) {
				int capacidade = instalacao.getCapacidade();
				for (int i = 0; i < capacidade; i++) {
					bilhetesIndividuais.add(new BilheteIndividual(precoIndividual, data));
				}
			}
		}
	}

	public void setInstalacao(Instalacao instalacao) {
		this.instalacao = instalacao;
	}
	
	public void setDataVendaBilhetes(LocalDate dataVendaBilhetes) {
		this.dataVendaBilhetes = dataVendaBilhetes;
		
	}

	
	public List<BilheteIndividual> getBilhetes(){
		return bilhetesIndividuais;
	}

	public float getPrecoPasse() {
		return this.precoPasse;
	}

	public boolean temBilhetePasse() {
		return this.precoPasse != -1;
	}

	public void addBilhetePasse(BilhetePasse bil) {
		bilhetesPasse.add(bil);
	}
	
	public boolean hasInstalacao() {
		return instalacao != null;
	}

	public boolean temLugaresMarcados() {
		return lugaresMarcados;
	}

		


}
