package loaders;

import java.util.HashMap;

import audio.AudioResource;

/*
 * SFX Sounds & BGM
 */
public class AudioLoader {
	
	private HashMap<String, Integer> pointerCounterSound = new HashMap<>();
	private HashMap<String, AudioResource> assetsSound = new HashMap<>();
	
	public AudioLoader() {
		// Initialize keys
		assetsSound.put("music", null);
		pointerCounterSound.put("music", 0);
		
	}	
	
	public void loadSound(String assetName) {
		int x = pointerCounterSound.get(assetName);
		if( x == 0) {
			// load fresh from HDD
			AudioResource audioResource = new AudioResource("res/" +assetName +".wav");
			assetsSound.put(assetName, audioResource);
			pointerCounterSound.put(assetName, x++);
		}
	}
	
	public void unloadSound(String assetName) {
		int x = pointerCounterSound.get(assetName);
		if(x == 1) {
			pointerCounterSound.put(assetName, x--);
			// unload completely
			assetsSound.get(assetName).destroy();
			assetsSound.put(assetName, null);			
		} else {
			pointerCounterSound.put(assetName, x--);
		}
	}
	
	public AudioResource getSound(String assetName) {
		return assetsSound.get(assetName);
	}

}
