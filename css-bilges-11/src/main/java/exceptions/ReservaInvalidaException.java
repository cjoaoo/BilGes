package exceptions;

public class ReservaInvalidaException extends Exception {

	private static final long serialVersionUID = -3643001529598828006L;


	public ReservaInvalidaException(String msg) {
		super(msg);
	}

	public ReservaInvalidaException(String msg, Exception e) {
		super(msg, e);
	}

	

}
