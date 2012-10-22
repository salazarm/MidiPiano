package datatypes;

public interface Visitor {	
	public int duration(Note note);
	public int duration(Chord chord);
	public int duration(Rest rest);
	public int duration(Repeat repeat);
	public int duration(Tuplet tuplet);
	public int duration (Voice voice);
	public int duration(Body body);
	public void onNote(Note note);
	public void onChord(Chord chord);
	public void onRest(Rest rest);
	public void onRepeat(Repeat repeat);
	public void onTuplet(Tuplet tuplet);
	public void onVoice(Voice voice);
	public void onBody(Body body);
}
