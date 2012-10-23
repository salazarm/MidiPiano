package player;

import java.util.List;
import datatypes.*;
import exception.ParseException;

public class Parser {
	
	private final Lexer lexer;
	private Header header;
	
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
	
	public void parseHeader() {
		List<Token> headerTokens = this.lexer.getHeader();
		if(headerTokens.get(0).getType()!=Token.Type.INDEX) {
			throw new ParseException("Header must start with index number");
		}
		String index = headerTokens.get(0).getValue();
	}
}