package exception;

/*
 * ParseException indicates an error that occurred while parsing lexed tokens
 */

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 3173367395747162768L;
	
	public ParseException() {}
	
	public ParseException(String message) {
		super(message);
	}

}
