package exception;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 4321848764240875364L;

	public InvalidInputException() {}
	
	public InvalidInputException(String message) {
		super(message);
	}
}