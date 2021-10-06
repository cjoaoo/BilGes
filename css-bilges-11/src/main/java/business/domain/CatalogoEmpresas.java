package business.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import exceptions.NotFoundException;

public class CatalogoEmpresas {
	
	private EntityManager em;
	
	public CatalogoEmpresas(EntityManager em) {
		this.em = em;
	}

	public Empresa getEmpresaByNumRegisto(int numEmpresa) throws NotFoundException {
		TypedQuery<Empresa> query = em.createNamedQuery(Empresa.FIND_BY_NUMREGISTO, Empresa.class);
		query.setParameter(Empresa.NUM_REGISTO, numEmpresa);
		try {
			return query.getSingleResult();
		}catch(PersistenceException e) {
			throw new NotFoundException("Empresa nao encontrada", e);
		}
	}
	
	
}
