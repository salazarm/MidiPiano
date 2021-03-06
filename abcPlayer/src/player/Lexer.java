package player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	protected List<Token> headerTokens;
	protected List<Token> bodyTokens;
	public int bodyStartIndex = 1;
	private int headerIterator, bodyIterator;
	
	/**
	 * @return an List of Tokens representing the tokens in the header of the abc file
	 */
	public List<Token> getHeader(){	return this.headerTokens; }
	/**
	 * @return a List of Tokens representing the tokens in the body of the abc file
	 */
	public List<Token> getBody(){	return this.bodyTokens;	}
	
	
	/**
	 * Takes a String representation of an abc file as input and creates an ArrayList of tokens out of it.
	 * @param input String representation of an abc file
	 */
	public Lexer(String input){
	    this.headerTokens = processHeader(input);
	    this.bodyTokens = processBody(input);
	}
	
	// Do not erase this constructor!
	// It's used by JUnit test.
	Lexer() { }
	
	/**
	 * Use next() to get next token.
	 * @return The next token if exists, or null.
	 */
	public Token nextHeader() { return 
			(headerIterator < headerTokens.size())? headerTokens.get(headerIterator++) : null;}
	/**
	 * Use peek() to get a token without advancing the iterator.
	 * @return The next token if exists, or null.
	 */
	public Token peekHeader() { return (headerIterator < headerTokens.size())? headerTokens.get(headerIterator  ) : null;}
	/**
	 * Use next() to get next token.
	 * @return The next token if exists, or null.
	 */
	public Token nextBody() { return (bodyIterator < bodyTokens.size())? bodyTokens.get(bodyIterator++) : null;}
	/**
	 * Use peek() to get a token without advancing the iterator.
	 * @return The next token if exists, or null.
	 */
	public Token peekBody() { return (bodyIterator < bodyTokens.size())? bodyTokens.get(bodyIterator  ) : null;}
	
	/**
	 * Advances the header iterator
	 * @param type Expected Token type of next token
	 * @throws RuntimeException if next Token not the same Type as the passed Token
	 */
	public void consumeHeader(Token.Type type) { if( nextHeader().getType() != type ) throw new RuntimeException("Expected token: "+type.toString()); }

	/**
	 * Advances the Body iterator
	 * @param type Expected Token type of next token
	 * @throws RuntimeException if next Token not the same Type as the passed Token
	 */
	public void consumeBody(Token.Type type) { if( nextBody().getType() != type ) throw new RuntimeException("Expected token: "+type.toString()); }

	private static final Pattern digitsFractdigits = Pattern.compile("\\A\\d+/\\d+");
	private static final Pattern fractDigits = Pattern.compile("\\A/\\d+");
	private static final Pattern loneFract = Pattern.compile("\\A/");
	private static final Pattern digitFract = Pattern.compile("\\A\\d+/");
	private static final Pattern soloNumber = Pattern.compile("\\A\\d+");

	/**
	 * Processes the given input and creates Tokens
	 * @param input String representation of the body of an abc file
	 * @return List of Tokens representing the tokenized body of the input file
	 * @throws RuntimeException if invalid substring encountered
	 */
	protected List<Token> processBody(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		input = input + " ";
		for (int i=bodyStartIndex; i <input.length(); i++){
			if (Pattern.matches("\\A[za-gA-G\\^,_\\='][\\s\\S]*", input.substring(i,input.length()-1))){ // Note, Accidentals, Octaves
				tokens.add(new Token(input.charAt(i)+"",input.charAt(i)+""));
				continue;
			}
			else if (Pattern.matches("\\A\\d+/\\d+[\\s\\S]*",input.substring(i,input.length()-1))){  // Number/Number
				Matcher matcher = digitsFractdigits.matcher(input.substring(i,input.length()-1));
				matcher.find();
				tokens.add(new Token(matcher.group(),matcher.group()));
				i += matcher.end()-1;
				continue;
			}
			else if (Pattern.matches("\\A/\\d+[\\s\\S]*", input.substring(i,input.length()))){ //  /Number
				Matcher matcher = fractDigits.matcher(input.substring(i,input.length()));
				matcher.find();
				tokens.add(new Token(matcher.group(),matcher.group()));
				i+=matcher.end()-1;
				continue;
			}
			else if(Pattern.matches("\\A/[^\\d][\\s\\S]*", input.substring(i,input.length()))){ // / (Lone Fraction Sign)
				Matcher matcher = loneFract.matcher(input.substring(i,input.length()));
				matcher.find();
				tokens.add(new Token(matcher.group(),matcher.group()));
				i+=matcher.end()-1;
				continue;
			}
			else if(Pattern.matches("\\A\\d+/[^\\d][\\s\\S]*", input.substring(i,input.length()-1))){ // Number/
				Matcher matcher = digitFract.matcher(input.substring(i,input.length()));
				matcher.find();
				tokens.add(new Token(matcher.group(),matcher.group()));
				i+=matcher.end()-1; continue;
			}
			else if(Pattern.matches("\\A\\d+[^/][\\s\\S]*", input.substring(i,input.length()))){ // Number
				Matcher matcher = soloNumber.matcher(input.substring(i,input.length()));
				matcher.find();
				tokens.add(new Token(matcher.group(), matcher.group()));
				i+=matcher.end()-1; continue;
			}
			else if(Pattern.matches("\\A:\\|[\\s\\S]*", input.substring(i,input.length()))){ // :|
				tokens.add(new Token(":|",":|")); i++; continue;
			}
			else if(Pattern.matches("\\A\\|:[\\s\\S]*", input.substring(i,input.length()))){  // |:
				tokens.add(new Token("|:","|:")); i++; continue;
			}
			else if(Pattern.matches("\\A\\|(\\||\\])[^:][\\s\\S]*", input.substring(i,input.length()))){ // || or |]
				tokens.add(new Token("|"+input.charAt(i+1),"|"+input.charAt(i+1))); i++;
			}
			else if(Pattern.matches("\\A\\|[\\s\\S]*", input.substring(i,input.length()))){  // |
				tokens.add(new Token("|","|")); continue;
			}
			else if(Pattern.matches("\\A\\([2-4][\\s\\S]*", input.substring(i,input.length()))){  // (2, (3, (4
				tokens.add(new Token(""+input.substring(i,i+2), ""+input.substring(i,i+2))); i++; continue;
			}
			else if(Pattern.matches("\\A\\[[1-2][\\s\\S]*", input.substring(i,input.length()))){  // [1, [2
				tokens.add(new Token(""+input.substring(i,i+2), ""+input.substring(i,i+2))); i++; continue;
			}
			else if(Pattern.matches("\\A\\[[\\s\\S]*", input.substring(i,input.length()-1))){ // [
				tokens.add(new Token("[", "["));
			}
			else if(Pattern.matches("\\A\\][\\s\\S]*", input.substring(i,input.length()-1))){ // ]
				tokens.add(new Token("]","]"));
			}
			else if(Pattern.matches("\\AV:[\\s\\S]*", input.substring(i,input.length()))){  // V:
				StringBuilder sb = new StringBuilder();
				for (int j = 0; i+j < input.length() && !Pattern.matches("[\\n\\r]",input.charAt(i+j)+"");j++ ){
					sb.append(input.charAt(i+j));
				}
				String string = sb.toString();
				StringBuilder sb2 = new StringBuilder();
				for(int qi =0; qi <string.length() && string.charAt(qi)!='%'; qi++){
					sb2.append(string.charAt(qi));
				}
				i+=sb.length()-1;
				tokens.add(new Token(1+sb2.toString(), sb2.toString()));
			}
			else if(Pattern.matches("\\A\\s[\\s\\S]*",input.substring(i,input.length()))){ // whitespace
				continue;
			}
			else if(input.charAt(i)=='%'){
				while(input.charAt(i)!='\n'){
					i++;
				}
			}
			else{
				throw new RuntimeException("Unexpected character sequence"+ input.substring(i-1,i+2));
			}
		}
		
		return tokens;
	}

	/**
	 * Processes the given input and creates Tokens
	 * @param input String representation of the header of an abc file
	 * @return List of Tokens representing the tokenized header of the input file
	 * @throws RuntimeException if header formatted incorrectly or invalid substrings present
	 */
	protected ArrayList<Token> processHeader(String input){
		ArrayList<Token> tokens = new ArrayList<Token>();
		for (int i=0; i< input.length(); i++){	
			if (input.charAt(i)=='%'){
				while(input.charAt(i) != '\n'){
					i++;
				}
				continue;
			}
			if (input.charAt(i)==':'){
			    if(i==0) throw new RuntimeException("a header line starts with ':'");

				int k = 1;
				StringBuilder sb = new StringBuilder();
				while(i+k<input.length() && !Pattern.matches("[\\r\\n]",input.charAt(i+k)+"")){
					sb.append(input.charAt(i+k));
					k++;
				}
				String a;
				if (input.charAt(i-1) == 'C'){
					a = "Ci";
				}
				else{	
					a = input.charAt(i-1)+"";
				}
				String string = sb.toString();
				StringBuilder sb2 = new StringBuilder();
				for(int qi =0; qi <string.length() && string.charAt(qi)!='%'; qi++){
					sb2.append(string.charAt(qi));
				}
				Token t = new Token(a,sb2.toString());
				if(Pattern.matches("\\A[LX]",a)){
					if (!Pattern.matches("\\A\\s*\\d++(/{1}\\d++)??\\s*\\z", t.getValue())){
						throw new RuntimeException("Got: "+t.getValue()+", For: "+t.getType()+", Instead of: Number");
					}
				}
				else if(Pattern.matches("\\AM", a)){
					if (!Pattern.matches("\\A\\s*\\d++(/{1}\\d++)??\\s*\\z", t.getValue())
							&& !Pattern.matches("\\s*C\\|?\\s*", t.getValue())){
						throw new RuntimeException("Got: "+t.getValue()+", For: "+t.getType()+", Instead of: Number");
					}
				}
				tokens.add(t);
				i+=k;
				if(t.getType() == Token.Type.KEY){
                     this.bodyStartIndex = i;
                     break;
                }			
			}
		}
		if(tokens.size()<3) throw new RuntimeException("A header needs at least X, T, K");
		if(tokens.get(0).getType()!=Token.Type.INDEX)
		    throw new RuntimeException("The first line of a header should be X");
		if(tokens.get(1).getType()!=Token.Type.TITLE)
            throw new RuntimeException("The second line of a header should be T");
		if(tokens.get(tokens.size()-1).getType()!=Token.Type.KEY)
            throw new RuntimeException("The last line of a header should be K");

		return tokens;
	}
}
