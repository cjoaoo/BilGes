package business.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import exceptions.DatabaseException;
import exceptions.InstalacaoException;


public class CatalogoInstalacoes {
	
	private EntityManager em;

	public CatalogoInstalacoes(EntityManager em) {
		this.em = em;
	}

	public String getListaInstalacoes() throws InstalacaoException {
		TypedQuery<String> query = em.createNamedQuery(Instalacao.GET_NOMES_INSTALACOES, String.class);
		try {
			List<String> lista = query.getResultList();
			if(lista.isEmpty()) {
				throw new InstalacaoException("Nao existem instalacoes.");
			}
			StringBuilder s = new StringBuilder();
			for(String instalacao: lista) {
				s.append(instalacao + ", ");
			}
			return s.toString().substring(0, s.length()-2);
		}catch(Exception e) {
			throw new InstalacaoException("Erro ao procurar na base de dados.");
		}
	}

	public Instalacao findByName(String nomeInstalacao) throws InstalacaoException {
		TypedQuery<Instalacao> query = em.createNamedQuery(Instalacao.FIND_BY_NAME, Instalacao.class);
		query.setParameter(Instalacao.NOME_INSTALACAO, nomeInstalacao);
		try {
			return query.getSingleResult();
		}catch(Exception e) {
			throw new InstalacaoException("Não existe instalação com esse nome.");
		}
	}
	
	public boolean instalacaoLivreNasDatas(EntityManager em, Instalacao instalacao, List<LocalDate> diasEvento) throws DatabaseException {
		for(LocalDate d: diasEvento) {
			TypedQuery<Long> query = em.createNamedQuery(Instalacao.INSTALACAO_OCUPADA, Long.class);
			query.setParameter(Instalacao.INSTALACAO_ID, instalacao.getId());
			query.setParameter(EventoData.DIA_EVENTO, d);	
			try {
			if(query.getSingleResult() > 0) {
				return false;
			}
			}catch(Exception e) {
				throw new DatabaseException("Erro ao procurar na base de dados");
			}
		}
		return true;
		
	}
}
