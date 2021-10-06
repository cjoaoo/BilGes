package client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import exceptions.ApplicationException;

import application.CriarEventoService;
import application.ReservaBilhetesService;
import facade.dto.DataDTO;
import facade.dto.LugarDTO;
import facade.startup.BilGes;

public class SimpleClient {

	private SimpleClient() {
		//construtor vazio
	}

	public static void main(String[] args) {

		BilGes app = new BilGes();

		try {
			app.run();	
			testesCriacaoEvento(app);
			testesAtribuicaoInstalacao(app);
			testesVendaBilhetesIndividuais(app);
			testesVendaBilhetesPasse(app);
			app.stopRun();		
		}catch(ApplicationException e) {
			System.out.println("Erro: " + e.getMessage());
		}

	}

	//---------------------------------------------------------------------------------------------//


	private static void testesCriacaoEvento(BilGes app) {

		// Caso 1
		ArrayList<DataDTO> datas = new ArrayList<>();
		datas.add(new DataDTO(LocalDate.of(2021, 5, 9), LocalTime.of(21,0), LocalTime.of(0,0)));		
		testeCriacaoEvento(app, 1, "Bye Semestre X", "TeteATete", 1, datas);

		// Caso 2
		datas = new ArrayList<>();
		datas.add(new DataDTO(LocalDate.of(2021, 5, 9), LocalTime.of(20, 0), LocalTime.of(22, 0)));	
		testeCriacaoEvento(app, 2, "Bye Semestre Y", "TeteATete", 1, datas);

		// Caso 3
		datas = new ArrayList<>();
		datas.add(new DataDTO(LocalDate.of(2021, 7, 17), LocalTime.of(21, 0), LocalTime.of(23, 30)));	
		datas.add(new DataDTO(LocalDate.of(2021, 7, 18), LocalTime.of(15, 0), LocalTime.of(20, 0)));	
		testeCriacaoEvento(app, 3, "Open dos Exames", "BandoSentado", 1, datas);

		// Caso 4 -> deve dar erro: produtora nao tem certificado para este tipo de evento
		datas = new ArrayList<>();
		datas.add(new DataDTO(LocalDate.of(2021, 7, 31), LocalTime.of(21, 0), LocalTime.of(23, 0)));	
		datas.add(new DataDTO(LocalDate.of(2021, 8, 1), LocalTime.of(14, 0), LocalTime.of(19, 0)));
		testeCriacaoEvento(app, 4, "Festival Estou de Ferias", "MultidaoEmPe", 1, datas);

		// Caso 5 -> deve dar erro: datas nao sao concecutivas
		datas = new ArrayList<>();
		datas.add(new DataDTO(LocalDate.of(2021, 12, 31), LocalTime.of(21, 0), LocalTime.of(23, 0)));	
		datas.add(new DataDTO(LocalDate.of(2021, 8, 1), LocalTime.of(14, 0), LocalTime.of(19, 0)));
		testeCriacaoEvento(app, 5, "Festival Estou de Ferias", "MultidaoEmPe", 2, datas);

		// Caso 6
		datas = new ArrayList<>();
		datas.add(new DataDTO(LocalDate.of(2021, 7, 31), LocalTime.of(21, 0), LocalTime.of(23, 0)));	
		datas.add(new DataDTO(LocalDate.of(2021, 8, 1), LocalTime.of(14, 0), LocalTime.of(19, 0)));
		testeCriacaoEvento(app, 6, "Festival Estou de Ferias", "MultidaoEmPe", 2, datas);

	}


	private static void testesAtribuicaoInstalacao(BilGes app) {

		// Caso 7 -> deve dar erro: capacidade de lugares sentados superior à dimensao maxima de participantes do tipo de evento
		testeAtribuicaoInstalacao(app, 7, "Bye Semestre X", "Mini Estadio", LocalDate.of(2021, 5, 1), 0);

		// Caso 8
		testeAtribuicaoInstalacao(app, 8, "Bye Semestre X", "Micro Pavilhao", LocalDate.of(2021, 5, 1), 5);

		// Caso 9 -> deve dar erro: instalacao ocupada nesse dia
		testeAtribuicaoInstalacao(app, 9, "Bye Semestre Y", "Micro Pavilhao", LocalDate.of(2021, 5, 1), 20);

		// Caso 10
		testeAtribuicaoInstalacaoComPasse(app, 10, "Open dos Exames", "Mini Estadio", LocalDate.of(2021, 5, 1), 20, 30);

		// Caso 11
		testeAtribuicaoInstalacao(app, 11, "Festival Estou de Ferias", "Pequeno Relvado", LocalDate.of(2021, 5, 1), 15);
	}

	private static void testesVendaBilhetesIndividuais(BilGes app) {
		// Caso 12
		testeBilheteIndividual(app, 12, "Bye Semestre X", LocalDate.of(2021, 5, 9), 3, "u1@gmail.com");

		// Caso 13 -> deve dar erro: bilhete nao está disponivel (esta reservado)
		testeBilheteIndividual(app, 13, "Bye Semestre X", LocalDate.of(2021, 5, 9), 1, "u2@gmail.com");

		// Caso 14
		testeBilheteIndividual(app, 14, "Bye Semestre X", LocalDate.of(2021, 5, 9), 1, "u2@gmail.com");

		// Caso 15 -> deve dar erro: evento nao tem lugares marcados
		testeBilheteIndividual(app, 15, "Festival Estou de Ferias", LocalDate.of(2021, 7, 31), 0, "u3@gmail.com");

		// Caso 16
		testeBilheteIndividual(app, 16, "Open dos Exames", LocalDate.of(2021, 7, 17), 2, "u3@gmail.com");


	}



