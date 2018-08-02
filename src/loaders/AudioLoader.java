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
	}

	public void loadSound(String assetName) {
		Integer x = pointerCounterSound.get(assetName);
		if (x == null || x.equals(0)) {
			// load fresh from HDD
			AudioResource audioResource = new AudioResource("res/" + assetName + ".wav");
			assetsSound.put(assetName, audioResource);
			pointerCounterSound.put(assetName, 1);
		}
	}

	public void unloadSound(String assetName) {
		Integer x = pointerCounterSound.get(assetName);
		if (x == null) {
			return;
		}

		if (x.equals(1)) {
			pointerCounterSound.put(assetName, x.intValue() - 1);
			// unload completely
			assetsSound.get(assetName).destroy();
			assetsSound.put(assetName, null);
		} else {
			pointerCounterSound.put(assetName, x.intValue() - 1);
		}
	}

	public AudioResource getSound(String assetName) {
		return assetsSound.get(assetName);
	}

}
