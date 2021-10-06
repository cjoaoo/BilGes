package application;

import java.time.LocalDate;
import java.util.List;

import business.handlers.ReservaBilhetesHandler;

import exceptions.ApplicationException;
import exceptions.ReservaInvalidaException;

import facade.dto.DataDTO;
import facade.dto.LugarDTO;

public class ReservaBilhetesService {
	
	private ReservaBilhetesHandler reservaHandler;

	public ReservaBilhetesService(ReservaBilhetesHandler reservaBilhetesHandler) {
		this.reservaHandler = reservaBilhetesHandler;
	}

	// UC3
	public List<DataDTO> getDatasComBilhetesDisponiveis(String nomeEvento) throws ApplicationException {
		try {
			return reservaHandler.getDatasComBilhetesDisponiveis(nomeEvento);
		} catch (Exception e) {
			throw new ApplicationException("Erro na criação da reserva: " + e.getMessage());
		}
	}

	// UC3
	public List<LugarDTO> escolheData(LocalDate data) throws ApplicationException {
		try {
			return reservaHandler.escolheData(data);
		}catch (ReservaInvalidaException e) {
			throw new ApplicationException("Erro na criação da reserva: "+ e.getMessage());
		}	
	}
	

	// UC3
	public void escolheLugar(LugarDTO lugar) throws ApplicationException {
		try {
			reservaHandler.escolheLugar(lugar);
		}catch (ReservaInvalidaException e) {
			throw new ApplicationException(e.getMessage());
		}
		
	}

	// UC3
	public String indicaEmail(String email) throws ApplicationException {
		try {
			return reservaHandler.indicaEmail(email);
		} catch (ReservaInvalidaException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	// UC4	
	public int comprarBilhetesPasse(String nomeEvento) throws ApplicationException {
			try {
				return reservaHandler.getNumBilhetesPasseDisponiveis(nomeEvento);
			} catch (ReservaInvalidaException e) {
				throw new ApplicationException(e.getMessage());
			}
		
	}

	// UC4
	public String indicaNumBilhetesEmail(int numBilhetes, String email) throws ApplicationException {
		try {
			return reservaHandler.indicaNumBilhetesPasseEmail(numBilhetes, email);
		} catch (ReservaInvalidaException e) {
			throw new ApplicationException(e.getMessage());
		}
	}


}
