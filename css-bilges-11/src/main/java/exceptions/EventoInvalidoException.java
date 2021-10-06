package exceptions;

public class EventoInvalidoException extends Exception {

	private static final long serialVersionUID = -6681669849458336491L;
	
	public EventoInvalidoException(String msg) {
		super(msg);
	}
	
	public EventoInvalidoException(String msg, Exception e) {
		super(msg, e);
	}
	

}
