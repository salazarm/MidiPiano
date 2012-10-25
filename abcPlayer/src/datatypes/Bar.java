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
	}
	

	public <R> R accept(Visitor<R> v) {
		v.onBar(this);
	}
	

}
