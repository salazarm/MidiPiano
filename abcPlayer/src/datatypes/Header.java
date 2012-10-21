package datatypes;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Header {
	
	private String composer = "Unknown";
	private final String title;
	private final int indexNumber;
	private int tempo = 100;
	private double defaultNoteLength = 1/8;
	private final KeySignature keySignature;
	private Meter meter = new Meter(4,4);
	private String[] voiceNames;
	
	public Header(int indexNumber, String title, KeySignature keySignature) {
		this.indexNumber = indexNumber;
		this.title = title;
		this.keySignature = keySignature;
	}
	
	public String getComposer() {
		return this.composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public int getTempo() {
		return this.tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public double getDefaultNoteLength() {
		return this.defaultNoteLength;
	}

	public void setDefaultNoteLength(double defaultNoteLength) {
		this.defaultNoteLength = defaultNoteLength;
	}

	public Meter getMeter() {
		return this.meter;
	}

	public void setMeter(Meter meter) {
		this.meter = meter;
	}

	public String[] getVoiceNames() {
		return this.voiceNames;
	}

	public void setVoiceNames(String[] voiceNames) {
		this.voiceNames = voiceNames;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIndexNumber() {
		return this.indexNumber;
	}

	public KeySignature getKeySignature() {
		return this.keySignature;
	}
}
