package datatypes;

import java.util.ArrayList;

public class Voice {
	
	private final String voiceName;
	private ArrayList<MusicSequence> musicSequences;
	private Player player;
	private int curTick = 0;
	
	public Voice(String voiceName, Player player) {
		this.voiceName = voiceName;
		this.player = player;
	}
	
	public void addToVoice(MusicSequence musicSequence) {
		this.musicSequences.add(musicSequence);
	}
	
	public String getVoiceName() {
		return this.voiceName;
	}

}
