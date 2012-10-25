package datatypes;

import java.util.ArrayList;
import java.util.List;

public class Body extends MusicSequence {
	
	private List<Voice> voiceList;

	public Body() {
		this.voiceList = new ArrayList<Voice>();
		super.setStartTick(0);
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
	
    public <R> R accept(Visitor<R> v)
    {
        return v.onBody(this);
    }
}
