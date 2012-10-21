package datatypes;

import java.util.ArrayList;
import java.util.List;

public class Body implements MusicSequence {
	
	private List<Voice> voiceList;
	private final Player player;
	
	public Body(Player player) {
		this.voiceList = new ArrayList<Voice>();
		this.player = player;
	}
	
	/**
	 * Add voice to List of Voices in this Body
	 * @param voice Voice object to be added to the Body
	 */
	public void addVoice(Voice voice) {
		this.voiceList.add(voice);
	}
	
	public List<Voice> getVoiceList() {
		return this.voiceList;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Returns duration of this Body, defined as the duration of the Voice with the longest
	 * duration in ticks.
	 * @return duration int value of duration in ticks, as defined above
	 */
	public int getDuration() {
		int duration = 0;
		for (Voice voice : this.voiceList) {
			duration = voice.getDuration();
		}
		return duration;
	}
	
	public void schedule(Visitor v) {
		v.onBody(this);
	}
}
