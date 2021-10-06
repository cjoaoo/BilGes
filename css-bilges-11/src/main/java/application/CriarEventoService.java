package application;

import java.time.LocalDate;

import business.handlers.CriacaoEventoHandler;
import exceptions.ApplicationException;
import exceptions.EventoInvalidoException;
import exceptions.InstalacaoException;
import facade.dto.DataDTO;

public class CriarEventoService {
	
	private CriacaoEventoHandler eventosHandler;

	public CriarEventoService(CriacaoEventoHandler criacaoEventoHandler) {
		this.eventosHandler = criacaoEventoHandler;
	}

	// UC1
	public String iniciaCriacaoEvento(){
		return eventosHandler.getTiposEventos();
	}

	// UC1
	public void indicaDetalhesEvento(String nome, String tipo, int numEmpresa) throws ApplicationException{
		try {	
			eventosHandler.indicaDetalhesEventos(nome, tipo, numEmpresa);
		}catch(Exception e) {
			throw new ApplicationException("Erro na criacao de evento: " + e.getMessage());
		}		
	}

	// UC1
	public void indicaData(DataDTO data) throws ApplicationException {
		try {
			eventosHandler.indicaData(data);
			
		}catch(EventoInvalidoException e) {
			throw new ApplicationException("Erro na criacao de evento: " + e.getMessage());
		}
	}

	// UC1
	public void terminar() throws ApplicationException {
		try {
		eventosHandler.terminarCriarEvento();
		}catch(Exception e) {
			throw new ApplicationException("Erro na criacao de evento: " + e.getMessage());
		}
	}

	// UC2
	public String getListaInstalacoes() throws ApplicationException {
		try {
			return eventosHandler.getListaInstalacoes();
		} catch (InstalacaoException e) {
			throw new ApplicationException("Erro ao obter lista de instalacoes: " + e.getMessage());
		}
	}

	// UC2
	public void escolheInstalacao(String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes,
			float precoIndividual) throws ApplicationException{
		try {
		eventosHandler.escolheInstalacao(nomeEvento, nomeInstalacao, dataVendaBilhetes, precoIndividual);
		}catch(Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
		
	}

	// UC2
	public void escolheInstalacao(String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes,
			float precoIndividual, float precoPasse) throws ApplicationException{
		try {
		eventosHandler.escolheInstalacao(nomeEvento, nomeInstalacao, dataVendaBilhetes, precoIndividual, precoPasse);
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(), e);
		}
		
	}

}
