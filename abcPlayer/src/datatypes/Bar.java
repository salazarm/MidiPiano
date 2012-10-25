package datatypes;

import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;

public class Bar extends MusicSequence {
	
	private List<MusicSequence> components;
	
	public Bar() {
		this.components = new ArrayList<MusicSequence>();
	}
	
	public void addComponent(MusicSequence component) {
		if (!(component instanceof Note) && !(component instanceof Chord) 
				&& !(component instanceof Tuplet) && !!(component instanceof Rest)) {
			throw new InvalidInputException("Invalid operation: Bar must consist of Note," +
					"Chord, Tuplet or Rest");
		}
		components.add(component);
	}
	
	public List<MusicSequence> getSequences()
	{
	    return components;
	}

	public <R> R accept(Visitor<R> v) {
		return v.onBar(this);
	}
}