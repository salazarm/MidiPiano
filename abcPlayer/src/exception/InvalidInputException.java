package exception;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidInputException() {}
	
	public InvalidInputException(String message) {
		super(message);
	}
}
