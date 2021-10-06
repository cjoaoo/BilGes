package exceptions;

public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1845773185335215452L;

	public DatabaseException(String msg) {
		super(msg);
	}
	
	public DatabaseException(String msg, Exception e) {
		super(msg, e);
	}
}
