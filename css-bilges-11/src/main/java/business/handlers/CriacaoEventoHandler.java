package business.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.domain.CatalogoEmpresas;
import business.domain.CatalogoEventos;
import business.domain.CatalogoInstalacoes;
import business.domain.EventoData;
import business.domain.Empresa;
import business.domain.Evento;
import business.domain.Instalacao;
import business.domain.TipoEvento;
import business.utils.DataUtils;
import exceptions.EventoInvalidoException;
import exceptions.InstalacaoException;
import facade.dto.DataDTO;

public class CriacaoEventoHandler {
	
	private EntityManagerFactory emf;
	private String nome;
	private Empresa empresa;
	private TipoEvento tipoEvento;
	private ArrayList<DataDTO> datas = new ArrayList<>();

	public CriacaoEventoHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	// UC1
	public String getTiposEventos() {
		return TipoEvento.getNames();
	}

	// UC1
	public void indicaDetalhesEventos(String nome, String tipo, int numEmpresa) throws EventoInvalidoException {
		
		EntityManager em = emf.createEntityManager();
		CatalogoEmpresas catEmpresas = new CatalogoEmpresas(em);
		CatalogoEventos catEventos = new CatalogoEventos(em);
		try {
			em.getTransaction().begin();
			if(catEventos.eventoExiste(nome)) {
				throw new EventoInvalidoException("Ja existe um evento com esse nome.");
			}
			this.nome = nome;
			this.tipoEvento = tipoValido(tipo);
			this.empresa = catEmpresas.getEmpresaByNumRegisto(numEmpresa);
			if(!empresa.temCertificado(tipoEvento)) {
				throw new EventoInvalidoException("Empresa nao tem certificado para este tipo de evento.");
			}
			em.getTransaction().commit();	
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new EventoInvalidoException(e.getMessage());
		}finally {
			em.close();
		}
	}



	// UC1
	public void indicaData(DataDTO data) throws EventoInvalidoException {
		if(!DataUtils.dataNoFuturo(data.getDia())) {
			throw new EventoInvalidoException("O evento tem de ser depois do dia de hoje.");
		}
		if(!DataUtils.horasValidas(data.getInicio(), data.getFim())) {
			throw new EventoInvalidoException("A hora de inicio deve ser anterior à hora de fim (excepto se terminar à meia-noite).");
		}
		datas.add(data);
	}

	// UC1
	public void terminarCriarEvento() throws EventoInvalidoException {
		
		if(!DataUtils.datasConsecutivas(datas)) {
			throw new EventoInvalidoException("As datas do evento devem ser consecutivas.");
		}
		ArrayList<EventoData> datasEvento = DataUtils.convertDTODataToData(this.datas);
			
		EntityManager em = emf.createEntityManager();
		CatalogoEventos catEventos = new CatalogoEventos(em);
		try {
			em.getTransaction().begin();
			catEventos.createEvento(nome, tipoEvento, empresa, datasEvento);
			em.getTransaction().commit();	
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new EventoInvalidoException(e.getMessage());
		}finally {
			em.close();
		}
		
	}

	// UC2
	public String getListaInstalacoes() throws InstalacaoException {
		EntityManager em = emf.createEntityManager();
		CatalogoInstalacoes catInstalacoes = new CatalogoInstalacoes(em);
		try {
			em.getTransaction().begin();
			return catInstalacoes.getListaInstalacoes();
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new InstalacaoException(e.getMessage());
		}finally {
			em.close();
		}
	}

	// UC2  sem bilhete passe
	public void escolheInstalacao(String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes,
			float precoIndividual) throws InstalacaoException, EventoInvalidoException {
		escolheInstalacao(false, nomeEvento, nomeInstalacao, dataVendaBilhetes, precoIndividual, 0);
		
	}

	// UC2 com bilhete passe
	public void escolheInstalacao(String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes,
			float precoIndividual, float precoPasse) throws InstalacaoException, EventoInvalidoException {
		
		escolheInstalacao(true, nomeEvento, nomeInstalacao, dataVendaBilhetes, precoIndividual, precoPasse);
	}
	
	//---------------------------------------------------------------------------------------------------------//
	// metodos auxiliares
	//---------------------------------------------------------------------------------------------------------//
	
	// UC2 generico para com e sem bilhete passe
	private void escolheInstalacao(boolean temPasse, String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes,
			float precoIndividual, float precoPasse) throws InstalacaoException, EventoInvalidoException {
		
		EntityManager em = emf.createEntityManager();
		CatalogoInstalacoes catInstalacoes = new CatalogoInstalacoes(em);
		CatalogoEventos catEventos = new CatalogoEventos(em);
		
		if(precoIndividual < 0 || (temPasse && precoPasse < 0)){
			throw new EventoInvalidoException("O preço do bilhete tem de ser maior ou igual a zero.");
		}
		try {
			em.getTransaction().begin();
			Evento evento = catEventos.findByName(nomeEvento);
			em.persist(evento);
			if(evento.hasInstalacao()) {
				throw new EventoInvalidoException("O evento ja tem instalação atribuída.");
			}
			List<LocalDate> diasEvento = catEventos.getDiasDoEvento(evento.getId());
			if(DataUtils.dataNoPassado(dataVendaBilhetes) || diasEvento.get(0).isBefore(dataVendaBilhetes)) {
				throw new EventoInvalidoException("A data a partir da qual os bilhetes são colocados à venda "
						+ "não pode ser no passado e deve ser anterior à primeira data do evento.");
			}
			Instalacao instalacao = catInstalacoes.findByName(nomeInstalacao);
			instalacao.adequadaParaEvento(evento);
			if(!catInstalacoes.instalacaoLivreNasDatas(em, instalacao, diasEvento)) {
				throw new InstalacaoException("A instalação não está livre na(s) data(s) do evento.");
			}
			evento.setInstalacao(instalacao);
			instalacao.acrescentaDatas(evento.getDatas());
			evento.setDataVendaBilhetes(dataVendaBilhetes);
			evento.geraBilhetesIndividuais(precoIndividual);
			if(temPasse) {
				evento.setPrecoPasse(precoPasse);
			}	
			em.getTransaction().commit();
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new InstalacaoException(e.getMessage(), e);
		}finally {
			em.close();
		}
		
	}
	
	
	// UC1 parse da string para validar o tipo do evento
	private TipoEvento tipoValido(String tipo) throws EventoInvalidoException{
		TipoEvento[] tipos = TipoEvento.values();
		for(int i = 0; i < tipos.length; i++) {
			if(tipos[i].toString().equalsIgnoreCase(tipo)) {
				return tipos[i];
			}
		}
		throw new EventoInvalidoException("Tipo de evento invalido.");
	}
	
}


