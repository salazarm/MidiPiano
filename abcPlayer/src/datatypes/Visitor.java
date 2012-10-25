package datatypes;

public interface Visitor<R> {	
	public R onNote(Note note);
	public R onChord(Chord chord);
	public R onRest(Rest rest);
	public R onTuplet(Tuplet tuplet);
	public R onVoice(Voice voice);
	public R onBody(Body body);	
}
