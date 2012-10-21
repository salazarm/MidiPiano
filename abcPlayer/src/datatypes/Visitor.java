package datatypes;

public interface Visitor {
	public void onNote(Note note);
	public void onChord(Chord chord);
	public void onRest(Rest rest);
	public void onRepeat(Repeat repeat);
	public void onTuplet(Tuplet tuplet);
}
