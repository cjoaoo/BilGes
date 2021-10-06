package business.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import exceptions.DatabaseException;
import exceptions.EventoInvalidoException;

public class CatalogoEventos {

	private EntityManager em;
	private static final int DIM_TETEATETE = 6;
	private static final int DIM_BANDOSENTADO = 1000;
	private static final int DIM_MULTIDAOEMPE = 500000;


	public CatalogoEventos(EntityManager em) {
		this.em = em;
	}

	public boolean eventoExiste(String nome) throws DatabaseException {
		TypedQuery<Long> query = em.createNamedQuery(Evento.EXISTS_BY_NAME, Long.class);
		query.setParameter(Evento.NOME_EVENTO, nome);
		try {
			return query.getSingleResult() > 0;
		} catch(Exception e) {
			throw new DatabaseException("Erro ao pesquisar na base de dados.");
		}

	}

	public Evento findByName(String nome) throws EventoInvalidoException {
		TypedQuery<Evento> query = em.createNamedQuery(Evento.FIND_BY_NAME, Evento.class);
		query.setParameter(Evento.NOME_EVENTO, nome);
		try {
			return query.getSingleResult();
		}catch(Exception e) {
			throw new EventoInvalidoException("O evento com esse nome nao existe");
		}
	}

	public void createEvento(String nome, TipoEvento tipoEvento, Empresa empresa, List<EventoData> datas) {
		Evento evento = null;
		if(tipoEvento.equals(TipoEvento.TETEATETE)) {
			evento = new Evento(nome, tipoEvento, DIM_TETEATETE, true, empresa, datas);
		}else if(tipoEvento.equals(TipoEvento.BANDOSENTADO)) {
			evento = new Evento(nome, tipoEvento, DIM_BANDOSENTADO, true, empresa, datas);
		}else if(tipoEvento.equals(TipoEvento.MULTIDAOEMPE)) {
			evento = new Evento(nome, tipoEvento, DIM_MULTIDAOEMPE, false, empresa, datas);
		}
		if(evento != null){
			em.persist(evento);
		}
	}
	
	public List<LocalDate> getDiasDoEvento(int eventoId) throws DatabaseException, EventoInvalidoException {
		TypedQuery<LocalDate> query = em.createNamedQuery(EventoData.DIAS_DO_EVENTO, LocalDate.class);
		query.setParameter(Evento.EVENTO_ID, eventoId);
		List<LocalDate> res = new ArrayList<>();
		try {
			 res = query.getResultList();
		}catch(Exception e) {	
			throw new DatabaseException("Problema na pesquisa de bilhetes na base de dados.");
		}
		if(res.isEmpty()) {
			throw new EventoInvalidoException("O evento não tem datas definidas.");
		}
		return res;

	}







}
