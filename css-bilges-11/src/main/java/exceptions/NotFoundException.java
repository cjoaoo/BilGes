package exceptions;

public class NotFoundException extends Exception {


	private static final long serialVersionUID = 1473684668098975313L;
	
	public NotFoundException(String msg) {
		super(msg);
	}
	
	public NotFoundException(String msg, Exception e) {
		super(msg, e);
	}

}
