package exceptions;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -6734356790893560061L;


	public ApplicationException(String msg) {
		super (msg);
	}
	
	
	public ApplicationException(String msg, Exception e) {
		super (msg, e);
	}
	
	

}
