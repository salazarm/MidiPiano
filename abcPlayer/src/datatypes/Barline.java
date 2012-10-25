package datatypes;

import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;

public class Barline extends MusicSequence {
	public <R> R accept(Visitor<R> v) {
		return v.onBarline(this);
	}
}
