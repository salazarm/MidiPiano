package player;

// TODO: Octave token ' and , are not accidentals.

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	private ArrayList<Token> headerTokens;
	private ArrayList<Token> bodyTokens;
	public int bodyStartIndex;
	private int headerIterator, bodyIterator;
	
	/**
	 * @return an ArrayList of Tokens representing the tokens in the header of the abc file
	 */
	public ArrayList<Token> getHeader(){	return this.headerTokens; }
	/**
	 * @return an ArrayList of Tokens representing the tokens in the body of the abc file
	 */
	public ArrayList<Token> getBody(){	return this.bodyTokens;	}
	
	
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
	 * Use next() to get next a token.
	 * Use peek() to not advance the iterator.
	 * @return The next token if exist, or null.
	 */
	public Token nextHeader() { return (headerIterator < headerTokens.size())? headerTokens.get(headerIterator++) : null;}
	public Token peekHeader() { return (headerIterator < headerTokens.size())? headerTokens.get(headerIterator  ) : null;}
	public Token nextBody() { return (bodyIterator < bodyTokens.size())? bodyTokens.get(bodyIterator++) : null;}
	public Token peekBody() { return (bodyIterator < bodyTokens.size())? bodyTokens.get(bodyIterator  ) : null;}
	
	public void consumeHeader(Token.Type type) { if( nextHeader().getType() != type ) throw new RuntimeException("Expected token: "+type.toString()); }
	public void consumeBody(Token.Type type) { if( nextBody().getType() != type ) throw new RuntimeException("Expected token: "+type.toString()); }


	protected ArrayList<Token> processBody(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		for(int i = bodyStartIndex; i<input.length(); i++){
				int j = 0;
				for(int k =1;i+k<input.length() && !Pattern.matches("\\s",""+input.charAt(i+k)); k++){
					j =k;
					if (Pattern.matches("[a-gA-Gz,\\^_'=]",""+input.charAt(i+k))){
						tokens.add(new Token(input.charAt(i+k)+"",input.charAt(i+k)+""));
					}
					else if((""+input.charAt(i+k)).equals(":")){
						tokens.add(new Token(":|",":|")); k+=1;
					}
					else if((""+input.charAt(i+k)).equals("|:")){
						tokens.add(new Token("|:","|:")); k+=1;
					}
					else if(Pattern.matches("\\([2-4]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("("+input.charAt(i+k+1), "("+input.charAt(i+k+1))); k+=1;
					}
					else if(Pattern.matches("\\A[/0-9]",input.charAt(i+k)+"")){
						boolean slash = false;
						StringBuilder num = new StringBuilder();
						int s = 0;
						while(Pattern.matches("\\A[/0-9]",input.charAt(i+k+s)+"")){
							if(input.charAt(i+k+s) == '/'){
								if(slash == true){
									throw new RuntimeException("Invalid Character Sequence");
								}
								num.append('/');
								slash = true;
							}	
							if(Pattern.matches("\\A[0-9]",""+input.charAt(i+k))){
								num.append(input.charAt(i+k));
							}	
							s++;
						}
						tokens.add(new Token(num.toString(),num.toString()));
//						System.out.println(num.toString()+" : "+ input.charAt(i+k) + (tokens.size()-1)); 
						k += s-1;
					}
					else if(Pattern.matches("\\[[12]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("["+input.charAt(i+k+1), "["+input.charAt(i+k+1)));k++;
					}
					else if( ("|]").equals(""+input.charAt(i+k)+input.charAt(i+k+1)) || ("||") .equals(""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("|"+input.charAt(i+k+1),"|"+input.charAt(i+k+1)));k++;
					}
					else if(Pattern.matches("\\[[\\^za-gA-G_]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("[","["));
					}
					else if(Pattern.matches("\\][,za-gA-G_0-9]",""+input.charAt(i+k)+input.charAt(i+k-1))){
						tokens.add(new Token("]","]"));
					}
					else if(Pattern.matches("\\nV",""+input.charAt(i+k-1)+input.charAt(i+k))){
						StringBuilder sb = new StringBuilder();
						for(; !Pattern.matches("\\n",""+input.charAt(i+k));k++){
							sb.append(input.charAt(i+k));
						}
						tokens.add(new Token(sb.toString(),sb.toString()));
					}
					else if(input.charAt(i+k) == '|'){
						tokens.add(new Token("|","|"));
					}
					else if(input.charAt(i+k) == '%'){
						while (!Pattern.matches("\\n",""+input.charAt(i+k))){
							k++;
						}
					}
					else if(input.charAt(i+k)=='/'){
						tokens.add(new Token("/","/"));
					}
					else{
						throw new RuntimeException("Unexpected Character: "+input.charAt(i+k)+" in: "+input.charAt(i+k-2) +input.charAt(i+k-1)+input.charAt(i+k)+input.charAt(i+k+1));
					}
				}i+=j;
				
		}
		return tokens;
	}


	protected ArrayList<Token> processHeader(String input){
		ArrayList<Token> tokens = new ArrayList<Token>();
		for (int i=0; i< input.length(); i++){		    
			if (input.charAt(i)==':'){
			    if(i==0) throw new RuntimeException("a header line starts with ':'");

				int k = 1;
				StringBuilder sb = new StringBuilder();
				while(i+k<input.length() && !Pattern.matches("\\n",input.charAt(i+k)+"")){
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
				Token t = new Token(a,sb.toString());
				if(Pattern.matches("\\A[MLX]",a)){
					if (!Pattern.matches("\\A\\s*\\d++(/{1}\\d++)??\\s*\\z", t.getValue())){
						throw new RuntimeException("Got: "+t.getValue()+", For: "+t.getType()+", Instead of: Number");
					}
				}
				tokens.add(t);
				i+=k;
				if(t.getType() == Token.Type.KEY){
                     this.bodyStartIndex = i+k;
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
