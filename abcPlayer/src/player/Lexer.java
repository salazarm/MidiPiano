package player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class Lexer {
	private ArrayList<Token> headerTokens;
	private ArrayList<Token> bodyTokens;
	private int bodyStartIndex;
	
	public ArrayList<Token> getHeader(){return this.headerTokens; }
	public ArrayList<Token> getBody(){	return this.bodyTokens;	}
	
	public Lexer(String input){
	    this.headerTokens = processHeader(input);
	    this.bodyTokens = processBody(input);
	}
	
	// For test
	Lexer(){}
	
	/**
     * repeats end in :| start with |:
     * tuplet starts with (
     * things are separated by spaces
	 * @param input
	 * @return
	 */

	private ArrayList<Token> processBody(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		for(int i = bodyStartIndex; i<input.length(); i++){
			if(Pattern.matches("\\s",""+input.charAt(i))){
				int j;
				for(int k =1;!Pattern.matches("\\s",""+input.charAt(i+k)); k++){
					j =k;
					if (Pattern.matches("[a-gA-Gz,\\^_']","+input.charAt(i+k)")){
						tokens.add(new Token(input.charAt(i+k)+"",input.charAt(i+k)+""));
					}
					else if(Pattern.matches("\\|",""+input.charAt(i+k)) && !Pattern.matches(""+input.charAt(i+k+1), "[\\]:]"))
						tokens.add(new Token("\\|","\\|"));
					else if(Pattern.matches( ":",""+input.charAt(i+k))){
						tokens.add(new Token(":\\|",":\\|")); i+=1;
					}
					else if(Pattern.matches("\\|",""+input.charAt(i+k))){
						tokens.add(new Token("\\|:","\\|:")); i+=1;
					}
					else if(Pattern.matches("\\([2-4]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("("+input.charAt(i+k+1), "("+input.charAt(i+k+1))); i+=1;
					}
					else if(Pattern.matches("\\[0-9]",""+input.charAt(i+k))){
						if(Pattern.matches("[ \\t\\n]",""+input.charAt(i+k+1))){
							tokens.add(new Token(""+input.charAt(i+k),""+input.charAt(i+k)));
						}
						else if(Pattern.matches("\\[0-9][ \\t\\n]",""+input.charAt(i+k)+input.charAt(i+k+1)+input.charAt(i+k+2))){
							tokens.add(new Token(""+input.charAt(i+k)+input.charAt(i+k+1),""+input.charAt(i+k)+input.charAt(i+k+1)));
						}	
						else if(Pattern.matches("[[0-9]\\[0-9]",""+input.charAt(i+k)+input.charAt(i+k+1)+input.charAt(i+k+2))){
							tokens.add(new Token(""+input.charAt(i+k)+input.charAt(i+k+1)+input.charAt(i+k+2),""+input.charAt(i+k)+input.charAt(i+k+1)+input.charAt(i+k+2)));
						}
						else{
							throw new RuntimeException("Invalid Character Sequence Encountered");
						}
					}
					else if(Pattern.matches("\\[[12]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("["+input.charAt(i+k+1), "["+input.charAt(i+k+1)));
					}
					else if(Pattern.matches("|[|\\]]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("|"+input.charAt(i+k+1),"|"+input.charAt(i+k+1)));
					}
					else if(Pattern.matches("\\[[za-gA-G]",""+input.charAt(i+k)+input.charAt(i+k+1))){
						tokens.add(new Token("[","["));
					}
					else if(Pattern.matches("\\][za-gA-G]",""+input.charAt(i+k)+input.charAt(i+k-1))){
						tokens.add(new Token("]","]"));
					}
					else{
						throw new RuntimeException("Unexpected Character: "+input.charAt(i+k));
					}i+=j;
				}
				
			}
		}
		return tokens;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public ArrayList<Token> processHeader(String input){
		ArrayList<Token> tokens = new ArrayList<Token>();
		for (int i=0; i< input.length(); i++){
		    
		    //  i012345
		    // T: title
		    
			if (input.charAt(i)==':'){
			    if(i==0) throw new RuntimeException("a header line starts with ':'");

				int k = 1;
				StringBuilder sb = new StringBuilder();
				while(i+k<input.length() && !Pattern.matches("\\n",input.charAt(i+k)+"")){
					sb.append(input.charAt(i+k));
					k++;
				}

				Token t = new Token(input.charAt(i-1)+"",sb.toString());
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
