package business.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import business.domain.CatalogoEventos;
import business.domain.Evento;
import business.domain.EventoData;
import business.domain.Lugar;
import exceptions.ApplicationException;
import business.domain.BilheteIndividual;
import business.domain.BilhetePasse;
import business.domain.CatalogoBilhetes;
import business.domain.Reserva;
import business.utils.DataUtils;
import business.utils.MockData;
import exceptions.ReservaInvalidaException;
import java.time.LocalDate;

import facade.dto.DataDTO;
import facade.dto.LugarDTO;

public class ReservaBilhetesHandler {

	private EntityManagerFactory emf;
	private Evento evento;
	private EventoData eventoData;
	private Reserva reserva;

	public ReservaBilhetesHandler(EntityManagerFactory emf) {
		this.emf = emf;
		this.eventoData = null;
		this.reserva = new Reserva(MockData.getCurrentDate());
	}

	// UC3: iniciar reserva: retorna datas com lugares disponiveis neste evento
	public List<DataDTO> getDatasComBilhetesDisponiveis(String nomeEvento) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoEventos catEventos = new CatalogoEventos(em);
		CatalogoBilhetes catBilhetes = new CatalogoBilhetes(em);
		ArrayList<DataDTO> datas = new ArrayList<>();
		List<EventoData> eDatas;
		try {
			this.evento = catEventos.findByName(nomeEvento);
			if(DataUtils.dataNoFuturo(evento.getDataVendaBilhetes())) {
				throw new ReservaInvalidaException("Os bilhetes para este evento ainda não estão à venda.");
			}
			eDatas = catBilhetes.getDatasComBilhetesDisponiveis(evento.getId());
			if (!eDatas.isEmpty()) {
				for (EventoData e : eDatas) {
					DataDTO d = new DataDTO(e.getDia(), e.getInicio(), e.getFim());
					datas.add(d);
				}
			} else {
				throw new ReservaInvalidaException("Nao existem datas com bilhetes disponiveis.");
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			em.close();
		}
		return datas;
	}

	// UC3: utilizador escolhe data, recebe lugares disponiveis
	public List<LugarDTO> escolheData(LocalDate data) throws ReservaInvalidaException {
		List<LugarDTO> res = new ArrayList<>();
		EntityManager em = emf.createEntityManager();
		CatalogoBilhetes catBilhetes = new CatalogoBilhetes(em);
		List<EventoData> datasEvento = this.evento.getDatas();
		try {
			if (!this.evento.temLugaresMarcados()) {
				throw new ReservaInvalidaException("O evento nao tem lugares marcados.");
			}
			// verifica que a data escolhida existe
			for (EventoData d : datasEvento) {
				if (d.getDia().equals(data)) {
					eventoData = d;
					break;
				}
			}
			if (eventoData == null) {
				throw new ReservaInvalidaException("O evento nao tem a data indicada");
			}	
			List<Lugar> lugares = catBilhetes.getLugaresDisponiveisNaData(evento.getId(), eventoData);
			if (lugares.isEmpty()) {
				throw new ReservaInvalidaException("Nao existem bilhetes na data indicada");
			}
			for (Lugar l : lugares) {
				res.add(new LugarDTO(l.getLetra(), l.getNum()));
			}
		}catch(Exception e) {
			throw new ReservaInvalidaException(e.getMessage());
		}finally {
			em.close();	
		}
		return res;
	}



	// UC3
	public void escolheLugar(LugarDTO lugar)throws ReservaInvalidaException{
		EntityManager em = emf.createEntityManager();
		CatalogoBilhetes catBilhetes = new CatalogoBilhetes(em);
		try {
			em.getTransaction().begin();
			BilheteIndividual bilhete= catBilhetes.getBilheteInLugar(evento.getId(), eventoData, lugar.getLetra(), lugar.getNum());
			bilhete.reserva();
			reserva.addBilheteIndividual(bilhete);
			em.merge(bilhete);
			//em.merge(reserva);
			em.getTransaction().commit();
		}catch (Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ReservaInvalidaException(e.getMessage());
		}finally {
			em.close();
		}
	}


	// UC3: finalizar reserva
	public String indicaEmail(String email)throws ReservaInvalidaException {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			reserva.setEmail(email);
			reserva.getDadosPagamento();
			em.merge(reserva);
			em.getTransaction().commit();			
		}catch (Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ReservaInvalidaException(e.getMessage());
		}finally {
			em.close();
		}
		return reserva.getTotalEntidadeReferencia();
	}


	// UC4
	public int getNumBilhetesPasseDisponiveis(String nomeEvento) throws ReservaInvalidaException {
		int numBilhetesDisponiveis;
		EntityManager em = emf.createEntityManager();
		CatalogoEventos catEventos = new CatalogoEventos(em);
		CatalogoBilhetes catBilhetes = new CatalogoBilhetes(em);
		try {
			em.getTransaction().begin();
			this.evento = catEventos.findByName(nomeEvento);
			if (!evento.temBilhetePasse()) {
				throw new ReservaInvalidaException("O evento não tem a opção de venda de bilhetes passe.");
			}
			if(DataUtils.dataNoFuturo(evento.getDataVendaBilhetes())) {
				throw new ReservaInvalidaException("Os bilhetes para este evento ainda não estão à venda.");
			}
			numBilhetesDisponiveis = numBilhetesPasseDisponiveis(catBilhetes);

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ReservaInvalidaException(e.getMessage());

		} finally {
			em.close();
		}
		return numBilhetesDisponiveis;
	}

	// UC4
	public String indicaNumBilhetesPasseEmail(int numBilhetes, String email) throws ReservaInvalidaException {
		EntityManager em = emf.createEntityManager();
		CatalogoBilhetes catBilhetes = new CatalogoBilhetes(em);
		if (numBilhetes > numBilhetesPasseDisponiveis(catBilhetes)) {
			throw new ReservaInvalidaException("Já não existem " + numBilhetes + " bilhetes passe disponiveis.");
		}
		try {
			em.getTransaction().begin();
			// gera os bilhetes passe necessarios
			
			for (int i = 0; i < numBilhetes; i++) {
				BilhetePasse bil = catBilhetes.geraBilhetePasse(evento);
				evento.addBilhetePasse(bil);
				reserva.addBilhetePasse(bil);
			}
			// finaliza reserva com email e pagamento
			reserva.setEmail(email);
			reserva.getDadosPagamento();
			em.merge(evento);
			em.merge(reserva);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ReservaInvalidaException(e.getMessage());
		} finally {
			em.close();
		}
		return reserva.getTotalEntidadeReferencia();
	}

	//---------------------------------------------------------------------------------------------------------//
	// metodos auxiliares
	//---------------------------------------------------------------------------------------------------------//


	// UC4: auxiliar para getNumBilhetesPasseDisponiveis
	private int numBilhetesPasseDisponiveis(CatalogoBilhetes catBilhetes) throws ReservaInvalidaException {
		ArrayList<Integer> numBilhetesPorDia = new ArrayList<>();
		for (EventoData d : evento.getDatas()) {
			Integer num = catBilhetes.getNumBilhetesDisponiveisNaData(evento.getId(), d);
			if (num > 0) {
				numBilhetesPorDia.add(num);
			} else {
				throw new ReservaInvalidaException("Já não há bilhetes passe disponiveis.");
			}
		}
		return (int) Collections.min(numBilhetesPorDia);
	}



}