	private static void testesVendaBilhetesPasse(BilGes app) {
		// Caso 17
		testeBilhetePasse(app, 17, "Open dos Exames", 2, "u4@gmail.com");

		// Caso 18
		testeBilhetePasse(app, 18, "Open dos Exames", 3, "u5@gmail.com");

		// Caso 19 -> suposto dar erro: evento nao tem opçao de bilhetes-passe
		testeBilhetePasse(app, 19, "Festival Estou de Ferias", 7, "u6@gmail.com");

	}

	//---------------------------------------------------------------------------------------------//

	private static void testeCriacaoEvento(BilGes app, int numCasoTeste, String nome, String tipo, int numEmpresa, ArrayList<DataDTO> datas) {
		CriarEventoService servicoEvento = app.getCriarEventoService();
		System.out.println("\nCaso teste " + numCasoTeste + ":");
		try {
			String tiposEventos = servicoEvento.iniciaCriacaoEvento();
			System.out.println("Tipos de eventos existentes: " + tiposEventos);
			servicoEvento.indicaDetalhesEvento(nome, tipo, numEmpresa);
			System.out.println("Dados inseridos: " + nome + ", " + tipo + ", " + numEmpresa);
			for(DataDTO data: datas) {	
				servicoEvento.indicaData(data);
				System.out.println("Dados inseridos: dia " + data.toString());
			}
			servicoEvento.terminar();
			System.out.println("Evento " + nome + " adicionado com sucesso.\n");
		}catch(ApplicationException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	private static void testeAtribuicaoInstalacao(BilGes app, int numCasoTeste, String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes, float precoIndividual) {
		CriarEventoService servicoEvento = app.getCriarEventoService();
		System.out.println("\nCaso teste " + numCasoTeste + ":");
		try {
			String listaInstalacoes = servicoEvento.getListaInstalacoes();
			System.out.println("Instalações: " + listaInstalacoes);
			servicoEvento.escolheInstalacao(nomeEvento, nomeInstalacao, dataVendaBilhetes, precoIndividual);
			System.out.println("Instalação " + nomeInstalacao + " atribuída com sucesso ao evento " + nomeEvento + ".");
		}catch(ApplicationException e) {
			System.out.println("Erro: " + e.getMessage());
		}	
	}

	private static void testeAtribuicaoInstalacaoComPasse(BilGes app, int numCasoTeste, String nomeEvento, String nomeInstalacao, LocalDate dataVendaBilhetes, float precoIndividual, float precoPasse) {
		CriarEventoService servicoEvento = app.getCriarEventoService();
		System.out.println("\nCaso teste " + numCasoTeste + ":");
		try {
			String listaInstalacoes = servicoEvento.getListaInstalacoes();
			System.out.println("Instalações registadas: "+ listaInstalacoes);
			servicoEvento.escolheInstalacao(nomeEvento, nomeInstalacao, dataVendaBilhetes, precoIndividual, precoPasse);
			System.out.println("Instalação " + nomeInstalacao + " atribuída com sucesso ao evento " + nomeEvento + ".");
		}catch(ApplicationException e) {
			System.out.println("Erro: " + e.getMessage());
		}	
	}


	private static void testeBilheteIndividual(BilGes app, int numCasoTeste, String nomeEvento, LocalDate data, int numLugares, String email) {

		ReservaBilhetesService res = app.getReservaBilhetesService();
		System.out.println("\nCaso teste " + numCasoTeste + ":");
		try {
			List<DataDTO> datas = res.getDatasComBilhetesDisponiveis(nomeEvento);
			System.out.println("Datas com bilhetes disponiveis no evento " + nomeEvento + ":");
			for(DataDTO d: datas) {
				System.out.println(d.toString());
			}
			
			List<LugarDTO> lugaresDisponiveis = res.escolheData(data);
			System.out.println("Lugares disponíveis: ");
			for(LugarDTO l: lugaresDisponiveis) {
				System.out.println(l.toString());
			}
			
			for(int i = 0; i < numLugares; i++) {
				// o caso teste 13 necessita de input de bilhete nao disponivel
				if(numCasoTeste==13) {
					res.escolheLugar(new LugarDTO("B", 1));
				}else {
					res.escolheLugar(lugaresDisponiveis.remove(0));
				}
				
			}
			String pagamento = res.indicaEmail(email);
			System.out.println(pagamento);
			
		}catch(ApplicationException e) {
			System.out.println("Erro: " + e.getMessage());
		}


	}


	private static void testeBilhetePasse(BilGes app, int numCasoTeste, String nomeEvento, int numBilhetes, String email) {

		ReservaBilhetesService res = app.getReservaBilhetesService();
		System.out.println("\nCaso teste " + numCasoTeste + ":");
		try {
			int numDisponiveis = res.comprarBilhetesPasse(nomeEvento);
			System.out.println("Número de bilhetes disponíveis no evento "+ nomeEvento +": " + numDisponiveis);
			System.out.println("Número de bilhetes pedidos: " + numBilhetes);
			String pagamento = res.indicaNumBilhetesEmail(numBilhetes, email);
			System.out.println(pagamento);
			
		}catch(ApplicationException e) {
			System.out.println("Erro: " + e.getMessage());
		}

	}

}
