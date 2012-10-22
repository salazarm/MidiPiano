package player;

public class Parser {
	
	private final Lexer lexer;
	
	/**
	 * Creates Parser over passed Lexer.
	 * @param lexer Lexer object to parse
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	public Lexer getLexer() {
		return this.lexer;
	}

}
