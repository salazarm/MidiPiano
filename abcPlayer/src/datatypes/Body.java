package datatypes;

import java.util.ArrayList;
import java.util.List;

public class Body {
	
	private List<Voice> voiceList;
	
	public Body() {
		this.voiceList = new ArrayList<Voice>();
	}
	
	public void addVoice(Voice voice) {
		this.voiceList.add(voice);
	}
	
	public List<Voice> getVoiceList() {
		return this.voiceList;
	}
}
