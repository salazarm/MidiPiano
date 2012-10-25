package datatypes;

public class Header {
    
    private String composer = "Unknown";
    private final String title;
    private final int indexNumber;
    private int tempo = 100;
    private Fraction defaultNoteLengthFraction = new Fraction(1, 8);
    private double defaultNoteLength = defaultNoteLengthFraction.getValue();
    private final KeySignature keySignature;
    private Fraction meter = new Fraction(4,4);
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

    public void setDefaultNoteLengthFraction(Fraction defaultNoteLengthFraction) {
        this.defaultNoteLengthFraction = defaultNoteLengthFraction;
        this.defaultNoteLength = this.defaultNoteLengthFraction.getValue();
    }
    
    public Fraction getDefaultNoteLengthFraction() {
        return this.defaultNoteLengthFraction;
    }

    public Fraction getMeter() {
        return this.meter;
    }

    public void setMeter(Fraction meter) {
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
    
    @Override
    public String toString() {
    	String voices = "";
    	for (String voice : voiceNames) {
    		voices += "V: " + voice + "\n";
    	}
        return String.format("X: %s\n" +
                "T: %s\n" +
                "C: %s\n" +
                "M: %s\n" +
                "L: %s\n" +
                "Q: %s\n" +
                "%s" + 
                "K: %s\n", this.getIndexNumber(), this.getTitle(), this.getComposer(),
                this.getMeter().toString(), this.getDefaultNoteLengthFraction().toString(),
                this.getTempo(), voices, this.getKeySignature().getStringRep());
    }
}
