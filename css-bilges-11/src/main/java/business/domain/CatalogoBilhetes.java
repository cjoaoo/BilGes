package business.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import exceptions.DatabaseException;
import exceptions.ReservaInvalidaException;

public class CatalogoBilhetes {

	private EntityManager em;

	public CatalogoBilhetes(EntityManager em) {
		this.em = em;
	}

	// UC3
	public List<EventoData> getDatasComBilhetesDisponiveis(int eventoId) throws ReservaInvalidaException {
		TypedQuery<EventoData> query = em.createNamedQuery(EventoData.DATAS_COM_BILHETES, EventoData.class);
		query.setParameter(Evento.EVENTO_ID, eventoId);
		query.setParameter(Bilhete.BILHETE_DISPONIVEL, EstadoBilhete.DISPONIVEL);
		try {
			return query.getResultList();
		}catch(Exception e) {
			throw new ReservaInvalidaException("Problema na pesquisa de datas na base de dados.");
		}
	}


	// UC3
	public List<Lugar> getLugaresDisponiveisNaData(int eventoId, EventoData eventoData) throws DatabaseException {
		TypedQuery<Lugar> query = em.createNamedQuery(Lugar.LUGARES_DISPONIVEIS_NA_DATA, Lugar.class);
		query.setParameter(Evento.EVENTO_ID, eventoId);
		query.setParameter(Bilhete.BILHETE_DISPONIVEL, EstadoBilhete.DISPONIVEL);
		query.setParameter(BilheteIndividual.DIA_EVENTO, eventoData);
		try {
			return query.getResultList();
		}catch(Exception e) {	
			throw new DatabaseException("Problema na pesquisa de bilhetes na base de dados.");
		}
	}


	// UC3
	public BilheteIndividual getBilheteInLugar(int eventoId, EventoData eventoData, String letra, int num) throws ReservaInvalidaException {
		TypedQuery<BilheteIndividual> query = em.createNamedQuery(BilheteIndividual.BILHETE_NA_DATA_NO_LUGAR, BilheteIndividual.class);
		query.setParameter(Evento.EVENTO_ID, eventoId);
		query.setParameter(Bilhete.BILHETE_DISPONIVEL, EstadoBilhete.DISPONIVEL);
		query.setParameter(BilheteIndividual.DIA_EVENTO, eventoData);
		query.setParameter(Lugar.LETRA, letra);
		query.setParameter(Lugar.NUMERO, num);
		try {
			return query.getSingleResult();
		}catch(NoResultException e) {
			throw new ReservaInvalidaException("O lugar não existe ou já está ocupado.");
		}catch(Exception e) {			
			throw new ReservaInvalidaException("Problema na pesquisa de bilhetes na base de dados.");
		}
	}

	// UC4
	public int getNumBilhetesDisponiveisNaData(int eventoId, EventoData dia) throws ReservaInvalidaException {
		TypedQuery<Long> query = em.createNamedQuery(BilheteIndividual.NUM_BILHETES_DISPONIVEIS_NA_DATA, Long.class);
		query.setParameter(Evento.EVENTO_ID, eventoId);
		query.setParameter(Bilhete.BILHETE_DISPONIVEL, EstadoBilhete.DISPONIVEL);
		query.setParameter(BilheteIndividual.DIA_EVENTO, dia);
		try {
			return query.getSingleResult().intValue();
		}catch(Exception e) {
			throw new ReservaInvalidaException("Problema na pesquisa de datas na base de dados.");
		}
	}

	// UC4
	public BilhetePasse geraBilhetePasse(Evento evento) throws ReservaInvalidaException{
		BilhetePasse bilhetePasse = new BilhetePasse(evento.getPrecoPasse());
		em.persist(bilhetePasse);
		try {
			for(EventoData d: evento.getDatas()) {
				BilheteIndividual b = getBilhetesNaData(evento.getId(), d).get(0);
				em.persist(b);
				b.setStatus(EstadoBilhete.RESERVADO);
				bilhetePasse.atribuiBilheteIndividual(b);	
			}
			bilhetePasse.setStatus(EstadoBilhete.RESERVADO);
		}catch(DatabaseException e) {
			throw new ReservaInvalidaException(e.getMessage());

		}catch(Exception e) {
			throw new ReservaInvalidaException("Já não há bilhetes suficientes disponíveis.");
		}
		return bilhetePasse;
	}


	// UC4
	public List<BilheteIndividual> getBilhetesNaData (int eventoId, EventoData d) throws DatabaseException {
		TypedQuery<BilheteIndividual> query = em.createNamedQuery(BilheteIndividual.BILHETES_DISPONIVEIS_NA_DATA, BilheteIndividual.class);
		query.setParameter(Evento.EVENTO_ID, eventoId);
		query.setParameter(Bilhete.BILHETE_DISPONIVEL, EstadoBilhete.DISPONIVEL);
		query.setParameter(BilheteIndividual.DIA_EVENTO, d);
		try {
			return query.getResultList();
		}catch(Exception e) {
			throw new DatabaseException("Problema na pesquisa de bilhetes na base de dados.");
		}
	}

/*
	// UC4 tentativa versao mais eficiente
	public List<BilhetePasse> geraBilhetesPasse(Evento evento, int numBilhetes) throws ReservaInvalidaException{
		List<BilhetePasse> bilhetesPasse = new ArrayList<>();
		List<List<BilheteIndividual>> listaDeListasDeBilhetesPorDia = new ArrayList<>();
		try {
			for(EventoData d: evento.getDatas()) {
				listaDeListasDeBilhetesPorDia.add(getBilhetesNaData(evento.getId(), d));	
			}
			for(int i = 0; i < numBilhetes; i++) {
				BilhetePasse bilhetePasse = new BilhetePasse(evento.getPrecoPasse());
				for(List<BilheteIndividual> lista : listaDeListasDeBilhetesPorDia) {
					BilheteIndividual b = lista.get(0);
					b.setStatus(EstadoBilhete.RESERVADO);
					bilhetePasse.atribuiBilheteIndividual(b);
					em.merge(b);
				}
				bilhetePasse.reserva();
			}		
		}catch(Exception e) {			
			throw new ReservaInvalidaException(e.getMessage());
		}
		return bilhetesPasse;
	}
*/


}
