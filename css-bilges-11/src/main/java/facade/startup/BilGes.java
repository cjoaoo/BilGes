package facade.startup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import application.CriarEventoService;
import application.ReservaBilhetesService;
import business.handlers.CriacaoEventoHandler;
import business.handlers.ReservaBilhetesHandler;
import exceptions.ApplicationException;

public class BilGes {
	
	private EntityManagerFactory emf;

	public void run() throws ApplicationException {
		try {
			emf = Persistence.createEntityManagerFactory("domain-model-jpa");			
		}catch(Exception e) {
			throw new ApplicationException("Erro ao ligar à base de dados.", e);
		}
		
	}
	
	// retorna um servico novo porque o servico mantem estado
	public CriarEventoService getCriarEventoService() {
		return new CriarEventoService(new CriacaoEventoHandler(emf));
	}

	// retorna um servico novo porque o servico mantem estado
	public ReservaBilhetesService getReservaBilhetesService() {
		return new ReservaBilhetesService(new ReservaBilhetesHandler(emf));
	}
	
	public void stopRun() {
		if(emf != null) {
			emf.close();
		}	
	}

}
