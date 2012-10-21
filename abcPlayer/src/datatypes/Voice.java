package datatypes;

import java.util.List;

public class Voice {
	
	private final String voiceName;
	private List<MusicSequence> musicSequences;
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
	
	public List<MusicSequence> getMusicSequences() {
		return this.musicSequences;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getCurTick() {
		return this.curTick;
	}

}
