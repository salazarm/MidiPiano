package datatypes;

public interface Visitor<R> {
	public R onNote(Note note);
	public R onChord(Chord chord);
	public R onRest(Rest rest);
	public R onRepeat(Repeat repeat);
	public R onTuplet(Tuplet tuplet);
}
