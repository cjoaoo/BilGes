package exceptions;

public class InstalacaoException extends Exception {


	private static final long serialVersionUID = -4724961343089365657L;
	
	public InstalacaoException(String msg) {
		super (msg);
	}
	
	
	public InstalacaoException(String msg, Exception e) {
		super (msg, e);
	}
	

}
