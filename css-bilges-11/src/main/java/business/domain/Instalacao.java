package business.domain;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import exceptions.InstalacaoException;

/**
 * Entity implementation class for Entity: Instalacao
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name=Instalacao.GET_NOMES_INSTALACOES, query="SELECT i.nome FROM Instalacao i"),
	@NamedQuery(name=Instalacao.FIND_BY_NAME, query="SELECT i FROM Instalacao i WHERE i.nome = :" + Instalacao.NOME_INSTALACAO),
	@NamedQuery(name=Instalacao.INSTALACAO_OCUPADA, query="SELECT count(i) FROM Instalacao i, IN(i.ocupacao) AS d WHERE i.id= :" 
			+ Instalacao.INSTALACAO_ID +" AND d.dia= :" + EventoData.DIA_EVENTO),
	})

public class Instalacao implements Serializable {


	private static final long serialVersionUID = -8524078031002352311L;
	
	// contantes para as NamedQueries
	public static final String GET_NOMES_INSTALACOES = "Instalacao.getInstalacoes";
	public static final String FIND_BY_NAME = "Instalacao.findByName";
	public static final String NOME_INSTALACAO = "nome";
	public static final String INSTALACAO_ID = "id";
	public static final String INSTALACAO_OCUPADA = "Instalacao.instalacaoOcupadaNoDia";

	@Id @GeneratedValue private int id;
	
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private boolean lugaresIndividuais;
	@Column(nullable = false)
	private int capacidade;
	@Column(nullable = false)
	private int numRegisto;

	
	@OneToMany(cascade = ALL) @JoinColumn(name="INSTALACAO_ID", nullable=false)
	private List<Lugar> lugares;
	
	@OneToMany(cascade = ALL) @JoinColumn(name="INSTALACAO_ID")
	private List<EventoData> ocupacao;
	
	Instalacao() {
	}

	public void adequadaParaEvento(Evento evento) throws InstalacaoException {
		if(evento.getDimAssistencia() < capacidade) {
			throw new InstalacaoException("A instalacao tem maior capacidade do que a dimensão máxima da assistência deste tipo de evento.");
		}
		if(evento.temLugaresMarcados() && !lugaresIndividuais) {
			throw new InstalacaoException("A instalacao nao tem lugares individuais e o evento é de lugares marcados.");
		}
		// if(!evento.temLugaresMarcados() && !lugaresIndividuais)  ?
		
	}

	public int getId() {
		return id;
	}

	public ArrayList<Lugar> getLugares() {
		ArrayList<Lugar> lugaresInstalacao = new ArrayList<>();
		for(Lugar l: lugares) {
			lugaresInstalacao.add(l);
		}
		return lugaresInstalacao;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void acrescentaDatas(List<EventoData> datas) {
		for(EventoData d: datas) {
			ocupacao.add(d);
		}
		
	}

	
   
}
